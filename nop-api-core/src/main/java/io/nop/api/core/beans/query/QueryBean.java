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
import io.nop.api.core.ApiConstants;
import io.nop.api.core.annotations.data.DataBean;
import io.nop.api.core.annotations.graphql.GraphQLObject;
import io.nop.api.core.beans.FilterBeans;
import io.nop.api.core.beans.ITreeBean;
import io.nop.api.core.beans.TreeBean;
import io.nop.api.core.util.ApiStringHelper;
import io.nop.api.core.util.Guard;
import io.nop.api.core.util.ICloneable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.nop.api.core.ApiConstants.JOIN_TYPE_INNER_JOIN;
import static io.nop.api.core.ApiConstants.JOIN_TYPE_LEFT_JOIN;

@DataBean
@GraphQLObject
public class QueryBean implements Serializable, ICloneable {
    private static final long serialVersionUID = 6756041836462853393L;

    private String name;
    private long offset;
    private int limit;

    private String cursor;
    private boolean findPrev;
    private boolean distinct;

    private List<QueryFieldBean> fields;
    private List<QueryAggregateFieldBean> aggregates;

    private String sourceName;

    private List<String> dimFields;

    private List<QuerySourceBean> joins;

    private List<String> leftJoinProps;

    private TreeBean filter;

    private List<OrderFieldBean> orderBy;

    private List<GroupFieldBean> groupBy;

    private Integer timeout;

    private boolean disableLogicalDelete;

    public QueryBean() {
    }

    public QueryBean(String sourceName) {
        Guard.notEmpty(sourceName, "sourceName");
        setSourceName(sourceName);
    }

