package io.nop.task.model._gen;

import io.nop.commons.collections.KeyedList; //NOPMD NOSONAR - suppressed UnusedImports - Used for List Prop
import io.nop.core.lang.json.IJsonHandler;
import io.nop.task.model.ForkTaskStepModel;
import io.nop.commons.util.ClassHelper;



// tell cpd to start ignoring code - CPD-OFF
/**
 * generate from /nop/schema/task/task.xdef <p>
 * 根据producer返回的集合数据，动态复制本步骤生成n个步骤实例，并行执行
 */
@SuppressWarnings({"PMD.UselessOverridingMethod","PMD.UnusedLocalVariable",
    "PMD.UnnecessaryFullyQualifiedName","PMD.EmptyControlStatement","java:S116","java:S101","java:S1128","java:S1161"})
public abstract class _ForkTaskStepModel extends io.nop.task.model.TaskStepsModel {
    
    /**
     *  
     * xml name: aggregator
     * 对并行步骤执行结果进行汇总处理
     */
    private io.nop.core.lang.eval.IEvalFunction _aggregator ;
    
    /**
     *  
     * xml name: autoCancelUnfinished
     * 
     */
    private boolean _autoCancelUnfinished  = true;
    
    /**
     *  
     * xml name: indexName
     * 对应于fork时的实例下标
     */
    private java.lang.String _indexName ;
    
    /**
     *  
     * xml name: joinType
     * 
     */
    private io.nop.commons.concurrent.AsyncJoinType _joinType ;
    
    /**
     *  
     * xml name: producer
     * 
     */
    private io.nop.core.lang.eval.IEvalAction _producer ;
    
    /**
     *  
     * xml name: 
     * 
     */
    private java.lang.String _type ;
    
    /**
     *  
     * xml name: varName
     * 对应于fork时的producer产生的某个元素
     */
    private java.lang.String _varName ;
    
    /**
     * 
     * xml name: aggregator
     *  对并行步骤执行结果进行汇总处理
     */
    
    public io.nop.core.lang.eval.IEvalFunction getAggregator(){
      return _aggregator;
    }

    
    public void setAggregator(io.nop.core.lang.eval.IEvalFunction value){
        checkAllowChange();
        
        this._aggregator = value;
           
    }

    
    /**
     * 
     * xml name: autoCancelUnfinished
     *  
     */
    
    public boolean isAutoCancelUnfinished(){
      return _autoCancelUnfinished;
    }

    
    public void setAutoCancelUnfinished(boolean value){
        checkAllowChange();
        
        this._autoCancelUnfinished = value;
           
    }

    
    /**
     * 
     * xml name: indexName
     *  对应于fork时的实例下标
     */
    
    public java.lang.String getIndexName(){
      return _indexName;
    }

    
    public void setIndexName(java.lang.String value){
        checkAllowChange();
        
        this._indexName = value;
           
    }

    
    /**
     * 
     * xml name: joinType
     *  
     */
    
    public io.nop.commons.concurrent.AsyncJoinType getJoinType(){
      return _joinType;
    }

    
    public void setJoinType(io.nop.commons.concurrent.AsyncJoinType value){
        checkAllowChange();
        
        this._joinType = value;
           
    }

    
    /**
     * 
     * xml name: producer
     *  
     */
    
    public io.nop.core.lang.eval.IEvalAction getProducer(){
      return _producer;
    }

    
    public void setProducer(io.nop.core.lang.eval.IEvalAction value){
        checkAllowChange();
        
        this._producer = value;
           
    }

    
    /**
     * 
     * xml name: 
     *  
     */
    
    public java.lang.String getType(){
      return _type;
    }

    
    public void setType(java.lang.String value){
        checkAllowChange();
        
        this._type = value;
           
    }

    
    /**
     * 
     * xml name: varName
     *  对应于fork时的producer产生的某个元素
     */
    
    public java.lang.String getVarName(){
      return _varName;
    }

    
    public void setVarName(java.lang.String value){
        checkAllowChange();
        
        this._varName = value;
           
    }

    

    @Override
    public void freeze(boolean cascade){
        if(frozen()) return;
        super.freeze(cascade);

        if(cascade){ //NOPMD - suppressed EmptyControlStatement - Auto Gen Code
        
        }
    }

    @Override
    protected void outputJson(IJsonHandler out){
        super.outputJson(out);
        
        out.putNotNull("aggregator",this.getAggregator());
        out.putNotNull("autoCancelUnfinished",this.isAutoCancelUnfinished());
        out.putNotNull("indexName",this.getIndexName());
        out.putNotNull("joinType",this.getJoinType());
        out.putNotNull("producer",this.getProducer());
        out.putNotNull("type",this.getType());
        out.putNotNull("varName",this.getVarName());
    }

    public ForkTaskStepModel cloneInstance(){
        ForkTaskStepModel instance = newInstance();
        this.copyTo(instance);
        return instance;
    }

    protected void copyTo(ForkTaskStepModel instance){
        super.copyTo(instance);
        
        instance.setAggregator(this.getAggregator());
        instance.setAutoCancelUnfinished(this.isAutoCancelUnfinished());
        instance.setIndexName(this.getIndexName());
        instance.setJoinType(this.getJoinType());
        instance.setProducer(this.getProducer());
        instance.setType(this.getType());
        instance.setVarName(this.getVarName());
    }

    protected ForkTaskStepModel newInstance(){
        return (ForkTaskStepModel) ClassHelper.newInstance(getClass());
    }
}
 // resume CPD analysis - CPD-ON
