package io.nop.batch.dsl.manager;

import io.nop.api.core.beans.query.QueryBean;
import io.nop.api.core.exceptions.NopException;
import io.nop.api.core.ioc.IBeanProvider;
import io.nop.batch.core.BatchTaskBuilder;
import io.nop.batch.core.IBatchAggregator;
import io.nop.batch.core.IBatchChunkContext;
import io.nop.batch.core.IBatchChunkProcessorBuilder;
import io.nop.batch.core.IBatchConsumerProvider;
import io.nop.batch.core.IBatchLoaderProvider;
import io.nop.batch.core.IBatchMetaProvider;
import io.nop.batch.core.IBatchProcessorProvider;
import io.nop.batch.core.IBatchRecordFilter;
import io.nop.batch.core.IBatchStateStore;
import io.nop.batch.core.IBatchTaskBuilder;
import io.nop.batch.core.consumer.EmptyBatchConsumer;
import io.nop.batch.core.consumer.MultiBatchConsumerProvider;
import io.nop.batch.core.consumer.ResourceRecordConsumerProvider;
import io.nop.batch.core.consumer.SplitBatchConsumer;
import io.nop.batch.core.filter.EvalBatchRecordFilter;
import io.nop.batch.core.loader.ResourceRecordLoaderProvider;
import io.nop.batch.core.processor.FilterBatchProcessor;
import io.nop.batch.core.processor.MultiBatchProcessorProvider;
import io.nop.batch.dsl.model.BatchChunkProcessorBuilderModel;
import io.nop.batch.dsl.model.BatchConsumerModel;
import io.nop.batch.dsl.model.BatchFileReaderModel;
import io.nop.batch.dsl.model.BatchFileWriterModel;
import io.nop.batch.dsl.model.BatchJdbcReaderModel;
import io.nop.batch.dsl.model.BatchListenersModel;
import io.nop.batch.dsl.model.BatchLoaderModel;
import io.nop.batch.dsl.model.BatchOrmReaderModel;
import io.nop.batch.dsl.model.BatchProcessorModel;
import io.nop.batch.dsl.model.BatchTaggerModel;
import io.nop.batch.dsl.model.BatchTaskModel;
import io.nop.batch.orm.loader.OrmQueryBatchLoaderProvider;
import io.nop.commons.collections.OrderByComparator;
import io.nop.commons.util.CollectionHelper;
import io.nop.commons.util.retry.IRetryPolicy;
import io.nop.core.lang.eval.IEvalFunction;
import io.nop.core.lang.xml.IXNodeGenerator;
import io.nop.core.lang.xml.XNode;
import io.nop.core.reflect.bean.BeanTool;
import io.nop.core.resource.IResourceLoader;
import io.nop.core.resource.VirtualFileSystem;
import io.nop.core.resource.record.IResourceRecordIO;
import io.nop.core.resource.record.csv.CsvResourceRecordIO;
import io.nop.dao.api.IDaoProvider;
import io.nop.dao.api.IQueryBuilder;
import io.nop.dao.jdbc.IJdbcTemplate;
import io.nop.dao.txn.ITransactionTemplate;
import io.nop.dao.utils.TransactionalFunctionInvoker;
import io.nop.dataset.record.IRecordSplitter;
import io.nop.dataset.record.IRecordTagger;
import io.nop.dataset.record.support.RecordTagSplitter;
import io.nop.orm.IOrmEntity;
import io.nop.orm.IOrmTemplate;
import io.nop.orm.utils.SingleSessionFunctionInvoker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import static io.nop.batch.dsl.BatchDslErrors.ARG_BATCH_TASK_NAME;
import static io.nop.batch.dsl.BatchDslErrors.ERR_BATCH_TASK_NO_LOADER;

public class ModelBasedBatchTaskBuilderFactory {
    private final String batchTaskName;
    private final BatchTaskModel batchTaskModel;
    private final ITransactionTemplate transactionTemplate;
    private final IOrmTemplate ormTemplate;

    private final IJdbcTemplate jdbcTemplate;
    private final IDaoProvider daoProvider;
    private final IBatchStateStore stateStore;

