package io.nop.excel.model._gen;

import io.nop.commons.collections.KeyedList; //NOPMD NOSONAR - suppressed UnusedImports - Used for List Prop
import io.nop.core.lang.json.IJsonHandler;
import io.nop.excel.model.ExcelSheet;
import io.nop.commons.util.ClassHelper;



// tell cpd to start ignoring code - CPD-OFF
/**
 * generate from /nop/schema/excel/workbook.xdef <p>
 * 
 */
@SuppressWarnings({"PMD.UselessOverridingMethod","PMD.UnusedLocalVariable",
    "PMD.UnnecessaryFullyQualifiedName","PMD.EmptyControlStatement","java:S116","java:S101","java:S1128","java:S1161"})
public abstract class _ExcelSheet extends io.nop.core.resource.component.AbstractComponentModel {
    
    /**
     *  
     * xml name: annotations
     * 
     */
    private KeyedList<io.nop.excel.model.ExcelAnnotation> _annotations = KeyedList.emptyList();
    
    /**
     *  
     * xml name: conditionalStyles
     * 
     */
    private KeyedList<io.nop.excel.model.ExcelConditionalStyle> _conditionalStyles = KeyedList.emptyList();
    
    /**
     *  
     * xml name: dataValidations
     * 
     */
    private KeyedList<io.nop.excel.model.ExcelDataValidation> _dataValidations = KeyedList.emptyList();
    
    /**
     *  
     * xml name: defaultColumnWidth
     * 
     */
    private java.lang.Double _defaultColumnWidth ;
    
    /**
     *  
     * xml name: defaultRowHeight
     * 
     */
    private java.lang.Double _defaultRowHeight ;
    
    /**
     *  
     * xml name: images
     * 
     */
    private KeyedList<io.nop.excel.model.ExcelImage> _images = KeyedList.emptyList();
    
    /**
     *  
     * xml name: model
     * 
     */
    private io.nop.excel.model.XptSheetModel _model ;
    
    /**
     *  
     * xml name: name
     * 
     */
    private java.lang.String _name ;
    
    /**
     *  
     * xml name: pageBreaks
     * 
     */
    private io.nop.excel.model.ExcelPageBreaks _pageBreaks ;
    
    /**
     *  
     * xml name: pageMargins
     * 
     */
    private io.nop.excel.model.ExcelPageMargins _pageMargins ;
    
    /**
     *  
     * xml name: pageSetup
     * 
     */
    private io.nop.excel.model.ExcelPageSetup _pageSetup ;
    
    /**
     *  
     * xml name: sheetOptions
     * 
     */
    private io.nop.excel.model.ExcelSheetOptions _sheetOptions ;
    
    /**
     *  
     * xml name: sheetProtection
     * Excel 工作表保护配置
     * 启用后，结合单元格的 locked 属性实现编辑控制
     */
    private io.nop.excel.model.ExcelSheetProtection _sheetProtection ;
    
    /**
     *  
     * xml name: table
     * 
     */
    private io.nop.excel.model.ExcelTable _table ;
    
    /**
     * 
     * xml name: annotations
     *  
     */
    
    public java.util.List<io.nop.excel.model.ExcelAnnotation> getAnnotations(){
      return _annotations;
    }

    
    public void setAnnotations(java.util.List<io.nop.excel.model.ExcelAnnotation> value){
        checkAllowChange();
        
        this._annotations = KeyedList.fromList(value, io.nop.excel.model.ExcelAnnotation::getRange);
           
    }

    
    public io.nop.excel.model.ExcelAnnotation getAnnotation(String name){
        return this._annotations.getByKey(name);
    }

    public boolean hasAnnotation(String name){
        return this._annotations.containsKey(name);
    }

    public void addAnnotation(io.nop.excel.model.ExcelAnnotation item) {
        checkAllowChange();
        java.util.List<io.nop.excel.model.ExcelAnnotation> list = this.getAnnotations();
        if (list == null || list.isEmpty()) {
            list = new KeyedList<>(io.nop.excel.model.ExcelAnnotation::getRange);
            setAnnotations(list);
        }
        list.add(item);
    }
    
    public java.util.Set<String> keySet_annotations(){
        return this._annotations.keySet();
    }

    public boolean hasAnnotations(){
        return !this._annotations.isEmpty();
    }
    
    /**
     * 
     * xml name: conditionalStyles
     *  
     */
    
    public java.util.List<io.nop.excel.model.ExcelConditionalStyle> getConditionalStyles(){
      return _conditionalStyles;
    }

    
    public void setConditionalStyles(java.util.List<io.nop.excel.model.ExcelConditionalStyle> value){
        checkAllowChange();
        
        this._conditionalStyles = KeyedList.fromList(value, io.nop.excel.model.ExcelConditionalStyle::getRange);
           
    }

    
    public io.nop.excel.model.ExcelConditionalStyle getConditionalStyle(String name){
        return this._conditionalStyles.getByKey(name);
    }

    public boolean hasConditionalStyle(String name){
        return this._conditionalStyles.containsKey(name);
    }

