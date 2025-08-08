package io.nop.task.model._gen;

import io.nop.commons.collections.KeyedList; //NOPMD NOSONAR - suppressed UnusedImports - Used for List Prop
import io.nop.core.lang.json.IJsonHandler;
import io.nop.task.model.TaskInputModel;
import io.nop.commons.util.ClassHelper;



// tell cpd to start ignoring code - CPD-OFF
/**
 * generate from /nop/schema/task/task.xdef <p>
 * 
 */
@SuppressWarnings({"PMD.UselessOverridingMethod","PMD.UnusedLocalVariable",
    "PMD.UnnecessaryFullyQualifiedName","PMD.EmptyControlStatement","java:S116","java:S101","java:S1128","java:S1161"})
public abstract class _TaskInputModel extends io.nop.core.resource.component.AbstractComponentModel {
    
    /**
     *  
     * xml name: defaultValue
     * 
     */
    private java.lang.Object _defaultValue ;
    
    /**
     *  
     * xml name: description
     * 
     */
    private java.lang.String _description ;
    
    /**
     *  
     * xml name: displayName
     * 
     */
    private java.lang.String _displayName ;
    
    /**
     *  
     * xml name: dump
     * 
     */
    private boolean _dump  = false;
    
    /**
     *  
     * xml name: fromTaskScope
     * 
     */
    private boolean _fromTaskScope  = false;
    
    /**
     *  
     * xml name: mandatory
     * 
     */
    private boolean _mandatory  = false;
    
    /**
     *  
     * xml name: name
     * 
     */
    private java.lang.String _name ;
    
    /**
     *  
     * xml name: optional
     * 
     */
    private boolean _optional  = false;
    
    /**
     *  是否持久化保存
     * xml name: persist
     * 标记为persist的变量会自动保存，支持中断后恢复执行
     */
    private boolean _persist  = true;
    
    /**
     *  
     * xml name: role
     * 
     */
    private java.lang.String _role ;
    
    /**
     *  
     * xml name: schema
     * schema包含如下几种情况：1. 简单数据类型 2. Map（命名属性集合） 3. List（顺序结构，重复结构） 4. Union（switch选择结构）
     * Map对应props配置,  List对应item配置, Union对应oneOf配置
     */
    private io.nop.xlang.xmeta.ISchema _schema ;
    
    /**
     *  
     * xml name: source
     * 
     */
    private io.nop.core.lang.eval.IEvalAction _source ;
    
    /**
     *  
     * xml name: type
     * 
     */
    private io.nop.core.type.IGenericType _type ;
    
    /**
     *  
     * xml name: value
     * 
     */
    private io.nop.core.lang.eval.IEvalAction _value ;
    
    /**
     * 
     * xml name: defaultValue
     *  
     */
    
    public java.lang.Object getDefaultValue(){
      return _defaultValue;
    }

    
    public void setDefaultValue(java.lang.Object value){
        checkAllowChange();
        
        this._defaultValue = value;
           
    }

    
    /**
     * 
     * xml name: description
     *  
     */
    
    public java.lang.String getDescription(){
      return _description;
    }

    
    public void setDescription(java.lang.String value){
        checkAllowChange();
        
        this._description = value;
           
    }

    
    /**
     * 
     * xml name: displayName
     *  
     */
    