    public ModelBasedBatchTaskBuilderFactory(String batchTaskName, BatchTaskModel batchTaskModel,
                                             IBatchStateStore stateStore,
                                             ITransactionTemplate transactionTemplate,
                                             IOrmTemplate ormTemplate, IJdbcTemplate jdbcTemplate,
                                             IDaoProvider daoProvider) {
        this.batchTaskName = batchTaskName;
        this.stateStore = stateStore;
        this.batchTaskModel = batchTaskModel;
        this.transactionTemplate = transactionTemplate;
        this.ormTemplate = ormTemplate;
        this.jdbcTemplate = jdbcTemplate;
        this.daoProvider = daoProvider;
    }

    public IBatchTaskBuilder newTaskBuilder(IBeanProvider beanContainer) {
        BatchTaskBuilder<Object, Object> builder = new BatchTaskBuilder<>();
        builder.batchSize(batchTaskModel.getBatchSize());
        if (batchTaskModel.getJitterRatio() != null)
            builder.jitterRatio(batchTaskModel.getJitterRatio());
        if (batchTaskModel.getSingleMode() != null) {
            builder.singleMode(batchTaskModel.getSingleMode());
        }
        if (batchTaskModel.getSingleSession() != null) {
            builder.singleSession(batchTaskModel.getSingleSession());
        }

        if (batchTaskModel.getRateLimit() != null)
            builder.rateLimit(batchTaskModel.getRateLimit());

        if (batchTaskModel.getTransactionScope() != null)
            builder.transactionScope(batchTaskModel.getTransactionScope());
        if (batchTaskModel.getRetryOneByOne() != null)
            builder.retryOneByOne(batchTaskModel.getRetryOneByOne());
        if (batchTaskModel.getConcurrency() > 0)
            builder.concurrency(batchTaskModel.getConcurrency());
        if (batchTaskModel.getExecutor() != null) {
            builder.executor((Executor) beanContainer.getBean(batchTaskModel.getExecutor()));
        }

        if (batchTaskModel.getRetryPolicy() != null) {
            builder.retryPolicy((IRetryPolicy<IBatchChunkContext>) batchTaskModel.getRetryPolicy().buildRetryPolicy());
        }

        if (batchTaskModel.getSkipPolicy() != null) {
            builder.skipPolicy(batchTaskModel.getSkipPolicy().buildSkipPolicy());
        }

        if (batchTaskModel.getInputSorter() != null) {
            builder.inputComparator(new OrderByComparator<>(batchTaskModel.getInputSorter(),
                    BeanTool::getComplexProperty));
        }

        if (transactionTemplate != null)
            builder.transactionalInvoker(new TransactionalFunctionInvoker(transactionTemplate));

        if (ormTemplate != null) {
            builder.singleSessionInvoker(new SingleSessionFunctionInvoker(ormTemplate));
        }

        builder.stateStore(stateStore);

        if (batchTaskModel.getLoadRetryPolicy() != null) {
            builder.loadRetryPolicy((IRetryPolicy<IBatchChunkContext>) this.batchTaskModel.getLoadRetryPolicy().buildRetryPolicy());
        }

        buildTask(builder, beanContainer);

        return builder;
    }

