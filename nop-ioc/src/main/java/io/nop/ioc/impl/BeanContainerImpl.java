/**
 * Copyright (c) 2017-2024 Nop Platform. All rights reserved.
 * Author: canonical_entropy@163.com
 * Blog:   https://www.zhihu.com/people/canonical-entropy
 * Gitee:  https://gitee.com/canonical-entropy/nop-entropy
 * Github: https://github.com/entropy-cloud/nop-entropy
 */
package io.nop.ioc.impl;

import io.nop.api.core.ApiConstants;
import io.nop.api.core.ApiErrors;
import io.nop.api.core.config.AppConfig;
import io.nop.api.core.config.IConfigProvider;
import io.nop.api.core.exceptions.NopException;
import io.nop.api.core.ioc.BeanContainerStartMode;
import io.nop.api.core.ioc.IBeanContainer;
import io.nop.api.core.util.FutureHelper;
import io.nop.api.core.util.ICancellable;
import io.nop.commons.concurrent.executor.GlobalExecutors;
import io.nop.commons.lang.IClassLoader;
import io.nop.commons.lang.impl.Cancellable;
import io.nop.commons.util.ClassHelper;
import io.nop.core.execution.TaskExecutionGraph;
import io.nop.core.lang.xml.XNode;
import io.nop.ioc.api.BeanScopeContext;
import io.nop.ioc.api.IBeanContainerImplementor;
import io.nop.ioc.api.IBeanDefinition;
import io.nop.ioc.api.IBeanScope;
import io.nop.ioc.loader.AliasName;
import io.nop.ioc.loader.BeanDefinitionBuilder;
import io.nop.xlang.api.XLang;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import static io.nop.ioc.IocConstants.PRODUCER_BEAN_PREFIX;
import static io.nop.ioc.IocErrors.ARG_BEAN;
import static io.nop.ioc.IocErrors.ARG_BEANS;
import static io.nop.ioc.IocErrors.ARG_BEAN_NAME;
import static io.nop.ioc.IocErrors.ARG_BEAN_TYPE;
import static io.nop.ioc.IocErrors.ARG_CONTAINER_ID;
import static io.nop.ioc.IocErrors.ERR_IOC_CONTAINER_ALREADY_STARTED;
import static io.nop.ioc.IocErrors.ERR_IOC_CONTAINER_NOT_STARTED;
import static io.nop.ioc.IocErrors.ERR_IOC_MULTIPLE_BEAN_WITH_TYPE;
import static io.nop.ioc.IocErrors.ERR_IOC_NOT_PRODUCER_BEAN;
import static io.nop.ioc.IocErrors.ERR_IOC_PRODUCER_BEAN_NOT_INITED;

public class BeanContainerImpl implements IBeanContainerImplementor {
    static final Logger LOG = LoggerFactory.getLogger(BeanContainerImpl.class);

    private final String id;
    private final Map<String, BeanDefinition> enabledBeans;
    private final Collection<BeanDefinition> optionalBeans;

    private final IBeanContainer parentContainer;

    private final List<BeanDefinition> orderedBeans;
    private volatile boolean running;
    private volatile boolean started;

    private final Map<Class<?>, BeanTypeMapping> beansByType = new ConcurrentHashMap<>();
    private final Map<Class<? extends Annotation>, List<BeanDefinition>> beansByAnnotation = new ConcurrentHashMap<>();

    private IConfigProvider configProvider = AppConfig.getConfigProvider();

    private IClassLoader classLoader = ClassHelper.getSafeClassLoader();
    private IBeanClassIntrospection classIntrospection;

    private IBeanScope singletonScope;

    private BeanContainerStartMode startMode = BeanContainerStartMode.DEFAULT;
    private Map<String, AliasName> aliases;

    private Cancellable cancellable;

    private boolean concurrentStart;

    private CompletableFuture<?> startFuture;

