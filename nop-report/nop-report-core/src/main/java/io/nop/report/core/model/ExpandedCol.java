/**
 * Copyright (c) 2017-2024 Nop Platform. All rights reserved.
 * Author: canonical_entropy@163.com
 * Blog:   https://www.zhihu.com/people/canonical-entropy
 * Gitee:  https://gitee.com/canonical-entropy/nop-entropy
 * Github: https://github.com/entropy-cloud/nop-entropy
 */
package io.nop.report.core.model;

import io.nop.core.model.table.ICellView;
import io.nop.excel.model.ExcelColumnConfig;
import io.nop.excel.model.IExcelCol;
import io.nop.excel.util.UnitsHelper;
import jakarta.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ExpandedCol implements IExcelCol {
    private ExcelColumnConfig colModel;
    private int assignedColIndex = -1;

    private String styleId;
    private ExpandedCell firstCell;

    private ExpandedTable table;
    private boolean removed;

    private boolean newlyCreated;

    private ExpandedCell generatorCell;

    public boolean isNewlyCreated() {
        return newlyCreated;
    }

    public void setNewlyCreated(boolean newlyCreated) {
        this.newlyCreated = newlyCreated;
    }

    public ExpandedCell getGeneratorCell() {
        return generatorCell;
    }

    public void setGeneratorCell(ExpandedCell generatorCell) {
        this.generatorCell = generatorCell;
    }


    public void forEachCell(Consumer<ExpandedCell> action) {
        ExpandedCell cell = firstCell;
        do {
            action.accept(cell);
            cell = cell.getDown();
            if (cell == null)
                break;
        } while (true);
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    @Override
    public Double getWidth() {
        return colModel == null ? null : colModel.getWidth();
    }

    public double getWidth(double defaultWidth) {
        Double d = getWidth();
        return d == null ? defaultWidth : d;
    }

    public int getWidthInTwips(double defaultHeight) {
        return UnitsHelper.pointsToTwips(getWidth(defaultHeight));
    }

    @Override
    public boolean isHidden() {
        return colModel != null && colModel.isHidden();
    }

    public ExcelColumnConfig getColModel() {
        return colModel;
    }

    public void setColModel(ExcelColumnConfig colModel) {
        this.colModel = colModel;
    }

    public String getStyleId() {
        return styleId;
    }

    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }

    public ExpandedCell getFirstCell() {
        return firstCell;
    }

    public void setFirstCell(ExpandedCell firstCell) {
        this.firstCell = firstCell;
    }


    public ExpandedCell getCell(int colIndex) {
        ExpandedCell cell = this.firstCell;
        for (int i = 0; i < colIndex; i++) {
            if (cell == null)
                return null;
            cell = cell.getDown();
        }
        return cell;
    }

    @Nonnull
    public List<? extends ICellView> getCells() {
        List<ExpandedCell> ret = new ArrayList<>();
        ExpandedCell cell = this.firstCell;
        while (cell != null) {
            ret.add(cell);
            cell = cell.getDown();
        }
        return ret;
    }

    public ExpandedTable getTable() {
        return table;
    }

    public void setTable(ExpandedTable table) {
        this.table = table;
    }

    public int getColIndex() {
        if (assignedColIndex >= 0)
            return assignedColIndex;
        return table.getCols().indexOf(this);
    }

    public void setAssignedColIndex(int assignedColIndex) {
        this.assignedColIndex = assignedColIndex;
    }

    public static void visitTwoCol(ExpandedCol col1, ExpandedCol col2,
                                   ITwoCellProcessor processor) {
        ExpandedCell c1 = col1.getFirstCell();
        ExpandedCell c2 = col2.getFirstCell();
        int index = 0;
        while (c1 != null) {
            processor.process(c1, c2, index);
            index++;
            c1 = c1.getDown();
            c2 = c2.getDown();
        }
    }

    public static void visitThreeCol(ExpandedCol col1, ExpandedCol col2,
                                     ExpandedCol col3,
                                     IThreeCellProcessor processor) {
        ExpandedCell c1 = col1.getFirstCell();
        ExpandedCell c2 = col2.getFirstCell();
        ExpandedCell c3 = col3.getFirstCell();
        int index = 0;
        while (c1 != null) {
            processor.process(c1, c2, c3, index);
            index++;
            c1 = c1.getDown();
            c2 = c2.getDown();
            c3 = c3.getDown();
        }
    }

    public void useNextColStyle() {
        ExpandedCell cell = this.firstCell;
        while (cell != null) {
            ExpandedCell right = cell.getRight();
            if (right != null) {
                cell.setStyleId(right.getStyleId());
            }
            cell = cell.getDown();
        }
    }
}