/**
 * Copyright (c) 2017-2024 Nop Platform. All rights reserved.
 * Author: canonical_entropy@163.com
 * Blog:   https://www.zhihu.com/people/canonical-entropy
 * Gitee:  https://gitee.com/canonical-entropy/nop-entropy
 * Github: https://github.com/entropy-cloud/nop-entropy
 */
package io.nop.rpc.model;

import io.nop.commons.util.StringHelper;
import io.nop.rpc.model._gen._ApiServiceModel;

public class ApiServiceModel extends _ApiServiceModel implements IWithOptions {
    public ApiServiceModel() {

    }

    public String getSimpleClassName() {
        return StringHelper.simpleClassName(getClassName());
    }

    public String getPackageName() {
        return StringHelper.packageName(getClassName());
    }

    public String getPackagePath() {
        return StringHelper.classNameToPath(getPackageName());
    }
}
