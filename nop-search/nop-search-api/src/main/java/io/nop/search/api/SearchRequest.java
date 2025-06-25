/**
 * Copyright (c) 2017-2024 Nop Platform. All rights reserved.
 * Author: canonical_entropy@163.com
 * Blog:   https://www.zhihu.com/people/canonical-entropy
 * Gitee:  https://gitee.com/canonical-entropy/nop-entropy
 * Github: https://github.com/entropy-cloud/nop-entropy
 */
package io.nop.search.api;

import io.nop.api.core.annotations.data.DataBean;
import io.nop.api.core.beans.TreeBean;

import java.util.Set;

@DataBean
public class SearchRequest {
    private String topic;

    private String query;

    private boolean matchAllTags;
    private Set<String> tags;

    private int limit;

    private double similarityThreshold;

    private TreeBean filter;

    public boolean isMatchAllTags() {
        return matchAllTags;
    }

    public void setMatchAllTags(boolean matchAllTags) {
        this.matchAllTags = matchAllTags;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public double getSimilarityThreshold() {
        return similarityThreshold;
    }

    public void setSimilarityThreshold(double similarityThreshold) {
        this.similarityThreshold = similarityThreshold;
    }

    public TreeBean getFilter() {
        return filter;
    }

    public void setFilter(TreeBean filter) {
        this.filter = filter;
    }
}