    public void addConditionalStyle(io.nop.excel.model.ExcelConditionalStyle item) {
        checkAllowChange();
        java.util.List<io.nop.excel.model.ExcelConditionalStyle> list = this.getConditionalStyles();
        if (list == null || list.isEmpty()) {
            list = new KeyedList<>(io.nop.excel.model.ExcelConditionalStyle::getRange);
            setConditionalStyles(list);
        }
        list.add(item);
    }
    
    public java.util.Set<String> keySet_conditionalStyles(){
        return this._conditionalStyles.keySet();
    }

    public boolean hasConditionalStyles(){
        return !this._conditionalStyles.isEmpty();
    }
    
    /**
     * 
     * xml name: dataValidations
     *  
     */
    
    public java.util.List<io.nop.excel.model.ExcelDataValidation> getDataValidations(){
      return _dataValidations;
    }

    
    public void setDataValidations(java.util.List<io.nop.excel.model.ExcelDataValidation> value){
        checkAllowChange();
        
        this._dataValidations = KeyedList.fromList(value, io.nop.excel.model.ExcelDataValidation::getId);
           
    }

    
    public io.nop.excel.model.ExcelDataValidation getDataValidation(String name){
        return this._dataValidations.getByKey(name);
    }

    public boolean hasDataValidation(String name){
        return this._dataValidations.containsKey(name);
    }

    public void addDataValidation(io.nop.excel.model.ExcelDataValidation item) {
        checkAllowChange();
        java.util.List<io.nop.excel.model.ExcelDataValidation> list = this.getDataValidations();
        if (list == null || list.isEmpty()) {
            list = new KeyedList<>(io.nop.excel.model.ExcelDataValidation::getId);
            setDataValidations(list);
        }
        list.add(item);
    }
    
    public java.util.Set<String> keySet_dataValidations(){
        return this._dataValidations.keySet();
    }

    public boolean hasDataValidations(){
        return !this._dataValidations.isEmpty();
    }
    
    /**
     * 
     * xml name: defaultColumnWidth
     *  
     */
    
    public java.lang.Double getDefaultColumnWidth(){
      return _defaultColumnWidth;
    }

    
    public void setDefaultColumnWidth(java.lang.Double value){
        checkAllowChange();
        
        this._defaultColumnWidth = value;
           
    }

    
    /**
     * 
     * xml name: defaultRowHeight
     *  
     */
    
    public java.lang.Double getDefaultRowHeight(){
      return _defaultRowHeight;
    }

    
    public void setDefaultRowHeight(java.lang.Double value){
        checkAllowChange();
        
        this._defaultRowHeight = value;
           
    }

    
    /**
     * 
     * xml name: images
     *  
     */
    
    public java.util.List<io.nop.excel.model.ExcelImage> getImages(){
      return _images;
    }

    
    public void setImages(java.util.List<io.nop.excel.model.ExcelImage> value){
        checkAllowChange();
        
        this._images = KeyedList.fromList(value, io.nop.excel.model.ExcelImage::getName);
           
    }

    
    public io.nop.excel.model.ExcelImage getImage(String name){
        return this._images.getByKey(name);
    }

    public boolean hasImage(String name){
        return this._images.containsKey(name);
    }

    public void addImage(io.nop.excel.model.ExcelImage item) {
        checkAllowChange();
        java.util.List<io.nop.excel.model.ExcelImage> list = this.getImages();
        if (list == null || list.isEmpty()) {
            list = new KeyedList<>(io.nop.excel.model.ExcelImage::getName);
            setImages(list);
        }
        list.add(item);
    }
    
    public java.util.Set<String> keySet_images(){
        return this._images.keySet();
    }

    public boolean hasImages(){
        return !this._images.isEmpty();
    }
    
    /**
     * 
     * xml name: model
     *  
     */
    
    public io.nop.excel.model.XptSheetModel getModel(){
      return _model;
    }

    
    public void setModel(io.nop.excel.model.XptSheetModel value){
        checkAllowChange();
        
        this._model = value;
           
    }

    
    /**
     * 
     * xml name: name
     *  
     */
    
    public java.lang.String getName(){
      return _name;
    }

    
    public void setName(java.lang.String value){
        checkAllowChange();
        
        this._name = value;
           
    }

    
    /**
     * 
     * xml name: pageBreaks
     *  
     */
    
    public io.nop.excel.model.ExcelPageBreaks getPageBreaks(){
      return _pageBreaks;
    }

    
    public void setPageBreaks(io.nop.excel.model.ExcelPageBreaks value){
        checkAllowChange();
        
        this._pageBreaks = value;
           
    }

    
    /**
     * 
     * xml name: pageMargins
     *  
     */
    
    public io.nop.excel.model.ExcelPageMargins getPageMargins(){
      return _pageMargins;
    }

    
    public void setPageMargins(io.nop.excel.model.ExcelPageMargins value){
        checkAllowChange();
        
        this._pageMargins = value;
           
    }

    
    /**
     * 
     * xml name: pageSetup
     *  
     */
    