    public java.lang.String getDisplayName(){
      return _displayName;
    }

    
    public void setDisplayName(java.lang.String value){
        checkAllowChange();
        
        this._displayName = value;
           
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
     * xml name: fromTaskScope
     *  
     */
    
    public boolean isFromTaskScope(){
      return _fromTaskScope;
    }

    
    public void setFromTaskScope(boolean value){
        checkAllowChange();
        
        this._fromTaskScope = value;
           
    }

    
    /**
     * 
     * xml name: mandatory
     *  
     */
    
    public boolean isMandatory(){
      return _mandatory;
    }

    
    public void setMandatory(boolean value){
        checkAllowChange();
        
        this._mandatory = value;
           
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
     * xml name: optional
     *  
     */
    
    public boolean isOptional(){
      return _optional;
    }

    
    public void setOptional(boolean value){
        checkAllowChange();
        
        this._optional = value;
           
    }

    
    /**
     * 是否持久化保存
     * xml name: persist
     *  标记为persist的变量会自动保存，支持中断后恢复执行
     */
    
    public boolean isPersist(){
      return _persist;
    }

    
    public void setPersist(boolean value){
        checkAllowChange();
        
        this._persist = value;
           
    }

    
    /**
     * 
     * xml name: role
     *  
     */
    
    public java.lang.String getRole(){
      return _role;
    }

    
    public void setRole(java.lang.String value){
        checkAllowChange();
        
        this._role = value;
           
    }

    
    /**
     * 
     * xml name: schema
     *  schema包含如下几种情况：1. 简单数据类型 2. Map（命名属性集合） 3. List（顺序结构，重复结构） 4. Union（switch选择结构）
     * Map对应props配置,  List对应item配置, Union对应oneOf配置
     */
    
    public io.nop.xlang.xmeta.ISchema getSchema(){
      return _schema;
    }

    
    public void setSchema(io.nop.xlang.xmeta.ISchema value){
        checkAllowChange();
        
        this._schema = value;
           
    }

    
    /**
     * 
     * xml name: source
     *  
     */
    
    public io.nop.core.lang.eval.IEvalAction getSource(){
      return _source;
    }

    
    public void setSource(io.nop.core.lang.eval.IEvalAction value){
        checkAllowChange();
        
        this._source = value;
           
    }

    
    /**
     * 
     * xml name: type
     *  
     */
    
    public io.nop.core.type.IGenericType getType(){
      return _type;
    }

    
    public void setType(io.nop.core.type.IGenericType value){
        checkAllowChange();
        
        this._type = value;
           
    }

    
    /**
     * 
     * xml name: value
     *  
     */
    
    public io.nop.core.lang.eval.IEvalAction getValue(){
      return _value;
    }

    
    public void setValue(io.nop.core.lang.eval.IEvalAction value){
        checkAllowChange();
        
        this._value = value;
           
    }

    

    @Override
    public void freeze(boolean cascade){
        if(frozen()) return;
        super.freeze(cascade);

        if(cascade){ //NOPMD - suppressed EmptyControlStatement - Auto Gen Code
        
           this._schema = io.nop.api.core.util.FreezeHelper.deepFreeze(this._schema);
            
        }
    }

    @Override
    protected void outputJson(IJsonHandler out){
        super.outputJson(out);
        
        out.putNotNull("defaultValue",this.getDefaultValue());
        out.putNotNull("description",this.getDescription());
        out.putNotNull("displayName",this.getDisplayName());
        out.putNotNull("dump",this.isDump());
        out.putNotNull("fromTaskScope",this.isFromTaskScope());
        out.putNotNull("mandatory",this.isMandatory());
        out.putNotNull("name",this.getName());
        out.putNotNull("optional",this.isOptional());
        out.putNotNull("persist",this.isPersist());
        out.putNotNull("role",this.getRole());
        out.putNotNull("schema",this.getSchema());
        out.putNotNull("source",this.getSource());
        out.putNotNull("type",this.getType());
        out.putNotNull("value",this.getValue());
    }

    public TaskInputModel cloneInstance(){
        TaskInputModel instance = newInstance();
        this.copyTo(instance);
        return instance;
    }

    protected void copyTo(TaskInputModel instance){
        super.copyTo(instance);
        
        instance.setDefaultValue(this.getDefaultValue());
        instance.setDescription(this.getDescription());
        instance.setDisplayName(this.getDisplayName());
        instance.setDump(this.isDump());
        instance.setFromTaskScope(this.isFromTaskScope());
        instance.setMandatory(this.isMandatory());
        instance.setName(this.getName());
        instance.setOptional(this.isOptional());
        instance.setPersist(this.isPersist());
        instance.setRole(this.getRole());
        instance.setSchema(this.getSchema());
        instance.setSource(this.getSource());
        instance.setType(this.getType());
        instance.setValue(this.getValue());
    }

    protected TaskInputModel newInstance(){
        return (TaskInputModel) ClassHelper.newInstance(getClass());
    }
}
 // resume CPD analysis - CPD-ON