    @Override
    public QueryBean cloneInstance() {
        QueryBean query = new QueryBean();
        query.setName(name);
        query.setOffset(offset);
        query.setLimit(limit);
        query.setCursor(cursor);
        query.setFindPrev(findPrev);
        query.setDistinct(distinct);
        if (fields != null) {
            query.setFields(fields.stream().map(QueryFieldBean::cloneInstance).collect(Collectors.toList()));
        }
        if (aggregates != null) {
            query.setAggregates(aggregates.stream().map(QueryAggregateFieldBean::cloneInstance).collect(Collectors.toList()));
        }
        query.setSourceName(sourceName);
        if (dimFields != null)
            query.setDimFields(new ArrayList<>(dimFields));

        if (joins != null) {
            query.setJoins(joins.stream().map(QuerySourceBean::cloneInstance).collect(Collectors.toList()));
        }

        if (leftJoinProps != null)
            query.setLeftJoinProps(new ArrayList<>(leftJoinProps));

        if (filter != null)
            query.setFilter(filter.cloneInstance());

        if (orderBy != null) {
            query.setOrderBy(orderBy.stream().map(OrderFieldBean::cloneInstance).collect(Collectors.toList()));
        }

        if (groupBy != null)
            query.setGroupBy(groupBy.stream().map(GroupFieldBean::cloneInstance).collect(Collectors.toList()));

        query.setTimeout(timeout);
        query.setDisableLogicalDelete(disableLogicalDelete);
        return query;
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public QueryBean distinct() {
        this.setDistinct(true);
        return this;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public boolean isDisableLogicalDelete() {
        return disableLogicalDelete;
    }

    public void setDisableLogicalDelete(boolean disableLogicalDelete) {
        this.disableLogicalDelete = disableLogicalDelete;
    }

    public List<QueryFieldBean> getFields() {
        return fields;
    }

    public void setFields(List<QueryFieldBean> fields) {
        this.fields = fields;
    }

    @JsonIgnore
    public List<String> getFieldNames() {
        if (fields == null)
            return null;
        return fields.stream().map(QueryFieldBean::getName).collect(Collectors.toList());
    }

    public void setFieldNames(List<String> fieldNames) {
        if (fieldNames == null || fieldNames.isEmpty()) {
            this.fields = null;
        } else {
            for (String fieldName : fieldNames) {
                addField(QueryFieldBean.forField(fieldName));
            }
        }
    }

    public QueryBean addField(QueryFieldBean field) {
        if (fields == null) {
            fields = new ArrayList<>();
        }
        fields.add(field);
        return this;
    }

    public QueryBean addFields(Collection<QueryFieldBean> fields) {
        if (this.fields == null)
            this.fields = new ArrayList<>();
        this.fields.addAll(fields);
        return this;
    }

    public QueryBean fields(QueryFieldBean field, QueryFieldBean... fields) {
        addField(field);

        for (QueryFieldBean f : fields) {
            addField(f);
        }
        return this;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public QueryBean offset(long offset) {
        setOffset(offset);
        return this;
    }

    public QueryBean limit(int limit) {
        setLimit(limit);
        return this;
    }

    public TreeBean getFilter() {
        return filter;
    }

    public void setFilter(TreeBean filter) {
        this.filter = filter;
    }

    public QueryBean addFilter(ITreeBean filter) {
        if (filter == null)
            return this;

        TreeBean tree = FilterBeans.normalizeFilterBean(filter);
        if (tree == null)
            return null;

        if (this.filter == null) {
            this.filter = tree;
        } else {
            this.filter = FilterBeans.and(this.filter, tree);
        }
        return this;
    }

    public QueryBean addFilters(List<TreeBean> filters) {
        if (filters == null || filters.isEmpty())
            return this;

        if (this.filter == null) {
            this.filter = FilterBeans.and(filters);
        } else {
            this.filter = FilterBeans.and(this.filter, FilterBeans.and(filters));
        }
        return this;
    }

    public QueryBean addFilterCondition(String propName, String op, Object value) {
        return addFilter(FilterBeans.compareOp(op, propName, value));
    }

    public TreeBean getPropFilter(String propName) {
        if (filter == null)
            return null;

        if (Objects.equals(propName, filter.getAttr("name")))
            return filter;

        return filter.childWithAttr("name", propName);
    }

    public Object getPropFilterValue(String propName) {
        TreeBean bean = getPropFilter(propName);
        return bean == null ? null : bean.getAttr("value");
    }

    public boolean transformFilter(Function<TreeBean, ?> fn) {
        if (filter == null)
            return false;

        TreeBean node = new TreeBean();
        node.addChild(filter);
        boolean b = node.transformChild(null, fn, true);
        if (node.getChildCount() == 1) {
            filter = node.getChildren().get(0);
        } else {
            filter = node;
        }
        return b;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public QueryBean timeout(Integer timeout) {
        setTimeout(timeout);
        return this;
    }

    public List<OrderFieldBean> getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(List<OrderFieldBean> orderBy) {
        this.orderBy = orderBy;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public List<String> getDimFields() {
        return dimFields;
    }

    public void setDimFields(List<String> dimFields) {
        this.dimFields = dimFields;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public List<QuerySourceBean> getJoins() {
        return joins;
    }

    public void setJoins(List<QuerySourceBean> joins) {
        this.joins = joins;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public List<String> getLeftJoinProps() {
        return leftJoinProps;
    }

    public void setLeftJoinProps(List<String> leftJoinProps) {
        this.leftJoinProps = leftJoinProps;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public List<QueryAggregateFieldBean> getAggregates() {
        return aggregates;
    }

    public void setAggregates(List<QueryAggregateFieldBean> aggregates) {
        this.aggregates = aggregates;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public List<GroupFieldBean> getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(List<GroupFieldBean> groupBy) {
        this.groupBy = groupBy;
    }

    public boolean hasGroupBy() {
        return groupBy != null && !groupBy.isEmpty();
    }

    public boolean hasOrderBy() {
        return orderBy != null && !orderBy.isEmpty();
    }

    public GroupFieldBean getGroupField(String name) {
        if (groupBy != null) {
            for (GroupFieldBean field : groupBy) {
                if (name.equals(field.getName()))
                    return field;
            }
        }
        return null;
    }

    public boolean hasGroupField(String name) {
        return getGroupField(name) != null;
    }

    public QueryBean addGroupField(String name) {
        if (!hasGroupField(name)) {
            if (groupBy == null)
                groupBy = new ArrayList<>();
            groupBy.add(GroupFieldBean.forField(name));
        }
        return this;
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public boolean isFindPrev() {
        return findPrev;
    }

    public void setFindPrev(boolean findPrev) {
        this.findPrev = findPrev;
    }

    public OrderFieldBean getOrderField(String name) {
        if (orderBy == null)
            return null;
        for (OrderFieldBean field : orderBy) {
            if (field.getName().equals(name))
                return field;
        }
        return null;
    }

    public boolean hasOrderField(String name) {
        return getOrderField(name) != null;
    }

    public QueryBean addOrderField(String name, boolean desc) {
        if (!hasOrderField(name)) {
            if (orderBy == null)
                orderBy = new ArrayList<>();
            orderBy.add(OrderFieldBean.forField(name, desc));
        }
        return this;
    }

    public QueryBean addOrderField(OrderFieldBean field) {
        if (!hasOrderField(field.getName())) {
            if (orderBy == null)
                orderBy = new ArrayList<>();
            orderBy.add(field);
        }
        return this;
    }

    public QueryBean addOrderBy(List<OrderFieldBean> orderBy) {
        if (orderBy == null)
            return this;
        for (OrderFieldBean orderField : orderBy) {
            if (!hasOrderField(orderField.getName())) {
                if (this.orderBy == null)
                    this.orderBy = new ArrayList<>();
                this.orderBy.add(orderField);
            }
        }
        return this;
    }

    public QueryBean addOrderByNode(ITreeBean orderBy) {
        if (orderBy != null) {
            if (orderBy.getChildCount() > 0 || orderBy.getTagName().equals(ApiConstants.DUMMY_TAG_NAME)) {
                List<? extends ITreeBean> children = orderBy.getChildren();
                if (children != null) {
                    for (ITreeBean child : children) {
                        addOrderField(OrderFieldBean.fromTreeBean(child));
                    }
                }
            } else {
                addOrderField(OrderFieldBean.fromTreeBean(orderBy));
            }
        }
        return this;
    }

    public QuerySourceBean getJoinByAlias(String alias) {
        if (joins == null)
            return null;
        for (QuerySourceBean join : joins) {
            if (join.getAlias().equals(alias))
                return join;
        }
        return null;
    }

    public QueryBean leftJoin(String sourceName, String alias, String leftJoinFields, String rightJoinFields) {
        return addJoin(JOIN_TYPE_LEFT_JOIN, sourceName, alias, leftJoinFields, rightJoinFields);
    }

    public QueryBean rightJoin(String sourceName, String alias, String leftJoinFields, String rightJoinFields) {
        return addJoin(JOIN_TYPE_LEFT_JOIN, sourceName, alias, leftJoinFields, rightJoinFields);
    }

    public QueryBean innerJoin(String sourceName, String alias, String leftJoinFields, String rightJoinFields) {
        return addJoin(JOIN_TYPE_INNER_JOIN, sourceName, alias, leftJoinFields, rightJoinFields);
    }

    public QueryBean addJoin(String joinType, String sourceName, String alias, String leftJoinFields, String rightJoinFields) {
        if (ApiStringHelper.isEmpty(alias))
            throw new IllegalArgumentException("alias is empty");

        if (ApiStringHelper.isEmpty(sourceName))
            throw new IllegalArgumentException("sourceName is empty");

        if (leftJoinFields == null || leftJoinFields.isEmpty())
            throw new IllegalArgumentException("leftJoinFields is empty");

        if (rightJoinFields == null || rightJoinFields.isEmpty())
            throw new IllegalArgumentException("rightJoinFields is empty");

        if (joins == null)
            joins = new ArrayList<>();

        List<String> leftProps = ApiStringHelper.split(leftJoinFields, ',');
        List<String> rightProps = ApiStringHelper.split(rightJoinFields, ',');
        if (leftProps.size() != rightProps.size())
            throw new IllegalArgumentException("leftJoinFields and rightJoinFields must have same number of properties");

        QuerySourceBean join = getJoinByAlias(alias);
        if (join == null) {
            join = new QuerySourceBean();
            joins.add(join);
        }
        join.setJoinType(joinType);
        join.setSourceName(sourceName);
        join.setAlias(alias);
        if (Objects.equals(dimFields, leftJoinFields)) {
            join.setDimFields(rightProps);
        } else {
            List<QueryJoinConditionBean> joins = new ArrayList<>(leftProps.size());
            for (int i = 0; i < leftProps.size(); i++) {
                QueryJoinConditionBean joinBean = new QueryJoinConditionBean();
                joinBean.setLeftField(leftProps.get(i));
                joinBean.setRightField(rightProps.get(i));
                joins.add(joinBean);
            }
            join.setConditions(joins);
        }
        return this;
    }
}