    public BeanContainerImpl(String id, Map<String, BeanDefinition> enabledBeans,
                             Collection<BeanDefinition> optionalBeans,
                             Map<String, AliasName> aliases, IBeanContainer parentContainer) {
        this.id = id;
        this.enabledBeans = enabledBeans;
        this.aliases = aliases;
        this.orderedBeans = BeanTopologySorter.INSTANCE.sort(enabledBeans);
        this.parentContainer = parentContainer;
        this.optionalBeans = optionalBeans == null ? Collections.emptyList() : optionalBeans;

        for (BeanDefinition bean : this.orderedBeans) {
            if (bean.getBeanModel().getIocAfter() != null) {
                for (String afterId : bean.getBeanModel().getIocAfter()) {
                    BeanDefinition afterBean = enabledBeans.get(afterId);
                    if (afterBean != null) {
                        afterBean.addNextBean(bean.getId());
                    }
                }
            }
        }
    }

    BeanContainerImpl(String id, Map<String, BeanDefinition> enabledBeans,
                      Collection<BeanDefinition> optionalBeans,
                      Map<String, AliasName> aliases, IBeanContainer parentContainer, List<BeanDefinition> orderedBeans) {
        this.id = id;
        this.enabledBeans = enabledBeans;
        this.aliases = aliases;
        this.orderedBeans = orderedBeans;
        this.parentContainer = parentContainer;
        this.optionalBeans = optionalBeans == null ? Collections.emptyList() : optionalBeans;
    }

    @Override
    public IBeanContainer buildNewInstance(IBeanContainer parentContainer) {
        return new BeanContainerImpl(id, enabledBeans, optionalBeans, aliases, parentContainer, orderedBeans);
    }

    public void setConfigProvider(IConfigProvider configProvider) {
        this.configProvider = configProvider;
    }

