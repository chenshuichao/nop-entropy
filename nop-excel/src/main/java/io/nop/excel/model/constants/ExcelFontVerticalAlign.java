/**
 * Copyright (c) 2017-2024 Nop Platform. All rights reserved.
 * Author: canonical_entropy@163.com
 * Blog:   https://www.zhihu.com/people/canonical-entropy
 * Gitee:  https://gitee.com/canonical-entropy/nop-entropy
 * Github: https://github.com/entropy-cloud/nop-entropy
 */
package io.nop.excel.model.constants;

import io.nop.api.core.annotations.core.StaticFactoryMethod;
import io.nop.commons.util.StringHelper;

public enum ExcelFontVerticalAlign {
    superscript((short) 1), subscript((short) 2);

    private short code;

    ExcelFontVerticalAlign(short code) {
        this.code = code;
    }

    public short getCode() {
        return code;
    }

    public static ExcelFontVerticalAlign fromCode(int code) {
        if (code == subscript.code)
            return subscript;
        if (code == superscript.code)
            return superscript;
        return null;
    }

    @StaticFactoryMethod
    public static ExcelFontVerticalAlign fromText(String text) {
        if (StringHelper.isEmpty(text) || "none".equals(text))
            return null;

        if (subscript.name().equals(text) || "2".equals(text)) {
            return subscript;
        } else if (superscript.name().equals(text) || "1".equals(text)) {
            return superscript;
        }
        return null;
    }
}