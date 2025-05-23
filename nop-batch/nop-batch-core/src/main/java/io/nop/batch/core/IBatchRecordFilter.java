/**
 * Copyright (c) 2017-2024 Nop Platform. All rights reserved.
 * Author: canonical_entropy@163.com
 * Blog:   https://www.zhihu.com/people/canonical-entropy
 * Gitee:  https://gitee.com/canonical-entropy/nop-entropy
 * Github: https://github.com/entropy-cloud/nop-entropy
 */
package io.nop.batch.core;

import io.nop.core.context.IEvalContext;

import java.util.List;
import java.util.stream.Collectors;

public interface IBatchRecordFilter<R, C extends IEvalContext> {
    boolean accept(R record, C context);

    default List<R> filter(List<R> records, C context) {
        return records.stream().filter(record -> {
            return accept(record, context);
        }).collect(Collectors.toList());
    }
}
