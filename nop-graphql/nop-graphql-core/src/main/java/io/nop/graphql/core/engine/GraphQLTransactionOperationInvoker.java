/**
 * Copyright (c) 2017-2024 Nop Platform. All rights reserved.
 * Author: canonical_entropy@163.com
 * Blog:   https://www.zhihu.com/people/canonical-entropy
 * Gitee:  https://gitee.com/canonical-entropy/nop-entropy
 * Github: https://github.com/entropy-cloud/nop-entropy
 */
package io.nop.graphql.core.engine;

import io.nop.commons.functional.IAsyncFunctionInvoker;
import io.nop.graphql.core.IDataFetchingEnvironment;
import io.nop.graphql.core.IGraphQLExecutionContext;
import io.nop.graphql.core.ast.GraphQLOperationType;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public class GraphQLTransactionOperationInvoker implements IAsyncFunctionInvoker {

    private IAsyncFunctionInvoker transactionalInvoker;

    public void setTransactionalInvoker(IAsyncFunctionInvoker invoker) {
        this.transactionalInvoker = invoker;
    }

    @Override
    public <R, T> CompletionStage<T> invokeAsync(Function<R, CompletionStage<T>> task, R request) {
        IGraphQLExecutionContext context = ((IDataFetchingEnvironment) request).getGraphQLExecutionContext();
        if (context.getOperation().getOperationType() == GraphQLOperationType.mutation) {
            return transactionalInvoker.invokeAsync(task, request);
        } else {
            return task.apply(request);
        }
    }
}