    private void buildTask(BatchTaskBuilder<Object, Object> builder, IBeanProvider beanContainer) {
        IBatchLoaderProvider<Object> loader = buildLoader(builder, beanContainer);
        if (loader == null)
            throw new NopException(ERR_BATCH_TASK_NO_LOADER)
                    .source(batchTaskModel)
                    .param(ARG_BATCH_TASK_NAME, batchTaskName);
        builder.loader(loader);

        if (batchTaskModel.getProcessors() != null) {
            List<IBatchProcessorProvider<?, ?>> list = new ArrayList<>(batchTaskModel.getProcessors().size());

            for (BatchProcessorModel processorModel : batchTaskModel.getProcessors()) {
                IBatchProcessorProvider<Object, Object> processor = buildProcessor(processorModel, builder, beanContainer);
                if (processorModel.getFilter() != null) {
                    list.add(newFilterProcessor(processorModel.getFilter()));
                }
                list.add(processor);
            }
            builder.processor(MultiBatchProcessorProvider.fromList(list));
        }

        IBatchChunkProcessorBuilder<Object> chunkProcessor = buildChunkProcessorBuilder(builder, beanContainer);
        if (chunkProcessor != null)
            builder.chunkProcessorBuilder(chunkProcessor);

        IRecordTagger<Object, IBatchChunkContext> tagger = getTagger(beanContainer);
        IRecordSplitter<Object, Object, IBatchChunkContext> splitter = tagger == null ? null : new RecordTagSplitter<>(tagger);

        if (batchTaskModel.getConsumers().size() == 1) {
            IBatchConsumerProvider<Object> writer = getWriter(batchTaskModel.getConsumers().get(0), beanContainer);
            builder.consumer(writer);
        } else {
            Map<String, List<IBatchConsumerProvider<Object>>> map = new HashMap<>();
            for (BatchConsumerModel consumerModel : batchTaskModel.getConsumers()) {
                IBatchConsumerProvider<Object> writer = getWriter(consumerModel, beanContainer);
                map.computeIfAbsent(consumerModel.getForTag(), k -> new ArrayList<>()).add(writer);
            }

            List<IBatchConsumerProvider<Object>> list = map.remove(null);
            if (map.isEmpty()) {
                if (list != null) {
                    builder.consumer(MultiBatchConsumerProvider.fromList(list));
                }
            } else {
                List<IBatchConsumerProvider<Object>> writers = new ArrayList<>();

                if (splitter != null) {
                    Map<String, IBatchConsumerProvider<Object>> consumerMap = new HashMap<>();
                    map.forEach((name, consumers) -> {
                        IBatchConsumerProvider<Object> writer = MultiBatchConsumerProvider.fromList(consumers);
                        consumerMap.put(name, writer);
                    });

                    SplitBatchConsumer<Object, Object> writer = new SplitBatchConsumer<>(splitter,
                            (tag, ctx) -> consumerMap.get(tag).setup(ctx.getTaskContext()), false);
                    writers.add(writer);
                }
                if (list != null) {
                    writers.add(MultiBatchConsumerProvider.fromList(list));
                }
                builder.consumer(MultiBatchConsumerProvider.fromList(writers));
            }
        }
    }

    private IBatchProcessorProvider<Object, Object> newFilterProcessor(IEvalFunction func) {
        return new FilterBatchProcessor<>(new EvalBatchRecordFilter<>(func));
    }

    @SuppressWarnings("unchecked")
    private IBatchLoaderProvider<Object> buildLoader(BatchTaskBuilder<Object, Object> builder, IBeanProvider beanContainer) {
        if (batchTaskModel.getLoader() == null) {
            return null;
        }

        addListeners(builder, batchTaskModel.getLoader());

        BatchLoaderModel reader = batchTaskModel.getLoader();
        return buildLoader(reader, beanContainer);
    }

    private IBatchLoaderProvider<Object> buildLoader(BatchLoaderModel loaderModel, IBeanProvider beanProvider) {
        IBatchLoaderProvider<Object> provider = buildLoader0(loaderModel, beanProvider);
        if (loaderModel.getAdapter() == null || provider == null)
            return provider;

        return context -> {
            IBatchLoaderProvider.IBatchLoader<Object> loader = provider.setup(context);
            return (IBatchLoaderProvider.IBatchLoader<Object>) loaderModel.getAdapter().call1(null, loader, context.getEvalScope());
        };
    }


    @SuppressWarnings("unchecked")
    private IBatchLoaderProvider<Object> buildLoader0(BatchLoaderModel loaderModel, IBeanProvider beanProvider) {
        if (loaderModel.getBean() != null) {
            return (IBatchLoaderProvider<Object>) beanProvider.getBean(loaderModel.getBean());
        }

        IBatchAggregator<Object, Object, Map<String, Object>> aggregator = loadAggregator(loaderModel.getAggregator(), beanProvider);

        if (loaderModel.getFileReader() != null) {
            return buildFileReader(loaderModel.getFileReader(), beanProvider, aggregator);
        } else if (loaderModel.getJdbcReader() != null) {
            return buildJdbcReader(loaderModel.getJdbcReader());
        } else if (loaderModel.getOrmReader() != null) {
            return buildOrmReader(loaderModel.getOrmReader());
        } else if (loaderModel.getSource() != null) {
            return context -> (batchSize, ctx) -> (List<Object>) loaderModel.getSource().call2(null,
                    batchSize, ctx, ctx.getEvalScope());
        } else {
            return null;
        }
    }

    private IBatchAggregator<Object, Object, Map<String, Object>> loadAggregator(String beanName, IBeanProvider beanContainer) {
        if (beanName != null)
            return null;
        return (IBatchAggregator) beanContainer.getBean(beanName);
    }

