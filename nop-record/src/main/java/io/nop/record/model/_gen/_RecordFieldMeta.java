package io.nop.record.model._gen;

import io.nop.commons.collections.KeyedList; //NOPMD NOSONAR - suppressed UnusedImports - Used for List Prop
import io.nop.core.lang.json.IJsonHandler;
import io.nop.record.model.RecordFieldMeta;
import io.nop.commons.util.ClassHelper;



// tell cpd to start ignoring code - CPD-OFF
/**
 * generate from /nop/schema/record/record-field.xdef <p>
 * 定长记录的定义
 */
@SuppressWarnings({"PMD.UselessOverridingMethod","PMD.UnusedLocalVariable",
    "PMD.UnnecessaryFullyQualifiedName","PMD.EmptyControlStatement","java:S116","java:S101","java:S1128","java:S1161"})
public abstract class _RecordFieldMeta extends io.nop.record.model.RecordSimpleFieldMeta {
    
    /**
     *  
     * xml name: afterRead
     * 在所有子字段都读取到之后执行
     */
    private io.nop.core.lang.eval.IEvalFunction _afterRead ;
    
    /**
     *  
     * xml name: afterWrite
     * 
     */
    private io.nop.core.lang.eval.IEvalFunction _afterWrite ;
    
    /**
     *  
     * xml name: fields
     * 
     */
    private KeyedList<io.nop.record.model.RecordFieldMeta> _fields = KeyedList.emptyList();
    
    /**
     *  
     * xml name: readRepeatExpr
     * 返回字段的循环次数
     */
    private io.nop.core.lang.eval.IEvalFunction _readRepeatExpr ;
    
    /**
     *  
     * xml name: readRepeatKind
     * 
     */
    private io.nop.record.model.FieldRepeatKind _readRepeatKind ;
    
    /**
     *  
     * xml name: readRepeatUntil
     * 返回字段循环的终止条件
     */
    private io.nop.core.lang.eval.IEvalFunction _readRepeatUntil ;
    
    /**
     *  
     * xml name: switch
     * 动态确定字段类型
     */
    private io.nop.record.model.RecordFieldSwitch _switch ;
    
    /**
     *  
     * xml name: tagIndex
     * 
     */
    private int _tagIndex  = 0;
    
    /**
     *  
     * xml name: tagsCodec
     * 类似ISO8583协议，支持先输出一个bitmap标记哪些字段需要写出，然后根据tagIndex过滤只写出部分字段
     */
    private java.lang.String _tagsCodec ;
    
    /**
     *  
     * xml name: template
     * 文本输出时使用template更加直观
     */
    private java.lang.String _template ;
    
    /**
     * 
     * xml name: afterRead
     *  在所有子字段都读取到之后执行
     */
    
    public io.nop.core.lang.eval.IEvalFunction getAfterRead(){
      return _afterRead;
    }

    
    public void setAfterRead(io.nop.core.lang.eval.IEvalFunction value){
        checkAllowChange();
        
        this._afterRead = value;
           
    }

    
    /**
     * 
     * xml name: afterWrite
     *  
     */
    
    public io.nop.core.lang.eval.IEvalFunction getAfterWrite(){
      return _afterWrite;
    }

    
    public void setAfterWrite(io.nop.core.lang.eval.IEvalFunction value){
        checkAllowChange();
        
        this._afterWrite = value;
           
    }

    
    /**
     * 
     * xml name: fields
     *  
     */
    
    public java.util.List<io.nop.record.model.RecordFieldMeta> getFields(){
      return _fields;
    }

    
    public void setFields(java.util.List<io.nop.record.model.RecordFieldMeta> value){
        checkAllowChange();
        
        this._fields = KeyedList.fromList(value, io.nop.record.model.RecordFieldMeta::getName);
           
    }

    
    public io.nop.record.model.RecordFieldMeta getField(String name){
        return this._fields.getByKey(name);
    }

    public boolean hasField(String name){
        return this._fields.containsKey(name);
    }

    public void addField(io.nop.record.model.RecordFieldMeta item) {
        checkAllowChange();
        java.util.List<io.nop.record.model.RecordFieldMeta> list = this.getFields();
        if (list == null || list.isEmpty()) {
            list = new KeyedList<>(io.nop.record.model.RecordFieldMeta::getName);
            setFields(list);
        }
        list.add(item);
    }
    
    public java.util.Set<String> keySet_fields(){
        return this._fields.keySet();
    }

    public boolean hasFields(){
        return !this._fields.isEmpty();
    }
    
    /**
     * 
     * xml name: readRepeatExpr
     *  返回字段的循环次数
     */
    
