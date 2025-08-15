package io.nop.excel.imp.model._gen;

import io.nop.commons.collections.KeyedList; //NOPMD NOSONAR - suppressed UnusedImports - Used for List Prop
import io.nop.core.lang.json.IJsonHandler;
import io.nop.excel.imp.model.ImportModel;
import io.nop.commons.util.ClassHelper;



// tell cpd to start ignoring code - CPD-OFF
/**
 * generate from /nop/schema/excel/imp.xdef <p>
 * 导入不涉及到展现控制，仅仅需要考虑后台处理逻辑，因此比导出设计要简单的多。导入策略与导出策略可以共享objMeta上的信息。
 */
@SuppressWarnings({"PMD.UselessOverridingMethod","PMD.UnusedLocalVariable",
    "PMD.UnnecessaryFullyQualifiedName","PMD.EmptyControlStatement","java:S116","java:S101","java:S1128","java:S1161"})
public abstract class _ImportModel extends io.nop.core.resource.component.AbstractComponentModel {
    
    /**
     *  
     * xml name: afterParse
     * 
     */
    private io.nop.core.lang.eval.IEvalAction _afterParse ;
    
    /**
     *  
     * xml name: beforeParse
     * 
     */
    private io.nop.core.lang.eval.IEvalAction _beforeParse ;
    
    /**
     *  
     * xml name: bizObjName
     * 
     */
    private java.lang.String _bizObjName ;
    
    /**
     *  
     * xml name: defaultStripText
     * 
     */
    private boolean _defaultStripText  = true;
    
    /**
     *  
     * xml name: dump
     * 
     */
    private boolean _dump  = false;
    
    /**
     *  
     * xml name: ignoreUnknownSheet
     * 
     */
    private boolean _ignoreUnknownSheet  = false;
    
    /**
     *  
     * xml name: normalizeFieldsExpr
     * 
     */
    private io.nop.core.lang.eval.IEvalAction _normalizeFieldsExpr ;
    
    /**
     *  
     * xml name: resultType
     * 
     */
    private io.nop.core.type.IGenericType _resultType ;
    
    /**
     *  
     * xml name: sheets
     * 
     */
    private KeyedList<io.nop.excel.imp.model.ImportSheetModel> _sheets = KeyedList.emptyList();
    
    /**
     *  
     * xml name: templatePath
     * 空的导入模板文件。导出数据时也会使用这个模板
     */
    private java.lang.String _templatePath ;
    
    /**
     *  
     * xml name: validator
     * 
     */
    private io.nop.core.lang.eval.IEvalAction _validator ;
    
    /**
     *  
     * xml name: xdef
     * 解析得到的模型对象所对应的xdef元模型定义，用于将模型对象序列化为XML格式时使用
     */
    private java.lang.String _xdef ;
    
    /**
     * 
     * xml name: afterParse
     *  
     */
    
    public io.nop.core.lang.eval.IEvalAction getAfterParse(){
      return _afterParse;
    }

    
    public void setAfterParse(io.nop.core.lang.eval.IEvalAction value){
        checkAllowChange();
        
        this._afterParse = value;
           
    }

    
    /**
     * 
     * xml name: beforeParse
     *  
     */
    
    public io.nop.core.lang.eval.IEvalAction getBeforeParse(){
      return _beforeParse;
    }

    
    public void setBeforeParse(io.nop.core.lang.eval.IEvalAction value){
        checkAllowChange();
        
        this._beforeParse = value;
           
    }

    
    /**
     * 
     * xml name: bizObjName
     *  
     */
    
    public java.lang.String getBizObjName(){
      return _bizObjName;
    }

    
    public void setBizObjName(java.lang.String value){
        checkAllowChange();
        
        this._bizObjName = value;
           
    }

    
    /**
     * 
     * xml name: defaultStripText
     *  
     */
    
    public boolean isDefaultStripText(){
      return _defaultStripText;
    }

    
    public void setDefaultStripText(boolean value){
        checkAllowChange();
        
        this._defaultStripText = value;
           
    }

    
    /**
     * 
     * xml name: dump
     *  
     */
    
    public boolean isDump(){
      return _dump;
    }

    
    public void setDump(boolean value){
        checkAllowChange();
        
        this._dump = value;
           
    }

    
    /**
     * 
     * xml name: ignoreUnknownSheet
     *  
     */
    
    public boolean isIgnoreUnknownSheet(){
      return _ignoreUnknownSheet;
    }

    
    public void setIgnoreUnknownSheet(boolean value){
        checkAllowChange();
        
        this._ignoreUnknownSheet = value;
           
    }

    
    /**
     * 
     * xml name: normalizeFieldsExpr
     *  
     */
    
    public io.nop.core.lang.eval.IEvalAction getNormalizeFieldsExpr(){
      return _normalizeFieldsExpr;
    }

    
    public void setNormalizeFieldsExpr(io.nop.core.lang.eval.IEvalAction value){
        checkAllowChange();
        
        this._normalizeFieldsExpr = value;
           
    }

    
    /**
     * 
     * xml name: resultType
     *  
     */
    