    public io.nop.excel.model.ExcelPageSetup getPageSetup(){
      return _pageSetup;
    }

    
    public void setPageSetup(io.nop.excel.model.ExcelPageSetup value){
        checkAllowChange();
        
        this._pageSetup = value;
           
    }

    
    /**
     * 
     * xml name: sheetOptions
     *  
     */
    
    public io.nop.excel.model.ExcelSheetOptions getSheetOptions(){
      return _sheetOptions;
    }

    
    public void setSheetOptions(io.nop.excel.model.ExcelSheetOptions value){
        checkAllowChange();
        
        this._sheetOptions = value;
           
    }

    
    /**
     * 
     * xml name: sheetProtection
     *  Excel 工作表保护配置
     * 启用后，结合单元格的 locked 属性实现编辑控制
     */
    
    public io.nop.excel.model.ExcelSheetProtection getSheetProtection(){
      return _sheetProtection;
    }

    
    public void setSheetProtection(io.nop.excel.model.ExcelSheetProtection value){
        checkAllowChange();
        
        this._sheetProtection = value;
           
    }

    
    /**
     * 
     * xml name: table
     *  
     */
    
    public io.nop.excel.model.ExcelTable getTable(){
      return _table;
    }

    
    public void setTable(io.nop.excel.model.ExcelTable value){
        checkAllowChange();
        
        this._table = value;
           
    }

    

    @Override
    public void freeze(boolean cascade){
        if(frozen()) return;
        super.freeze(cascade);

        if(cascade){ //NOPMD - suppressed EmptyControlStatement - Auto Gen Code
        
           this._annotations = io.nop.api.core.util.FreezeHelper.deepFreeze(this._annotations);
            
           this._conditionalStyles = io.nop.api.core.util.FreezeHelper.deepFreeze(this._conditionalStyles);
            
           this._dataValidations = io.nop.api.core.util.FreezeHelper.deepFreeze(this._dataValidations);
            
           this._images = io.nop.api.core.util.FreezeHelper.deepFreeze(this._images);
            
           this._model = io.nop.api.core.util.FreezeHelper.deepFreeze(this._model);
            
           this._pageBreaks = io.nop.api.core.util.FreezeHelper.deepFreeze(this._pageBreaks);
            
           this._pageMargins = io.nop.api.core.util.FreezeHelper.deepFreeze(this._pageMargins);
            
           this._pageSetup = io.nop.api.core.util.FreezeHelper.deepFreeze(this._pageSetup);
            
           this._sheetOptions = io.nop.api.core.util.FreezeHelper.deepFreeze(this._sheetOptions);
            
           this._sheetProtection = io.nop.api.core.util.FreezeHelper.deepFreeze(this._sheetProtection);
            
           this._table = io.nop.api.core.util.FreezeHelper.deepFreeze(this._table);
            
        }
    }

    @Override
    protected void outputJson(IJsonHandler out){
        super.outputJson(out);
        
        out.putNotNull("annotations",this.getAnnotations());
        out.putNotNull("conditionalStyles",this.getConditionalStyles());
        out.putNotNull("dataValidations",this.getDataValidations());
        out.putNotNull("defaultColumnWidth",this.getDefaultColumnWidth());
        out.putNotNull("defaultRowHeight",this.getDefaultRowHeight());
        out.putNotNull("images",this.getImages());
        out.putNotNull("model",this.getModel());
        out.putNotNull("name",this.getName());
        out.putNotNull("pageBreaks",this.getPageBreaks());
        out.putNotNull("pageMargins",this.getPageMargins());
        out.putNotNull("pageSetup",this.getPageSetup());
        out.putNotNull("sheetOptions",this.getSheetOptions());
        out.putNotNull("sheetProtection",this.getSheetProtection());
        out.putNotNull("table",this.getTable());
    }

    public ExcelSheet cloneInstance(){
        ExcelSheet instance = newInstance();
        this.copyTo(instance);
        return instance;
    }

    protected void copyTo(ExcelSheet instance){
        super.copyTo(instance);
        
        instance.setAnnotations(this.getAnnotations());
        instance.setConditionalStyles(this.getConditionalStyles());
        instance.setDataValidations(this.getDataValidations());
        instance.setDefaultColumnWidth(this.getDefaultColumnWidth());
        instance.setDefaultRowHeight(this.getDefaultRowHeight());
        instance.setImages(this.getImages());
        instance.setModel(this.getModel());
        instance.setName(this.getName());
        instance.setPageBreaks(this.getPageBreaks());
        instance.setPageMargins(this.getPageMargins());
        instance.setPageSetup(this.getPageSetup());
        instance.setSheetOptions(this.getSheetOptions());
        instance.setSheetProtection(this.getSheetProtection());
        instance.setTable(this.getTable());
    }

    protected ExcelSheet newInstance(){
        return (ExcelSheet) ClassHelper.newInstance(getClass());
    }
}
 // resume CPD analysis - CPD-ON