    public io.nop.core.lang.eval.IEvalFunction getReadRepeatExpr(){
      return _readRepeatExpr;
    }

    
    public void setReadRepeatExpr(io.nop.core.lang.eval.IEvalFunction value){
        checkAllowChange();
        
        this._readRepeatExpr = value;
           
    }

    
    /**
     * 
     * xml name: readRepeatKind
     *  
     */
    
    public io.nop.record.model.FieldRepeatKind getReadRepeatKind(){
      return _readRepeatKind;
    }

    
    public void setReadRepeatKind(io.nop.record.model.FieldRepeatKind value){
        checkAllowChange();
        
        this._readRepeatKind = value;
           
    }

    
    /**
     * 
     * xml name: readRepeatUntil
     *  返回字段循环的终止条件
     */
    
    public io.nop.core.lang.eval.IEvalFunction getReadRepeatUntil(){
      return _readRepeatUntil;
    }

    
    public void setReadRepeatUntil(io.nop.core.lang.eval.IEvalFunction value){
        checkAllowChange();
        
        this._readRepeatUntil = value;
           
    }

    
    /**
     * 
     * xml name: switch
     *  动态确定字段类型
     */
    
    public io.nop.record.model.RecordFieldSwitch getSwitch(){
      return _switch;
    }

    
    public void setSwitch(io.nop.record.model.RecordFieldSwitch value){
        checkAllowChange();
        
        this._switch = value;
           
    }

    
    /**
     * 
     * xml name: tagIndex
     *  
     */
    
    public int getTagIndex(){
      return _tagIndex;
    }

    
    public void setTagIndex(int value){
        checkAllowChange();
        
        this._tagIndex = value;
           
    }

    
    /**
     * 
     * xml name: tagsCodec
     *  类似ISO8583协议，支持先输出一个bitmap标记哪些字段需要写出，然后根据tagIndex过滤只写出部分字段
     */
    
    public java.lang.String getTagsCodec(){
      return _tagsCodec;
    }

    
    public void setTagsCodec(java.lang.String value){
        checkAllowChange();
        
        this._tagsCodec = value;
           
    }

    
    /**
     * 
     * xml name: template
     *  文本输出时使用template更加直观
     */
    
    public java.lang.String getTemplate(){
      return _template;
    }

    
    public void setTemplate(java.lang.String value){
        checkAllowChange();
        
        this._template = value;
           
    }

    

    @Override
    public void freeze(boolean cascade){
        if(frozen()) return;
        super.freeze(cascade);

        if(cascade){ //NOPMD - suppressed EmptyControlStatement - Auto Gen Code
        
           this._fields = io.nop.api.core.util.FreezeHelper.deepFreeze(this._fields);
            
           this._switch = io.nop.api.core.util.FreezeHelper.deepFreeze(this._switch);
            
        }
    }

    @Override
    protected void outputJson(IJsonHandler out){
        super.outputJson(out);
        
        out.putNotNull("afterRead",this.getAfterRead());
        out.putNotNull("afterWrite",this.getAfterWrite());
        out.putNotNull("fields",this.getFields());
        out.putNotNull("readRepeatExpr",this.getReadRepeatExpr());
        out.putNotNull("readRepeatKind",this.getReadRepeatKind());
        out.putNotNull("readRepeatUntil",this.getReadRepeatUntil());
        out.putNotNull("switch",this.getSwitch());
        out.putNotNull("tagIndex",this.getTagIndex());
        out.putNotNull("tagsCodec",this.getTagsCodec());
        out.putNotNull("template",this.getTemplate());
    }

    public RecordFieldMeta cloneInstance(){
        RecordFieldMeta instance = newInstance();
        this.copyTo(instance);
        return instance;
    }

    protected void copyTo(RecordFieldMeta instance){
        super.copyTo(instance);
        
        instance.setAfterRead(this.getAfterRead());
        instance.setAfterWrite(this.getAfterWrite());
        instance.setFields(this.getFields());
        instance.setReadRepeatExpr(this.getReadRepeatExpr());
        instance.setReadRepeatKind(this.getReadRepeatKind());
        instance.setReadRepeatUntil(this.getReadRepeatUntil());
        instance.setSwitch(this.getSwitch());
        instance.setTagIndex(this.getTagIndex());
        instance.setTagsCodec(this.getTagsCodec());
        instance.setTemplate(this.getTemplate());
    }

    protected RecordFieldMeta newInstance(){
        return (RecordFieldMeta) ClassHelper.newInstance(getClass());
    }
}
 // resume CPD analysis - CPD-ON
