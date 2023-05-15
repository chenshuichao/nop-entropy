/**
 * Copyright (c) 2017-2023 Nop Platform. All rights reserved.
 * Author: canonical_entropy@163.com
 * Blog:   https://www.zhihu.com/people/canonical-entropy
 * Gitee:  https://gitee.com/canonical-entropy/nop-chaos
 * Github: https://github.com/entropy-cloud/nop-chaos
 */
package io.nop.orm.eql.ast;

import io.nop.core.lang.ast.ASTNode;
import io.nop.orm.eql.ast._gen._SqlColumnName;
import io.nop.orm.model.IEntityPropModel;

public class SqlColumnName extends _SqlColumnName {
    private SqlTableSource tableSource;
    private IEntityPropModel propModel;

    public SqlTableSource getTableSource() {
        return tableSource;
    }

    public void setTableSource(SqlTableSource tableSource) {
        this.tableSource = tableSource;
    }

    @Override
    protected void copyExtFieldsTo(ASTNode node) {
        super.copyExtFieldsTo(node);
        SqlColumnName col = (SqlColumnName) node;
        col.tableSource = tableSource;
        col.propModel = propModel;
    }

    @Override
    public String getResolvedOwner() {
        return tableSource.getAliasName();
    }

    public IEntityPropModel getPropModel() {
        return propModel;
    }

    public void setPropModel(IEntityPropModel propModel) {
        this.propModel = propModel;
    }
}