    private IBatchLoaderProvider<Object> buildOrmReader(BatchOrmReaderModel loaderModel) {
        IXNodeGenerator query = loaderModel.getQuery();
        List<String> batchLoadProps = loaderModel.getBatchLoadProps();

        OrmQueryBatchLoaderProvider<IOrmEntity> loader = new OrmQueryBatchLoaderProvider<>();
        loader.setBatchLoadProps(batchLoadProps);
        loader.setDaoProvider(daoProvider);
        if (query != null)
            loader.setQueryBuilder(newQueryBuilder(query));
        //loader.setSqlGenerator(loaderModel.getEql());

        return (IBatchLoaderProvider) loader;
    }

    private IQueryBuilder newQueryBuilder(IXNodeGenerator generator) {
        return context -> {
            XNode node = generator.generateNode(context);
            return BeanTool.buildBeanFromTreeBean(node, QueryBean.class);
        };
    }

    private IBatchLoaderProvider<Object> buildJdbcReader(BatchJdbcReaderModel loaderModel) {
        return null;
    }

    private IBatchLoaderProvider<Object> buildFileReader(BatchFileReaderModel loaderModel,
                                                         IBeanProvider beanContainer,
                                                         IBatchAggregator<Object, Object, Map<String, Object>> aggregator) {
        IResourceRecordIO<Object> recordIO = loadRecordIO(loaderModel.getResourceIO(), beanContainer);
        IResourceLoader resourceLoader = loadResourceLoader(loaderModel.getResourceLoader(), beanContainer);

        ResourceRecordLoaderProvider<Object> loader = new ResourceRecordLoaderProvider<>();
        loader.setName("reader");
        loader.setRecordIO(recordIO);
        loader.setResourceLoader(resourceLoader);
        if (loaderModel.getMaxCount() != null)
            loader.setMaxCount(loaderModel.getMaxCount());
        loader.setPathExpr(loaderModel.getPathExpr());
        loader.setEncoding(loaderModel.getEncoding());
        loader.setAggregator(aggregator);

        return loader;
    }

    private IResourceRecordIO<Object> loadRecordIO(String beanName, IBeanProvider beanContainer) {
        if (beanName != null)
            return (IResourceRecordIO<Object>) beanContainer.getBean(beanName);
        return new CsvResourceRecordIO<>();
    }

    private IResourceLoader loadResourceLoader(String loaderBean, IBeanProvider beanContainer) {
        if (loaderBean != null)
            return (IResourceLoader) beanContainer.getBean(loaderBean);
        return VirtualFileSystem.instance();
    }

    @SuppressWarnings("unchecked")
    private IBatchProcessorProvider<Object, Object> buildProcessor(BatchProcessorModel processorModel,
                                                                   BatchTaskBuilder<Object, Object> builder, IBeanProvider beanContainer) {
        addListeners(builder, processorModel);

        IBatchProcessorProvider<Object, Object> provider = buildProcessor0(processorModel, beanContainer);
        if (processorModel.getAdapter() == null)
            return null;
        return context -> {
            IBatchProcessorProvider.IBatchProcessor<Object, Object> processor = provider.setup(context);
            return (IBatchProcessorProvider.IBatchProcessor<Object, Object>) processorModel.getAdapter().call1(null, processor, context.getEvalScope());
        };
    }

    private IBatchProcessorProvider<Object, Object> buildProcessor0(BatchProcessorModel processorModel, IBeanProvider beanContainer) {
        if (processorModel.getBean() != null)
            return (IBatchProcessorProvider) beanContainer.getBean(processorModel.getBean());

        return context -> (item, consumer, ctx) -> {
            processorModel.getSource().call3(null, item, consumer, ctx, ctx.getEvalScope());
        };
    }

    private IBatchChunkProcessorBuilder<Object> buildChunkProcessorBuilder(BatchTaskBuilder<Object, Object> builder, IBeanProvider beanContainer) {
        if (batchTaskModel.getChunkProcessorBuilder() == null)
            return null;

        addListeners(builder, batchTaskModel.getChunkProcessorBuilder());

        BatchChunkProcessorBuilderModel processorModel = batchTaskModel.getChunkProcessorBuilder();
        if (processorModel.getBean() != null)
            return (IBatchChunkProcessorBuilder<Object>) beanContainer.getBean(processorModel.getBean());

        return null;
    }

