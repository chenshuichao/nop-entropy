/**
 * Copyright (c) 2017-2024 Nop Platform. All rights reserved.
 * Author: canonical_entropy@163.com
 * Blog:   https://www.zhihu.com/people/canonical-entropy
 * Gitee:  https://gitee.com/canonical-entropy/nop-entropy
 * Github: https://github.com/entropy-cloud/nop-entropy
 */
package io.nop.report.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.nop.api.core.annotations.lang.EvalMethod;
import io.nop.api.core.util.Guard;
import io.nop.core.lang.eval.IEvalScope;
import io.nop.core.lang.utils.Underscore;
import io.nop.core.model.table.ICellView;
import io.nop.excel.model.ExcelImage;
import io.nop.excel.model.IExcelCell;
import io.nop.excel.model.XptCellModel;
import io.nop.excel.model.constants.XptExpandType;
import io.nop.excel.util.UnitsHelper;
import io.nop.report.core.coordinate.CellCoordinate;
import io.nop.report.core.coordinate.CellLayerCoordinate;
import io.nop.report.core.dataset.KeyedReportDataSet;
import io.nop.report.core.dataset.ReportDataSet;
import io.nop.report.core.engine.IXptRuntime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 报表展开过程中需要频繁修改行和列，所以采用单向列表形式来维护
 */
public class ExpandedCell implements IExcelCell {
    private XptCellModel model;

    private String id;
    private String styleId;
    private Object value;
    private String comment;
    private int mergeDown;
    private int mergeAcross;
    private int colOffset;
    private int rowOffset;

    private Object formattedValue;
    private String linkUrl;

    private String formula;

    // 对于合并单元格，realCell设置为左上角的单元格
    // private int rowOffset;
    //  private int colOffset;
    private ExpandedCell realCell;

    private ExpandedCell right;
    private ExpandedCell down;

    private ExpandedCell rowParent;
    private ExpandedCell colParent;

    // 递归包含所有子单元格
    private Map<String, List<ExpandedCell>> rowDescendants = null;
    private Map<String, List<ExpandedCell>> colDescendants = null;

    private Object expandValue;
    private int expandIndex = -1; // 在展开列表中的下标

    private ExpandedRow row;
    private ExpandedCol col;

    private ExcelImage image;

    private boolean removed;

    /**
     * valueExpr已经执行完毕，value值可用
     */
    private boolean evaluated;

    /**
     * 缓存与单元格有关的动态计算的值
     */
    private Map<String, Object> extValues;

    public String toString() {
        return "ExpandedCell[name=" + getName() + ",expandIndex=" + getExpandIndex() + ",text=" + getText()
                + ",coord=" + getColCoordinates() + "]";
    }

    public double getCellWidth(double defaultColWidth) {
        double totalW = 0;
        ExpandedCell c = this;
        for (int i = 0; i <= mergeAcross; i++) {
            double w = c.getCol().getWidth(defaultColWidth);
            totalW += w;
            c = c.getRight();
            if (c == null)
                break;
        }
        return totalW;
    }

    public double getCellWidthTwips(double defaultColWidth) {
        return UnitsHelper.pointsToTwips(getCellWidth(defaultColWidth));
    }


    public CellLayerCoordinate getLayerCoordinate() {
        CellLayerCoordinate coord = new CellLayerCoordinate();
        coord.setCellName(getName());
        if (getRowParent() != null) {
            List<CellCoordinate> rowCoordinates = getRowCoordinates();
            coord.setRowCoordinates(rowCoordinates);
        }
        if (getColParent() != null) {
            List<CellCoordinate> colCoordinates = getColCoordinates();
            coord.setColCoordinates(colCoordinates);
        }
        return coord;
    }

    private List<CellCoordinate> getRowCoordinates() {
        List<ExpandedCell> parents = new ArrayList<>();
        ExpandedCell parent = getRowParent();
        while (parent != null) {
            parents.add(parent);
            parent = parent.getRowParent();
        }

        return toCoordinates(parents);
    }

    private List<CellCoordinate> getColCoordinates() {
        List<ExpandedCell> parents = new ArrayList<>();
        ExpandedCell parent = getColParent();
        while (parent != null) {
            parents.add(parent);
            parent = parent.getColParent();
        }

        return toCoordinates(parents);
    }