    public io.nop.core.type.IGenericType getResultType(){
      return _resultType;
    }

    
    public void setResultType(io.nop.core.type.IGenericType value){
        checkAllowChange();
        
        this._resultType = value;
           
    }

    
    /**
     * 
     * xml name: sheets
     *  
     */
    
    public java.util.List<io.nop.excel.imp.model.ImportSheetModel> getSheets(){
      return _sheets;
    }

    
    public void setSheets(java.util.List<io.nop.excel.imp.model.ImportSheetModel> value){
        checkAllowChange();
        
        this._sheets = KeyedList.fromList(value, io.nop.excel.imp.model.ImportSheetModel::getName);
           
    }

    
    public io.nop.excel.imp.model.ImportSheetModel getSheet(String name){
        return this._sheets.getByKey(name);
    }

    public boolean hasSheet(String name){
        return this._sheets.containsKey(name);
    }

    public void addSheet(io.nop.excel.imp.model.ImportSheetModel item) {
        checkAllowChange();
        java.util.List<io.nop.excel.imp.model.ImportSheetModel> list = this.getSheets();
        if (list == null || list.isEmpty()) {
            list = new KeyedList<>(io.nop.excel.imp.model.ImportSheetModel::getName);
            setSheets(list);
        }
        list.add(item);
    }
    
    public java.util.Set<String> keySet_sheets(){
        return this._sheets.keySet();
    }

    public boolean hasSheets(){
        return !this._sheets.isEmpty();
    }
    
    /**
     * 
     * xml name: templatePath
     *  空的导入模板文件。导出数据时也会使用这个模板
     */
    
    public java.lang.String getTemplatePath(){
      return _templatePath;
    }

    
    public void setTemplatePath(java.lang.String value){
        checkAllowChange();
        
        this._templatePath = value;
           
    }

    
    /**
     * 
     * xml name: validator
     *  
     */
    
    public io.nop.core.lang.eval.IEvalAction getValidator(){
      return _validator;
    }

    
    public void setValidator(io.nop.core.lang.eval.IEvalAction value){
        checkAllowChange();
        
        this._validator = value;
           
    }

    
    /**
     * 
     * xml name: xdef
     *  解析得到的模型对象所对应的xdef元模型定义，用于将模型对象序列化为XML格式时使用
     */
    
    public java.lang.String getXdef(){
      return _xdef;
    }

    
    public void setXdef(java.lang.String value){
        checkAllowChange();
        
        this._xdef = value;
           
    }

    

    @Override
    public void freeze(boolean cascade){
        if(frozen()) return;
        super.freeze(cascade);

        if(cascade){ //NOPMD - suppressed EmptyControlStatement - Auto Gen Code
        
           this._sheets = io.nop.api.core.util.FreezeHelper.deepFreeze(this._sheets);
            
        }
    }

    @Override
    protected void outputJson(IJsonHandler out){
        super.outputJson(out);
        
        out.putNotNull("afterParse",this.getAfterParse());
        out.putNotNull("beforeParse",this.getBeforeParse());
        out.putNotNull("bizObjName",this.getBizObjName());
        out.putNotNull("defaultStripText",this.isDefaultStripText());
        out.putNotNull("dump",this.isDump());
        out.putNotNull("ignoreUnknownSheet",this.isIgnoreUnknownSheet());
        out.putNotNull("normalizeFieldsExpr",this.getNormalizeFieldsExpr());
        out.putNotNull("resultType",this.getResultType());
        out.putNotNull("sheets",this.getSheets());
        out.putNotNull("templatePath",this.getTemplatePath());
        out.putNotNull("validator",this.getValidator());
        out.putNotNull("xdef",this.getXdef());
    }

    public ImportModel cloneInstance(){
        ImportModel instance = newInstance();
        this.copyTo(instance);
        return instance;
    }

    protected void copyTo(ImportModel instance){
        super.copyTo(instance);
        
        instance.setAfterParse(this.getAfterParse());
        instance.setBeforeParse(this.getBeforeParse());
        instance.setBizObjName(this.getBizObjName());
        instance.setDefaultStripText(this.isDefaultStripText());
        instance.setDump(this.isDump());
        instance.setIgnoreUnknownSheet(this.isIgnoreUnknownSheet());
        instance.setNormalizeFieldsExpr(this.getNormalizeFieldsExpr());
        instance.setResultType(this.getResultType());
        instance.setSheets(this.getSheets());
        instance.setTemplatePath(this.getTemplatePath());
        instance.setValidator(this.getValidator());
        instance.setXdef(this.getXdef());
    }

    protected ImportModel newInstance(){
        return (ImportModel) ClassHelper.newInstance(getClass());
    }
}
 // resume CPD analysis - CPD-ON
