/**
 * Copyright (c) 2017-2024 Nop Platform. All rights reserved.
 * Author: canonical_entropy@163.com
 * Blog:   https://www.zhihu.com/people/canonical-entropy
 * Gitee:  https://gitee.com/canonical-entropy/nop-entropy
 * Github: https://github.com/entropy-cloud/nop-entropy
 */
package io.nop.batch.core;

import io.nop.api.core.beans.IntRangeBean;
import io.nop.core.context.IExecutionContext;
import io.nop.core.context.IServiceContext;
import io.nop.core.utils.IVarSet;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 一个批处理任务对应一个loader + processor + consumer的组合调用
 */
public interface IBatchTaskContext extends IExecutionContext {
    IServiceContext getServiceContext();

    String getTaskName();

    void setTaskName(String taskName);

    String getTaskId();

    void setTaskId(String taskId);

    String getTaskKey();

    void setTaskKey(String taskKey);

    /**
     * 外部传入的只读参数，在任务执行过程中不会被修改
     */
    Map<String, Object> getParams();

    void setParams(Map<String, Object> params);

    default Object getParam(String name) {
        Map<String, Object> params = getParams();
        return params == null ? null : params.get(name);
    }

    /**
     * 持久化的状态变量。当批处理任务失败后重试时可以读取上次处理状态
     */
    IVarSet getPersistVars();

    void setPersistVars(IVarSet vars);

    default Object getPersistVar(String name) {
        IVarSet vars = getPersistVars();
        if (vars == null)
            return null;
        return vars.getVar(name);
    }

    default void setPersistVar(String name, Object value) {
        IVarSet vars = getPersistVars();
        vars.setVar(name, value);
    }

    /**
     * 本次任务处理所涉及到的数据分区
     */
    IntRangeBean getPartition();

    void setPartition(IntRangeBean partition);

    boolean isRecoverMode();

    void setRecoverMode(boolean recoverMode);

    IBatchChunkContext newChunkContext();

    IBatchTaskMetrics getMetrics();

    void setMetrics(IBatchTaskMetrics metrics);

    /**
     * 处理过程中因为出错跳过的记录条目数
     */
    long getSkipItemCount();

    void incSkipItemCount(int count);

    void setSkipItemCount(long count);

    long getCompleteItemCount();

    void setCompleteItemCount(long count);

    void incCompleteItemCount(int count);

    long getCompletedIndex();

    void setCompletedIndex(long index);

    long getProcessItemCount();

    void setProcessItemCount(long processCount);

    void incProcessItemCount(int count);

    void onTaskBegin(Runnable action);

    void onChunkBegin(Consumer<IBatchChunkContext> action);

    void onBeforeChunkEnd(Consumer<IBatchChunkContext> action);

    void onChunkEnd(BiConsumer<Throwable, IBatchChunkContext> action);

    void fireTaskBegin();

    void fireChunkBegin(IBatchChunkContext chunkContext);

    void fireBeforeChunkEnd(IBatchChunkContext chunkContext);

    void fireChunkEnd(Throwable err, IBatchChunkContext chunkContext);
}