    private List<CellCoordinate> toCoordinates(List<ExpandedCell> parents) {
        List<CellCoordinate> ret = new ArrayList<>(parents.size());
        for (int i = parents.size() - 1; i >= 0; i--) {
            ExpandedCell parentCell = parents.get(i);
            CellCoordinate ord = new CellCoordinate();
            ord.setCellName(parentCell.getName());
            ord.setPosition(parentCell.getExpandIndex() + 1);
            ret.add(ord);
        }
        return ret;
    }

    public Object getComputed(String key, Function<ExpandedCell, Object> fn) {
        if (extValues == null) {
            extValues = new HashMap<>();
        }
        return extValues.computeIfAbsent(key, k -> fn.apply(this));
    }

    public Object getExtValue(String key) {
        if (extValues == null)
            return null;
        return extValues.get(key);
    }

    public void setExtValue(String name, Object value) {
        if (extValues == null)
            extValues = new HashMap<>();
        extValues.put(name, value);
    }

    public ExcelImage getImage() {
        return image;
    }

    public void setExcelImage(ExcelImage image) {
        this.image = image;
    }

    public ExcelImage makeImage() {
        if (image == null)
            image = new ExcelImage();
        return image;
    }

    public double getWidth() {
        ExpandedSheet sheet = getTable().getSheet();
        int colIndex = getColIndex();
        return sheet.getWidth(colIndex, colIndex + getMergeAcross());
    }

    public double getHeight() {
        ExpandedSheet sheet = getTable().getSheet();
        int rowIndex = getRowIndex();
        return sheet.getHeight(rowIndex, rowIndex + getMergeDown());
    }

    @Override
    public boolean isExportFormattedValue() {
        if (model == null)
            return false;
        return Boolean.TRUE.equals(model.getExportFormattedValue());
    }

    @Override
    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    @Override
    public int getColOffset() {
        return colOffset;
    }

    public void setColOffset(int colOffset) {
        this.colOffset = colOffset;
    }

    @Override
    public int getRowOffset() {
        return rowOffset;
    }

    public void setRowOffset(int rowOffset) {
        this.rowOffset = rowOffset;
    }

    /**
     * 标记colSpan和rowSpan范围内的所有单元格的realCell为当前单元格。
     */
    public void markProxy() {
        Guard.checkState(!isProxyCell());

        ExpandedCell cell = this;
        for (int i = 0; i <= mergeDown; i++) {
            ExpandedCell colCell = cell;
            for (int j = 0; j <= mergeAcross; j++) {
                colCell.setRealCell(this);
                colCell.setRowOffset(i);
                colCell.setColOffset(j);

                colCell = colCell.getRight();
            }
            cell = cell.getDown();
        }
    }

    @JsonIgnore
    public Number getNumberValue() {
        Object value = getValue();
        if (value instanceof Number)
            return (Number) value;
        return null;
    }

    @JsonIgnore
    public ReportDataSet getDs() {
        if (expandValue instanceof ReportDataSet)
            return (ReportDataSet) expandValue;
        return null;
    }

    public String getDsName() {
        ReportDataSet ds = getDs();
        if (ds != null)
            return ds.getDsName();
        return model.getDs();
    }

    public int getRowIndex() {
        return row.getRowIndex();
    }

    public int getColIndex() {
        return col.getColIndex();
    }

    public void markEvaluated() {
        setEvaluated(true);
        setExpandValue(null);
        // 通过这个标志来标记已经展开完毕
        setExpandIndex(0);
        if (!isStaticCell()) {
            setValue(null);
        }
    }

    public boolean isStaticCell() {
        XptCellModel model = getModel();
        if (model == null)
            return true;
        if (model.getExpandType() != null || model.getExpandExpr() != null)
            return false;

        if (model.getValueExpr() != null)
            return false;

        if (model.getField() != null)
            return false;
        return true;
    }

    public Object getFormattedValue() {
        if (formattedValue == null)
            return value;
        return formattedValue;
    }