    private void addListeners(BatchTaskBuilder<Object, Object> builder, BatchListenersModel listenersModel) {
        if (listenersModel.getOnTaskBegin() != null)
            builder.addTaskInitializer(context -> {
                context.onTaskBegin(() -> {
                    listenersModel.getOnTaskBegin().call1(null, context, context.getEvalScope());
                });
            });

        if (listenersModel.getOnTaskEnd() != null)
            builder.addTaskInitializer(context -> {
                context.onAfterComplete(err -> {
                    listenersModel.getOnTaskEnd().call2(null, err, context, context.getEvalScope());
                });
            });

        if (listenersModel.getOnChunkBegin() != null) {
            builder.addTaskInitializer(context -> {
                context.onChunkBegin(ctx -> {
                    listenersModel.getOnChunkBegin().call1(null, ctx, ctx.getEvalScope());
                });
            });
        }

        if (listenersModel.getOnChunkEnd() != null) {
            builder.addTaskInitializer(context -> {
                context.onChunkEnd((err, ctx) -> {
                    listenersModel.getOnChunkEnd().call2(null, err, ctx, ctx.getEvalScope());
                });
            });
        }
    }

    private IRecordTagger<Object, IBatchChunkContext> getTagger(IBeanProvider beanContainer) {
        if (batchTaskModel.getTagger() == null)
            return null;

        BatchTaggerModel taggerModel = batchTaskModel.getTagger();
        if (taggerModel.getBean() != null)
            return (IRecordTagger) beanContainer.getBean(taggerModel.getBean());

        if (taggerModel.getSource() != null)
            return (record, ctx) ->
                    CollectionHelper.toCollection(taggerModel.getSource().call2(null,
                            record, ctx, ctx.getEvalScope()), true);
        return null;
    }

    private IBatchConsumerProvider<Object> getWriter(BatchConsumerModel consumerModel, IBeanProvider beanContainer) {
        IBatchConsumerProvider<Object> provider = getWriter0(consumerModel, beanContainer);
        if (consumerModel.getAdapter() == null)
            return null;
        return context -> {
            IBatchConsumerProvider.IBatchConsumer<Object> writer = provider.setup(context);
            return (IBatchConsumerProvider.IBatchConsumer<Object>) consumerModel.getAdapter().call1(null, writer, context.getEvalScope());
        };
    }

    private IBatchConsumerProvider<Object> getWriter0(BatchConsumerModel consumerModel, IBeanProvider beanContainer) {
        IBatchAggregator<Object, Object, Map<String, Object>> aggregator = loadAggregator(consumerModel.getAggregator(), beanContainer);
        IBatchMetaProvider metaProvider = loadMetaProvider(consumerModel.getMetaProvider(), beanContainer);

        IBatchConsumerProvider<Object> ret;
        if (consumerModel.getFileWriter() != null) {
            ResourceRecordConsumerProvider<Object> writer = newFileWriter(consumerModel.getFileWriter(), beanContainer);
            writer.setName(consumerModel.getName());
            writer.setAggregator(aggregator);
            writer.setMetaProvider(metaProvider);
            ret = writer;
        } else {
            ret = null;
        }
        return addFilterForWriter(consumerModel, ret);
    }

    private IBatchConsumerProvider<Object> addFilterForWriter(BatchConsumerModel consumerModel, IBatchConsumerProvider<Object> consumer) {
        if (consumerModel.getFilter() == null)
            return consumer;

        if (consumer == null)
            consumer = EmptyBatchConsumer.instance();

        IBatchRecordFilter<Object> filter = new EvalBatchRecordFilter<>(consumerModel.getFilter());
        return consumer.withFilter(filter);
    }

    private IBatchMetaProvider loadMetaProvider(String beanName, IBeanProvider beanContainer) {
        if (beanName == null)
            return null;
        return (IBatchMetaProvider) beanContainer.getBean(beanName);
    }

    private ResourceRecordConsumerProvider<Object> newFileWriter(BatchFileWriterModel consumerModel,
                                                                 IBeanProvider beanContainer) {
        IResourceRecordIO<Object> recordIO = loadRecordIO(consumerModel.getResourceIO(), beanContainer);
        IResourceLoader resourceLoader = loadResourceLoader(consumerModel.getResourceLoader(), beanContainer);

        ResourceRecordConsumerProvider<Object> writer = new ResourceRecordConsumerProvider<>();
        writer.setEncoding(consumerModel.getEncoding());
        writer.setPathExpr(consumerModel.getPathExpr());
        writer.setRecordIO(recordIO);
        writer.setResourceLoader(resourceLoader);
        return writer;
    }
}
