/**
 * Copyright (c) 2017-2024 Nop Platform. All rights reserved.
 * Author: canonical_entropy@163.com
 * Blog:   https://www.zhihu.com/people/canonical-entropy
 * Gitee:  https://gitee.com/canonical-entropy/nop-entropy
 * Github: https://github.com/entropy-cloud/nop-entropy
 */
package io.nop.graphql.core.biz;

import io.nop.graphql.core.reflection.GraphQLBizModels;
import io.nop.graphql.core.schema.TypeRegistry;

public interface IGraphQLBizInitializer {
    void initialize(IGraphQLBizObject bizObj,
                    IBizObjectQueryProcessorBuilder queryProcessorBuilder,
                    TypeRegistry typeRegistry, GraphQLBizModels bizModels);
}
