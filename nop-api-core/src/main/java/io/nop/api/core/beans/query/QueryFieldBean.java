/**
 * Copyright (c) 2017-2024 Nop Platform. All rights reserved.
 * Author: canonical_entropy@163.com
 * Blog:   https://www.zhihu.com/people/canonical-entropy
 * Gitee:  https://gitee.com/canonical-entropy/nop-entropy
 * Github: https://github.com/entropy-cloud/nop-entropy
 */
package io.nop.api.core.beans.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.nop.api.core.annotations.data.DataBean;
import io.nop.api.core.annotations.graphql.GraphQLObject;
import io.nop.api.core.annotations.meta.PropMeta;
import io.nop.api.core.beans.TreeBean;
import io.nop.api.core.util.Guard;
import io.nop.api.core.util.ICloneable;

@DataBean
@GraphQLObject
public class QueryFieldBean implements ICloneable {
    private String owner;
    private String name;

    /**
     * 选择出数据后对应的别名
     */
    private String alias;
    private String aggFunc;

    private TreeBean formula;

    private boolean internal;

    public static QueryFieldBean forField(String name) {
        Guard.notEmpty(name, "name");
        QueryFieldBean field = new QueryFieldBean();
        field.setName(name);
        return field;
    }

    public static QueryFieldBean mainField(String name) {
        return forField(name);
    }

    public static QueryFieldBean subField(String owner, String name) {
        Guard.notEmpty(owner, "owner");
        Guard.notEmpty(name, "name");
        QueryFieldBean field = new QueryFieldBean();
        field.setOwner(owner);
        field.setName(name);
        return field;
    }

    public QueryFieldBean aggFunc(String aggFunc) {
        setAggFunc(aggFunc);
        return this;
    }

    public QueryFieldBean sum() {
        setAggFunc("sum");
        return this;
    }

    public QueryFieldBean count() {
        setAggFunc("count");
        return this;
    }

    public QueryFieldBean min() {
        setAggFunc("min");
        return this;
    }

    public QueryFieldBean max() {
        setAggFunc("max");
        return this;
    }

    public QueryFieldBean alias(String alias) {
        setAlias(alias);
        return this;
    }

    public QueryFieldBean cloneInstance() {
        QueryFieldBean field = new QueryFieldBean();
        field.setOwner(owner);
        field.setName(name);
        field.setAlias(alias);
        field.setAggFunc(aggFunc);
        return field;
    }

    public QueryFieldBean cloneExceptOwner() {
        QueryFieldBean field = cloneInstance();
        field.setOwner(null);
        return field;
    }

    @JsonIgnore
    public String getAliasOrName() {
        if (alias != null)
            return alias;
        return name;
    }

    @JsonIgnore
    public String getLabel() {
        if (alias != null)
            return alias;
        if (aggFunc != null) {
            return aggFunc + '(' + name + ')';
        }
        return name;
    }

    @PropMeta(propId = 1)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @PropMeta(propId = 2)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String getAggFunc() {
        return aggFunc;
    }

    public void setAggFunc(String aggFunc) {
        this.aggFunc = aggFunc;
    }

    @PropMeta(propId = 3)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @PropMeta(propId = 4)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @PropMeta(propId = 5)
    public boolean isInternal() {
        return internal;
    }

    public void setInternal(boolean internal) {
        this.internal = internal;
    }

    @PropMeta(propId = 6)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public TreeBean getFormula() {
        return formula;
    }

    public void setFormula(TreeBean formula) {
        this.formula = formula;
    }
}