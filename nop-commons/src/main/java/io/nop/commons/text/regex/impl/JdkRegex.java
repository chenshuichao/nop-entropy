/**
 * Copyright (c) 2017-2024 Nop Platform. All rights reserved.
 * Author: canonical_entropy@163.com
 * Blog:   https://www.zhihu.com/people/canonical-entropy
 * Gitee:  https://gitee.com/canonical-entropy/nop-entropy
 * Github: https://github.com/entropy-cloud/nop-entropy
 */
package io.nop.commons.text.regex.impl;

import io.nop.commons.text.regex.IRegex;
import io.nop.commons.util.StringHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JdkRegex implements IRegex {
    private final Pattern pattern;

    public JdkRegex(Pattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean test(String text) {
        return pattern.matcher(text).matches();
    }

    @Override
    public List<String> exec(String text) {
        if (StringHelper.isEmpty(text))
            return null;

        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            int n = matcher.groupCount();
            List<String> ret = new ArrayList<>(n);
            ret.add(matcher.group());
            for (int i = 0; i < n; i++) {
                ret.add(matcher.group(i + 1));
            }
            return ret;
        } else {
            return null;
        }
    }

    @Override
    public List<String> match(String text) {
        if (StringHelper.isEmpty(text))
            return null;

        Matcher matcher = pattern.matcher(text);
        List<String> ret = new ArrayList<>();
        while (matcher.find()) {
            ret.add(matcher.group());
        }
        return ret.isEmpty() ? null : ret;
    }

    @Override
    public String replace(String text, String replacement, boolean all) {
        Matcher m = pattern.matcher(text);
        return all ? m.replaceAll(replacement) : m.replaceFirst(replacement);
    }
}