    public void setFormattedValue(Object formattedValue) {
        this.formattedValue = formattedValue;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public boolean isEvaluated() {
        return evaluated;
    }

    public void setEvaluated(boolean evaluated) {
        this.evaluated = evaluated;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setModel(XptCellModel model) {
        this.model = model;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public ICellView cloneInstance() {
        return this;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        // 被删除的单元格，它的值被强制设置为null
        if (removed) {
            setEvaluated(true);
            setValue(null);
        }
        this.removed = removed;
    }

    public String getName() {
        return model == null ? null : model.getName();
    }

    public boolean isExpanded() {
        return expandIndex >= 0 || model == null || model.getExpandType() == null;
    }

    public XptExpandType getExpandType() {
        return model == null ? null : model.getExpandType();
    }

    @Override
    public Object getExportValue() {
        if (isExportFormattedValue())
            return getFormattedValue();
        return getValue();
    }

    @JsonIgnore
    public int getRowParentExpandIndex() {
        if (rowParent == null)
            return -1;
        return rowParent.getExpandIndex();
    }

    @JsonIgnore
    public int getColParentExpandIndex() {
        if (colParent == null)
            return -1;
        return colParent.getExpandIndex();
    }

    public boolean isProxyCell() {
        return realCell != null && realCell != this;
    }

    @JsonIgnore
    public ExpandedTable getTable() {
        return getRow().getTable();
    }

    @JsonIgnore
    public ExpandedSheet getSheet() {
        return getTable().getSheet();
    }

    @JsonIgnore
    public ExpandedCell getRowRoot() {
        if (rowParent == null)
            return this;
        return rowParent.getRowRoot();
    }

    @JsonIgnore
    public ExpandedCell getColRoot() {
        if (colParent == null)
            return this;
        return colParent.getColRoot();
    }

    @JsonIgnore
    public XptCellModel getModel() {
        return model;
    }

    @JsonIgnore
    public boolean isExpandable() {
        XptCellModel model = getModel();
        if (model == null)
            return false;
        return model.getExpandType() != null;
    }

    @JsonIgnore
    public boolean isRowParentExpandable() {
        ExpandedCell rowParent = getRowParent();
        if (rowParent == null)
            return false;
        if (rowParent.isExpandable())
            return true;
        return rowParent.isRowParentExpandable();
    }

    @JsonIgnore
    public boolean isColParentExpandable() {
        ExpandedCell colParent = getColParent();
        if (colParent == null)
            return false;
        if (colParent.isExpandable())
            return true;
        return colParent.isColParentExpandable();
    }

    public String getStyleId() {
        return styleId;
    }

    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }

    public Object getV() {
        return getValue();
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public ExpandedCell getRight() {
        return right;
    }

    public void setRight(ExpandedCell right) {
        this.right = right;
    }

    public int getMergeDown() {
        return mergeDown;
    }

    public void setMergeDown(int mergeDown) {
        this.mergeDown = mergeDown;
    }

    public int getMergeAcross() {
        return mergeAcross;
    }

    public void setMergeAcross(int mergeAcross) {
        this.mergeAcross = mergeAcross;
    }

    @JsonIgnore
    public ExpandedCell getRealCell() {
        if (realCell == null)
            return this;
        return realCell;
    }

    public void setRealCell(ExpandedCell realCell) {
        this.realCell = realCell;
    }

    @JsonIgnore
    public ExpandedCell getDown() {
        return down;
    }

    public void setDown(ExpandedCell down) {
        this.down = down;
    }

    @JsonIgnore
    public ExpandedCell getRowParent() {
        return rowParent;
    }

    public void setRowParent(ExpandedCell rowParent) {
        this.rowParent = rowParent;
    }

    public ExpandedCell getRowClosest(String cellName) {
        if (cellName.equals(this.getName()))
            return this;

        if (this.rowParent == null)
            return null;
        if (cellName.equals(this.rowParent.getName()))
            return this.rowParent;
        return this.rowParent.getRowClosest(cellName);
    }

    public ExpandedCell getColClosest(String cellName) {
        if (cellName.equals(this.getName()))
            return this;

        if (this.colParent == null)
            return null;
        if (cellName.equals(this.colParent.getName()))
            return colParent;
        return this.colParent.getColClosest(cellName);
    }

    @JsonIgnore
    public List<ExpandedCell> getRowChildren() {
        if (rowDescendants == null || rowDescendants.isEmpty())
            return Collections.emptyList();

        List<ExpandedCell> children = new ArrayList<>();
        for (String cellName : model.getRowChildCells().keySet()) {
            List<ExpandedCell> list = rowDescendants.get(cellName);
            if (list != null) {
                for (ExpandedCell cell : list) {
                    if (cell.getRowParent() == this) {
                        children.add(cell);
                    }
                }
            }
        }
        return children;
    }

    @JsonIgnore
    public List<ExpandedCell> getColChildren() {
        if (colDescendants == null || colDescendants.isEmpty())
            return Collections.emptyList();

        List<ExpandedCell> children = new ArrayList<>();
        for (String cellName : model.getColChildCells().keySet()) {
            List<ExpandedCell> list = colDescendants.get(cellName);
            if (list != null) {
                for (ExpandedCell cell : list) {
                    if (cell.getColParent() == this) {
                        children.add(cell);
                    }
                }
            }
        }
        return children;
    }

    @JsonIgnore
    public ExpandedCell getColParent() {
        return colParent;
    }

    public ExpandedCell getExpandableColParent() {
        if (colParent == null)
            return null;
        if (colParent.getModel() == null)
            return null;
        if (colParent.getModel().getExpandType() == null)
            return colParent.getExpandableColParent();
        return colParent;
    }

    public ExpandedCell getExpandableRowParent() {
        if (rowParent == null)
            return null;
        if (rowParent.getModel() == null)
            return null;
        if (rowParent.getModel().getExpandType() == null)
            return rowParent.getExpandableColParent();
        return rowParent;
    }

    public void setColParent(ExpandedCell colParent) {
        this.colParent = colParent;
    }

    public Object getExpandValue() {
        return expandValue;
    }

    public void setExpandValue(Object expandValue) {
        this.expandValue = expandValue;
    }

    public Object getExpandKey() {
        if (expandValue instanceof KeyedReportDataSet)
            return ((KeyedReportDataSet) expandValue).getKey();
        return null;
    }

    @JsonIgnore
    public ExpandedCell getRr() {
        return getRowRoot();
    }

    @JsonIgnore
    public ExpandedCell getCr() {
        return getColRoot();
    }

    @JsonIgnore
    public ExpandedCell getRp() {
        return getRowParent();
    }

    @JsonIgnore
    public ExpandedCell getCp() {
        return getColParent();
    }

    @JsonIgnore
    public Object getEv() {
        return getExpandValue();
    }

    @JsonIgnore
    public int getEi() {
        return getExpandIndex();
    }

    public Object getExpandField(String name) {
        if (expandValue != null) {
            if (expandValue instanceof ReportDataSet) {
                return ((ReportDataSet) expandValue).field(name);
            }
            return Underscore.getFieldValue(expandValue, name);
        }
        return Underscore.getFieldValue(value, name);
    }

    public int getExpandIndex() {
        return expandIndex;
    }

    public void setExpandIndex(int expandIndex) {
        this.expandIndex = expandIndex;
    }

    @JsonIgnore
    public ExpandedRow getRow() {
        return row;
    }

    public void setRow(ExpandedRow row) {
        this.row = row;
    }

    @JsonIgnore
    public ExpandedCol getCol() {
        return col;
    }

    public void setCol(ExpandedCol col) {
        this.col = col;
    }

    @JsonIgnore
    public Map<String, List<ExpandedCell>> getRowDescendants() {
        return rowDescendants;
    }

    public ExpandedCellSet childSet(String cellName, IXptRuntime xptRt) {
        if (rowDescendants != null) {
            List<ExpandedCell> cells = rowDescendants.get(cellName);
            if (cells != null && !cells.isEmpty())
                return new ExpandedCellSet(null, cellName, cells).evaluateAll(xptRt);
        }
        if (colDescendants != null) {
            List<ExpandedCell> cells = colDescendants.get(cellName);
            if (cells != null && !cells.isEmpty())
                return new ExpandedCellSet(null, cellName, cells).evaluateAll(xptRt);
        }
        return null;
    }

    public ExpandedCell childCell(String cellName, IXptRuntime xptRt) {
        if (rowDescendants != null) {
            List<ExpandedCell> cells = rowDescendants.get(cellName);
            if (cells != null && !cells.isEmpty()) {
                ExpandedCell cell = cells.get(0);
                xptRt.evaluateCell(cell);
                return cell;
            }
        }
        if (colDescendants != null) {
            List<ExpandedCell> cells = colDescendants.get(cellName);
            if (cells != null && !cells.isEmpty()) {
                ExpandedCell cell = cells.get(0);
                xptRt.evaluateCell(cell);
                return null;
            }
        }
        return null;
    }

    public Object childValue(String cellName, IXptRuntime xptRt) {
        ExpandedCell cell = childCell(cellName, xptRt);
        return cell == null ? null : cell.getValue();
    }

    @EvalMethod
    public Object cv(IEvalScope scope, String cellName) {
        IXptRuntime xptRt = IXptRuntime.fromScope(scope);
        return childValue(cellName, xptRt);
    }

    public boolean hasRowDescendant() {
        return rowDescendants != null && !rowDescendants.isEmpty();
    }

    public boolean hasColDescendant() {
        return colDescendants != null && !colDescendants.isEmpty();
    }

    public void setRowDescendants(Map<String, List<ExpandedCell>> rowDescendants) {
        this.rowDescendants = rowDescendants;
    }

    public void addRowChild(ExpandedCell cell) {
        if (rowDescendants == null)
            rowDescendants = new HashMap<>();

        addToList(rowDescendants, cell);

        ExpandedCell p = rowParent;
        while (p != null) {
            if (p.rowDescendants == null)
                p.rowDescendants = new HashMap<>();
            addToList(p.rowDescendants, cell);
            p = p.getRowParent();
        }
    }

    public void addColChild(ExpandedCell cell) {
        if (colDescendants == null)
            colDescendants = new HashMap<>();

        addToList(colDescendants, cell);

        ExpandedCell p = colParent;
        while (p != null) {
            if (p.colDescendants == null)
                p.colDescendants = new HashMap<>();
            addToList(p.colDescendants, cell);
            p = p.getColParent();
        }
    }

    void addToList(Map<String, List<ExpandedCell>> map, ExpandedCell cell) {
        List<ExpandedCell> list = map.get(cell.getName());
        if (list == null) {
            list = new ArrayList<>();
            map.put(cell.getName(), list);
        }
        list.add(cell);
    }

    void removeFromList(Map<String, List<ExpandedCell>> map, ExpandedCell cell) {
        List<ExpandedCell> list = map.get(cell.getName());
        if (list != null)
            list.remove(cell);
    }

    public void removeRowChild(ExpandedCell cell) {
        if (rowDescendants != null) {
            removeFromList(rowDescendants, cell);
            ExpandedCell p = rowParent;
            while (p != null) {
                removeFromList(p.rowDescendants, cell);
                p = p.getRowParent();
            }
        }
    }

    public boolean isRowDescendantOf(ExpandedCell cell) {
        ExpandedCell c = rowParent;
        do {
            if (c == cell)
                return true;
            if (c == null)
                return false;
            c = c.getRowParent();
        } while (true);
    }

    public boolean isColDescendantOf(ExpandedCell cell) {
        ExpandedCell c = colParent;
        do {
            if (c == cell)
                return true;
            if (c == null)
                return false;
            c = c.getColParent();
        } while (true);
    }

    public void setColDescendants(Map<String, List<ExpandedCell>> colDescendants) {
        this.colDescendants = colDescendants;
    }

    public void addRowChildren(Map<String, List<ExpandedCell>> rowChildren) {
        this.rowDescendants = merge(this.rowDescendants, rowChildren);
    }

    static Map<String, List<ExpandedCell>> merge(Map<String, List<ExpandedCell>> mapA, Map<String, List<ExpandedCell>> mapB) {
        if (mapA == null)
            return mapB;

        for (Map.Entry<String, List<ExpandedCell>> entry : mapB.entrySet()) {
            List<ExpandedCell> listB = entry.getValue();
            List<ExpandedCell> listA = mapA.get(entry.getKey());
            if (listA == null) {
                listA = new ArrayList<>(listB);
                mapA.put(entry.getKey(), listA);
            } else {
                listA.addAll(listB);
            }
        }
        return mapA;
    }

    @JsonIgnore
    public Map<String, List<ExpandedCell>> getColDescendants() {
        return colDescendants;
    }

    public void addColChildren(Map<String, List<ExpandedCell>> colChildren) {
        this.colDescendants = merge(this.colDescendants, colChildren);
    }

    public ExpandedCellSet rowChildSet(String cellName) {
        String expr = cellName + "[" + getName() + "]";

        List<ExpandedCell> cells = null;
        if (rowDescendants != null) {
            cells = rowDescendants.get(cellName);
        }

        if (cells == null || cells.isEmpty())
            return new ExpandedCellSet(null, expr, Collections.emptyList());
        return new ExpandedCellSet(null, expr, cells);
    }

    public ExpandedCellSet colChildSet(String cellName) {
        String expr = cellName + "[" + getName() + "]";

        List<ExpandedCell> cells = null;
        if (colDescendants != null) {
            cells = colDescendants.get(cellName);
        }

        if (cells == null || cells.isEmpty())
            return new ExpandedCellSet(null, expr, Collections.emptyList());
        return new ExpandedCellSet(null, expr, cells);
    }

    public void changeColSpan(int delta) {
        this.mergeAcross += delta;
    }

    public void changeRowSpan(int delta) {
        this.mergeDown += delta;
    }
}