    public void setClassLoader(IClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public IClassLoader getClassLoader() {
        return classLoader;
    }

    public IBeanClassIntrospection getClassIntrospection() {
        if (classIntrospection == null) {
            classIntrospection = new DefaultBeanClassIntrospection(classLoader);
        }
        return classIntrospection;
    }

    public void setClassIntrospection(IBeanClassIntrospection classIntrospection) {
        this.classIntrospection = classIntrospection;
    }

    public void setConcurrentStart(boolean concurrentStart) {
        this.concurrentStart = concurrentStart;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean containsBean(String name) {
        if (name.startsWith(PRODUCER_BEAN_PREFIX))
            name = name.substring(PRODUCER_BEAN_PREFIX.length());

        boolean b = enabledBeans.containsKey(name);
        if (!b) {
            if (parentContainer != null) {
                name = normalizeAlias(name);
                b = parentContainer.containsBean(name);
            }
        }
        return b;
    }

    private String normalizeAlias(String name) {
        AliasName alias = aliases.get(name);
        return alias == null ? name : alias.getName();
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Nonnull
    @Override
    public Object getBean(String name) {
        return getBean(name, false);
    }

    @Nonnull
    @Override
    public <T> T getBeanByType(Class<T> clazz) {
        return getBeanByType(clazz, false);
    }

    @Override
    public <T> T tryGetBeanByType(Class<T> requiredType) {
        checkStarted();
        BeanDefinition bean = findBeanByType(requiredType);
        if (bean == null) {
            if (parentContainer != null) {
                return parentContainer.tryGetBeanByType(requiredType);
            } else {
                return null;
            }
        }
        return (T) getBean0(bean, false, false);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> clazz) {
        checkStarted();

        BeanTypeMapping mapping = BeanFinder.getBeansByType(beansByType, orderedBeans, clazz);
        Map<String, T> ret = new HashMap<>();
        if (parentContainer != null) {
            ret.putAll(parentContainer.getBeansOfType(clazz));
        }
        for (BeanDefinition beanDef : mapping.getBeans()) {
            T bean = (T) getBean0(beanDef, false, false);
            ret.put(beanDef.getId(), bean);
        }
        return ret;
    }

    public IBeanDefinition getBeanDefinition(String name) {
        IBeanDefinition bean = enabledBeans.get(name);
        if (bean == null) {
            if (parentContainer instanceof IBeanContainerImplementor) {
                name = normalizeAlias(name);
                bean = ((IBeanContainerImplementor) parentContainer).getBeanDefinition(name);
            }
        }
        return bean;
    }

    @Override
    public Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annClass) {
        checkStarted();
        List<BeanDefinition> annBeans = BeanFinder.getBeansByAnnotation(beansByAnnotation, orderedBeans, annClass);
        Map<String, Object> ret = new HashMap<>();
        if (parentContainer != null) {
            ret.putAll(parentContainer.getBeansWithAnnotation(annClass));
        }
        for (BeanDefinition bean : annBeans) {
            Object instance = getBean0(bean, false, false);
            ret.put(bean.getId(), instance);
        }
        return ret;
    }

    @Override
    public String getBeanScope(String name) {
        BeanDefinition beanDef = enabledBeans.get(name);
        if (beanDef == null) {
            if (parentContainer != null) {
                return parentContainer.getBeanScope(normalizeAlias(name));
            }

            throw new NopException(ApiErrors.ERR_IOC_UNKNOWN_BEAN_FOR_NAME).param(ARG_BEAN_NAME, name);
        }
        return beanDef.getScope();
    }

    @Override
    public Class<?> getBeanClass(String name) {
        BeanDefinition beanDef = enabledBeans.get(name);
        if (beanDef == null) {
            if (parentContainer != null) {
                return parentContainer.getBeanClass(normalizeAlias(name));
            }

            throw new NopException(ApiErrors.ERR_IOC_UNKNOWN_BEAN_FOR_NAME).param(ARG_BEAN_NAME, name);
        }
        return beanDef.getBeanClass();
    }

    @Override
    public boolean containsBeanType(Class<?> clazz) {
        BeanTypeMapping mapping = BeanFinder.getBeansByType(beansByType, orderedBeans, clazz);
        boolean b = !mapping.isEmpty();
        if (!b) {
            if (parentContainer != null)
                b = parentContainer.containsBeanType(clazz);
        }
        return b;
    }

    @Override
    public Object getBean(@Nonnull String name, boolean includeCreating) {
        checkStarted();

        String rawName = name;
        boolean onlyProducer = name.startsWith(PRODUCER_BEAN_PREFIX);
        if (onlyProducer) {
            name = name.substring(PRODUCER_BEAN_PREFIX.length());
        }

        BeanDefinition bean = enabledBeans.get(name);
        if (bean == null) {
            if (parentContainer != null)
                return parentContainer.getBean(normalizeAlias(rawName));

            throw new NopException(ApiErrors.ERR_IOC_UNKNOWN_BEAN_FOR_NAME).param(ARG_BEAN_NAME, name);
        }

        if (onlyProducer && bean.getBeanMethod() == null)
            throw new NopException(ERR_IOC_NOT_PRODUCER_BEAN).param(ARG_BEAN_NAME, name);

        return getBean0(bean, onlyProducer, includeCreating);
    }

    @Override
    public <T> T getBeanByType(Class<T> requiredType, boolean includeCreating) {
        checkStarted();
        BeanDefinition bean = findBeanByType(requiredType);
        if (bean == null) {
            if (parentContainer != null)
                return parentContainer.getBeanByType(requiredType);

            throw new NopException(ApiErrors.ERR_IOC_UNKNOWN_BEAN_FOR_TYPE).param(ARG_BEAN_TYPE, requiredType)
                    .param(ARG_CONTAINER_ID, getId());
        }
        return (T) getBean0(bean, false, includeCreating);
    }

    @Override
    public String findAutowireCandidate(Class<?> beanType) {
        BeanDefinition beanDef = findBeanByType(beanType);
        if (beanDef == null) {
            if (parentContainer != null)
                return parentContainer.findAutowireCandidate(beanType);
            return null;
        }
        return beanDef.getId();
    }

    private BeanDefinition findBeanByType(Class<?> beanType) {
        BeanTypeMapping mapping = BeanFinder.getBeansByType(beansByType, orderedBeans, beanType);
        if (mapping.isEmpty())
            return null;
        if (mapping.getOtherPrimaryBean() != null)
            throw new NopException(ERR_IOC_MULTIPLE_BEAN_WITH_TYPE).param(ARG_BEAN_TYPE, beanType)
                    .param(ARG_BEANS, mapping.getBeans());

        BeanDefinition bean = mapping.getPrimaryBean();
        if (bean == null) {
            throw new NopException(ERR_IOC_MULTIPLE_BEAN_WITH_TYPE).param(ARG_BEAN_TYPE, beanType)
                    .param(ARG_BEANS, mapping.getBeans());
        }
        return bean;
    }

    public void refreshConfig(String beanId) {
        IBeanDefinition beanDef = getBeanDefinition(beanId);
        if (beanDef instanceof BeanDefinition) {
            BeanDefinition def = (BeanDefinition) beanDef;
            if (def.getRefreshConfigMethod() != null || def.getBeanModel().getIocRefreshConfig() != null) {
                IBeanScope beanScope = getBeanScope(def);
                Object bean = beanScope.get(def.getId());
                if (bean != null) {
                    def.onRefreshConfig(bean, this, beanScope);
                }
            }
        }
    }

    private Object getBean0(BeanDefinition beanDef, boolean onlyProducer, boolean includeCreating) {
        IBeanScope beanScope = getBeanScope(beanDef);

        if (includeCreating && beanScope != null) {
            Object bean = beanScope.get(beanDef.getId());
            if (bean != null) {
                Object createdBean = beanDef.getBeanInstance(bean, onlyProducer);
                if (createdBean == null) {
                    // 如果是并行启动，则允许等待一段时间
                    if (concurrentStart && !started)
                        createdBean = ((ProducedBeanInstance) bean).awaitGetBean(10000);
                    if (createdBean == null)
                        throw new NopException(ERR_IOC_PRODUCER_BEAN_NOT_INITED).param(ARG_BEAN, beanDef)
                                .param(ARG_BEAN_NAME, beanDef.getId());
                }

                return createdBean;
            }
        }

        boolean isNew = false;

        Object bean;
        if (beanScope == null) {
            bean = beanDef.newObject(null, this);
            bean = beanDef.getBeanInstance(bean, onlyProducer);
            isNew = true;
        } else {
            synchronized (beanDef) { //NOSONAR
                bean = beanScope.get(beanDef.getId());
                if (bean == null) {
                    LOG.info("nop.new-bean:{}", beanDef);
                    bean = beanDef.newObject(beanScope, this);
                    LOG.trace("nop.new-bean-completed:{}", beanDef);
                    if (isStarted() && beanDef.hasDelayMethod()) {
                        beanDef.runDelayMethod(bean, beanScope, this);
                    }
                    isNew = true;
                }
                bean = beanDef.getBeanInstance(bean, onlyProducer);
            }
        }

        if (isNew) {
            for (String nextId : beanDef.getNextBeans()) {
                getBean(nextId, true);
            }
        }
        return bean;
    }

    IBeanScope getBeanScope(BeanDefinition bean) {
        IBeanScope beanScope;
        if (bean.isSingleton()) {
            beanScope = singletonScope;
        } else if (bean.isPrototype()) {
            beanScope = null;
        } else {
            beanScope = BeanScopeContext.instance().getScope(this, bean.getScope());
        }
        return beanScope;
    }

    public boolean isStarted() {
        return started;
    }

    public void restart() {
        stop();
        start();
    }

    public BeanContainerStartMode getStartMode() {
        return startMode;
    }

    public void setStartMode(BeanContainerStartMode startMode) {
        this.startMode = startMode;
    }

    @Override
    public Object getConfigValue(String varName) {
        return configProvider.getConfigValue(varName, null);
    }

    @Override
    public IConfigProvider getConfigProvider() {
        return configProvider;
    }

    @Override
    public void start() {
        if (running)
            throw new NopException(ERR_IOC_CONTAINER_ALREADY_STARTED).param(ARG_CONTAINER_ID, id);

        startFuture = null;
        LOG.info("nop.ioc.start-container:containerId={}", getId());

        running = true;
        singletonScope = new BeanScopeImpl(ApiConstants.BEAN_SCOPE_SINGLETON, XLang.newEvalScope(), this);

        try {
            List<BeanDefinition> startBeans = new ArrayList<>();

            for (BeanDefinition bean : orderedBeans) {
                if (bean.isSingleton()) {
                    if (startMode == BeanContainerStartMode.ALL_LAZY) {
                        // 只创建具有delayMethod的bean
                        if (bean.hasDelayMethod() && !bean.isLazyInit() || bean.isIocForceInit()) {
                            startBean(startBeans, bean);
                        }
                    } else if (startMode == BeanContainerStartMode.ALL_EAGER || !bean.isLazyInit()) {
                        startBean(startBeans, bean);
                    }
                }
            }

            if (concurrentStart) {
                startFuture = asyncStartBeans(startBeans).whenComplete((ret, err) -> {
                    if (err != null) {
                        handleStartError(err);
                    } else {
                        runDelayMethod();
                        started = true;
                    }
                    LOG.info("nop.ioc.async-start-finished:{}", getId());
                });
            } else {
                runDelayMethod();
                started = true;
                LOG.info("nop.ioc.start-finished:{}", getId());
            }
        } catch (Exception e) {
            // dumpCreations();
            handleStartError(e);
            throw e;
        }
    }

    void handleStartError(Throwable e) {
        LOG.error("nop.err.ioc.start-failed", e);
        try {
            stop();
        } catch (Exception e2) {
            LOG.error("nop.err.ioc.stop-failed", e2);
        }
    }

    @Override
    public void awaitStartFinished() {
        if (startFuture != null)
            FutureHelper.syncGet(startFuture);
    }

    /*
    @DataBean
    public static class BeanCreation {
        final String threadName;
        final String beanId;
        final Set<String> depends;
        final boolean created;

        public BeanCreation(String threadName, String beanId, Set<String> depends, boolean created) {
            this.threadName = threadName;
            this.beanId = beanId;
            this.depends = depends;
            this.created = created;
        }

        public String toString() {
            return StringHelper.rightPad(threadName,25,' ') + " " + beanId + (created ? depends : "");
        }

        public String getThreadName() {
            return threadName;
        }

        public String getBeanId() {
            return beanId;
        }

        public Set<String> getDepends() {
            return depends;
        }

        public boolean isCreated() {
            return created;
        }
    }

    Queue<BeanCreation> beanCreations = new LinkedTransferQueue<>();

    void dumpCreations() {
        System.out.println(StringHelper.join(beanCreations,"\r\n"));
    }*/

    CompletableFuture<Void> asyncStartBeans(List<BeanDefinition> startBeans) {
        TaskExecutionGraph graph = new TaskExecutionGraph(GlobalExecutors.globalWorker(), "ioc-container-start");
        for (BeanDefinition bean : startBeans) {
            graph.addTaskWithDepends(bean.getId(),
                    cancelToken -> {
                        // beanCreations.add(new BeanCreation(Thread.currentThread().getName(), bean.getId(), graph.getDepends(bean.getId()), false));
                        getBean0(bean, true, false);
                        // beanCreations.add(new BeanCreation(Thread.currentThread().getName(), bean.getId(), graph.getDepends(bean.getId()), true));
                        return null;
                    },
                    bean.getDependBeanIds());
        }
        graph.analyze();
        this.cancellable = new Cancellable();
        return graph.executeAsync(this.cancellable);
    }

    void startBean(List<BeanDefinition> startBeans, BeanDefinition bean) {
        if (concurrentStart) {
            startBeans.add(bean);
        } else {
            getBean0(bean, true, false);
        }
    }

    void runDelayMethod() {
        for (BeanDefinition beanDef : orderedBeans) {
            if (beanDef.isSingleton() && beanDef.hasDelayMethod()) {
                IBeanScope beanScope = getBeanScope(beanDef);
                Object instance = beanScope.get(beanDef.getId());
                if (instance != null)
                    beanDef.runDelayMethod(instance, beanScope, this);
            }
        }
        LOG.info("nop.ioc.run-delay-method-finished");
    }

    @Override
    public void stop() {
        LOG.info("nop.ioc.stop-container:containerId={}", getId());
        running = false;
        started = false;
        if (cancellable != null) {
            cancellable.cancel(ICancellable.CANCEL_REASON_STOP);
            cancellable = null;
        }

        RuntimeException ex = null;
        if (singletonScope != null) {
            try {
                singletonScope.close();
            } catch (RuntimeException e) {
                LOG.error("nop.err.ioc.singleton-scope-close-fail:containerId={}", getId(), e);
                ex = e;
            }
        }
        BeanScopeContext.instance().onContainerStop(this);

        if (ex != null)
            throw ex;
    }

    void checkStarted() {
        if (!started && !running)
            throw new NopException(ERR_IOC_CONTAINER_NOT_STARTED).param(ARG_CONTAINER_ID, getId());
    }

    @Override
    public void destroyBean(String beanName, Object bean) {
        BeanDefinition beanDef = enabledBeans.get(beanName);
        if (beanDef != null) {
            IBeanScope beanScope = getBeanScope(beanDef);
            beanDef.destroyBean(bean, beanScope, this);
        }
    }

    @Override
    public boolean supportInjectTo() {
        return true;
    }

    @Override
    public void injectTo(Object bean) {
        LOG.debug("nop.inject-props-to-bean:{}", bean);
        checkStarted();
        Class<?> beanClass = bean.getClass();
        BeanDefinition beanDef = new BeanDefinitionBuilder(classLoader, getClassIntrospection(), this)
                .useBeans(this.enabledBeans).buildForAutowire(beanClass);

        beanDef.initProps(bean, this, null);
    }

    public XNode toConfigNode() {
        List<BeanDefinition> beans = new ArrayList<>();
        beans.addAll(orderedBeans);
        beans.addAll(optionalBeans);
        return new BeanContainerDumper().toConfigNode(beans);
    }

    @Override
    public Map<String, IBeanDefinition> getBeanDefinitionsByType(Class<?> clazz) {
        checkStarted();

        BeanTypeMapping mapping = BeanFinder.getBeansByType(beansByType, orderedBeans, clazz);
        Map<String, IBeanDefinition> ret = new HashMap<>();
        if (parentContainer instanceof IBeanContainerImplementor) {
            ret.putAll(((IBeanContainerImplementor) parentContainer).getBeanDefinitionsByType(clazz));
        }
        for (BeanDefinition beanDef : mapping.getBeans()) {
            ret.put(beanDef.getId(), beanDef);
        }
        return ret;
    }

    @Override
    public Map<String, IBeanDefinition> getBeanDefinitionsByAnnotation(Class<? extends Annotation> annClass) {
        checkStarted();
        List<BeanDefinition> annBeans = BeanFinder.getBeansByAnnotation(beansByAnnotation, orderedBeans, annClass);
        Map<String, IBeanDefinition> ret = new HashMap<>();
        if (parentContainer instanceof IBeanContainerImplementor) {
            ret.putAll(((IBeanContainerImplementor) parentContainer).getBeanDefinitionsByAnnotation(annClass));
        }
        for (BeanDefinition bean : annBeans) {
            ret.put(bean.getId(), bean);
        }
        return ret;
    }
}