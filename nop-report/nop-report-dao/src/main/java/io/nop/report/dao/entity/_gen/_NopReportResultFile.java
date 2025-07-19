package io.nop.report.dao.entity._gen;

import io.nop.orm.model.IEntityModel;
import io.nop.orm.support.DynamicOrmEntity;
import io.nop.orm.support.OrmEntitySet; //NOPMD - suppressed UnusedImports - Auto Gen Code
import io.nop.orm.IOrmEntitySet; //NOPMD - suppressed UnusedImports - Auto Gen Code
import io.nop.api.core.convert.ConvertHelper;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.List;

import io.nop.report.dao.entity.NopReportResultFile;

// tell cpd to start ignoring code - CPD-OFF
/**
 *  报表结果文件: nop_report_result_file
 */
@SuppressWarnings({"PMD.UselessOverridingMethod","PMD.UnusedLocalVariable","java:S3008","java:S1602","java:S1128","java:S1161",
        "PMD.UnnecessaryFullyQualifiedName","PMD.EmptyControlStatement","java:S116","java:S115","java:S101","java:S3776"})
public class _NopReportResultFile extends DynamicOrmEntity{
    
    /* 主键: SID VARCHAR */
    public static final String PROP_NAME_sid = "sid";
    public static final int PROP_ID_sid = 1;
    
    /* 文件名称: FILE_NAME VARCHAR */
    public static final String PROP_NAME_fileName = "fileName";
    public static final int PROP_ID_fileName = 2;
    
    /* 文件类型: FILE_TYPE VARCHAR */
    public static final String PROP_NAME_fileType = "fileType";
    public static final int PROP_ID_fileType = 3;
    
    /* 文件路径: FILE_PATH VARCHAR */
    public static final String PROP_NAME_filePath = "filePath";
    public static final int PROP_ID_filePath = 4;
    
    /* 文件长度: FILE_LENGTH BIGINT */
    public static final String PROP_NAME_fileLength = "fileLength";
    public static final int PROP_ID_fileLength = 5;
    
    /* 业务日期: BIZ_DATE DATE */
    public static final String PROP_NAME_bizDate = "bizDate";
    public static final int PROP_ID_bizDate = 6;
    
    /* 报表ID: RPT_ID VARCHAR */
    public static final String PROP_NAME_rptId = "rptId";
    public static final int PROP_ID_rptId = 7;
    
    /* 报表参数: RPT_PARAMS VARCHAR */
    public static final String PROP_NAME_rptParams = "rptParams";
    public static final int PROP_ID_rptParams = 8;
    
    /* 状态: STATUS INTEGER */
    public static final String PROP_NAME_status = "status";
    public static final int PROP_ID_status = 9;
    
    /* 描述: DESCRIPTION VARCHAR */
    public static final String PROP_NAME_description = "description";
    public static final int PROP_ID_description = 10;
    
    /* 数据版本: VERSION INTEGER */
    public static final String PROP_NAME_version = "version";
    public static final int PROP_ID_version = 11;
    
    /* 创建人: CREATED_BY VARCHAR */
    public static final String PROP_NAME_createdBy = "createdBy";
    public static final int PROP_ID_createdBy = 12;
    
    /* 创建时间: CREATE_TIME TIMESTAMP */
    public static final String PROP_NAME_createTime = "createTime";
    public static final int PROP_ID_createTime = 13;
    
    /* 修改人: UPDATED_BY VARCHAR */
    public static final String PROP_NAME_updatedBy = "updatedBy";
    public static final int PROP_ID_updatedBy = 14;
    
    /* 修改时间: UPDATE_TIME TIMESTAMP */
    public static final String PROP_NAME_updateTime = "updateTime";
    public static final int PROP_ID_updateTime = 15;
    
    /* 备注: REMARK VARCHAR */
    public static final String PROP_NAME_remark = "remark";
    public static final int PROP_ID_remark = 16;
    

    private static int _PROP_ID_BOUND = 17;

    
    /* relation: 报表定义 */
    public static final String PROP_NAME_reportDefinition = "reportDefinition";
    

    protected static final List<String> PK_PROP_NAMES = Arrays.asList(PROP_NAME_sid);
    protected static final int[] PK_PROP_IDS = new int[]{PROP_ID_sid};

    private static final String[] PROP_ID_TO_NAME = new String[17];
    private static final Map<String,Integer> PROP_NAME_TO_ID = new HashMap<>();
    static{
      
          PROP_ID_TO_NAME[PROP_ID_sid] = PROP_NAME_sid;
          PROP_NAME_TO_ID.put(PROP_NAME_sid, PROP_ID_sid);
      
          PROP_ID_TO_NAME[PROP_ID_fileName] = PROP_NAME_fileName;
          PROP_NAME_TO_ID.put(PROP_NAME_fileName, PROP_ID_fileName);
      
          PROP_ID_TO_NAME[PROP_ID_fileType] = PROP_NAME_fileType;
          PROP_NAME_TO_ID.put(PROP_NAME_fileType, PROP_ID_fileType);
      
          PROP_ID_TO_NAME[PROP_ID_filePath] = PROP_NAME_filePath;
          PROP_NAME_TO_ID.put(PROP_NAME_filePath, PROP_ID_filePath);
      
          PROP_ID_TO_NAME[PROP_ID_fileLength] = PROP_NAME_fileLength;
          PROP_NAME_TO_ID.put(PROP_NAME_fileLength, PROP_ID_fileLength);
      
          PROP_ID_TO_NAME[PROP_ID_bizDate] = PROP_NAME_bizDate;
          PROP_NAME_TO_ID.put(PROP_NAME_bizDate, PROP_ID_bizDate);
      
          PROP_ID_TO_NAME[PROP_ID_rptId] = PROP_NAME_rptId;
          PROP_NAME_TO_ID.put(PROP_NAME_rptId, PROP_ID_rptId);
      
          PROP_ID_TO_NAME[PROP_ID_rptParams] = PROP_NAME_rptParams;
          PROP_NAME_TO_ID.put(PROP_NAME_rptParams, PROP_ID_rptParams);
      
          PROP_ID_TO_NAME[PROP_ID_status] = PROP_NAME_status;
          PROP_NAME_TO_ID.put(PROP_NAME_status, PROP_ID_status);
      
          PROP_ID_TO_NAME[PROP_ID_description] = PROP_NAME_description;
          PROP_NAME_TO_ID.put(PROP_NAME_description, PROP_ID_description);
      
          PROP_ID_TO_NAME[PROP_ID_version] = PROP_NAME_version;
          PROP_NAME_TO_ID.put(PROP_NAME_version, PROP_ID_version);
      
          PROP_ID_TO_NAME[PROP_ID_createdBy] = PROP_NAME_createdBy;
          PROP_NAME_TO_ID.put(PROP_NAME_createdBy, PROP_ID_createdBy);
      
          PROP_ID_TO_NAME[PROP_ID_createTime] = PROP_NAME_createTime;
          PROP_NAME_TO_ID.put(PROP_NAME_createTime, PROP_ID_createTime);
      
          PROP_ID_TO_NAME[PROP_ID_updatedBy] = PROP_NAME_updatedBy;
          PROP_NAME_TO_ID.put(PROP_NAME_updatedBy, PROP_ID_updatedBy);
      
          PROP_ID_TO_NAME[PROP_ID_updateTime] = PROP_NAME_updateTime;
          PROP_NAME_TO_ID.put(PROP_NAME_updateTime, PROP_ID_updateTime);
      
          PROP_ID_TO_NAME[PROP_ID_remark] = PROP_NAME_remark;
          PROP_NAME_TO_ID.put(PROP_NAME_remark, PROP_ID_remark);
      
    }

    
    /* 主键: SID */
    private java.lang.String _sid;
    
    /* 文件名称: FILE_NAME */
    private java.lang.String _fileName;
    
    /* 文件类型: FILE_TYPE */
    private java.lang.String _fileType;
    
    /* 文件路径: FILE_PATH */
    private java.lang.String _filePath;
    
    /* 文件长度: FILE_LENGTH */
    private java.lang.Long _fileLength;
    
    /* 业务日期: BIZ_DATE */
    private java.time.LocalDate _bizDate;
    
    /* 报表ID: RPT_ID */
    private java.lang.String _rptId;
    
    /* 报表参数: RPT_PARAMS */
    private java.lang.String _rptParams;
    
    /* 状态: STATUS */
    private java.lang.Integer _status;
    
    /* 描述: DESCRIPTION */
    private java.lang.String _description;
    
    /* 数据版本: VERSION */
    private java.lang.Integer _version;
    
    /* 创建人: CREATED_BY */
    private java.lang.String _createdBy;
    
    /* 创建时间: CREATE_TIME */
    private java.sql.Timestamp _createTime;
    
    /* 修改人: UPDATED_BY */
    private java.lang.String _updatedBy;
    
    /* 修改时间: UPDATE_TIME */
    private java.sql.Timestamp _updateTime;
    
    /* 备注: REMARK */
    private java.lang.String _remark;
    

    public _NopReportResultFile(){
        // for debug
    }

    protected NopReportResultFile newInstance(){
        NopReportResultFile entity = new NopReportResultFile();
        entity.orm_attach(orm_enhancer());
        entity.orm_entityModel(orm_entityModel());
        return entity;
    }

    @Override
    public NopReportResultFile cloneInstance() {
        NopReportResultFile entity = newInstance();
        orm_forEachInitedProp((value, propId) -> {
            entity.orm_propValue(propId,value);
        });
        return entity;
    }

    @Override
    public String orm_entityName() {
      // 如果存在实体模型对象，则以模型对象上的设置为准
      IEntityModel entityModel = orm_entityModel();
      if(entityModel != null)
          return entityModel.getName();
      return "io.nop.report.dao.entity.NopReportResultFile";
    }

    @Override
    public int orm_propIdBound(){
      IEntityModel entityModel = orm_entityModel();
      if(entityModel != null)
          return entityModel.getPropIdBound();
      return _PROP_ID_BOUND;
    }

    @Override
    public Object orm_id() {
    
        return buildSimpleId(PROP_ID_sid);
     
    }

    @Override
    public boolean orm_isPrimary(int propId) {
        
            return propId == PROP_ID_sid;
          
    }

    @Override
    public String orm_propName(int propId) {
        if(propId >= PROP_ID_TO_NAME.length)
            return super.orm_propName(propId);
        String propName = PROP_ID_TO_NAME[propId];
        if(propName == null)
           return super.orm_propName(propId);
        return propName;
    }

    @Override
    public int orm_propId(String propName) {
        Integer propId = PROP_NAME_TO_ID.get(propName);
        if(propId == null)
            return super.orm_propId(propName);
        return propId;
    }

    @Override
    public Object orm_propValue(int propId) {
        switch(propId){
        
            case PROP_ID_sid:
               return getSid();
        
            case PROP_ID_fileName:
               return getFileName();
        
            case PROP_ID_fileType:
               return getFileType();
        
            case PROP_ID_filePath:
               return getFilePath();
        
            case PROP_ID_fileLength:
               return getFileLength();
        
            case PROP_ID_bizDate:
               return getBizDate();
        
            case PROP_ID_rptId:
               return getRptId();
        
            case PROP_ID_rptParams:
               return getRptParams();
        
            case PROP_ID_status:
               return getStatus();
        
            case PROP_ID_description:
               return getDescription();
        
            case PROP_ID_version:
               return getVersion();
        
            case PROP_ID_createdBy:
               return getCreatedBy();
        
            case PROP_ID_createTime:
               return getCreateTime();
        
            case PROP_ID_updatedBy:
               return getUpdatedBy();
        
            case PROP_ID_updateTime:
               return getUpdateTime();
        
            case PROP_ID_remark:
               return getRemark();
        
           default:
              return super.orm_propValue(propId);
        }
    }

    

    @Override
    public void orm_propValue(int propId, Object value){
        switch(propId){
        
            case PROP_ID_sid:{
               java.lang.String typedValue = null;
               if(value != null){
                   typedValue = ConvertHelper.toString(value,
                       err-> newTypeConversionError(PROP_NAME_sid));
               }
               setSid(typedValue);
               break;
            }
        
            case PROP_ID_fileName:{
               java.lang.String typedValue = null;
               if(value != null){
                   typedValue = ConvertHelper.toString(value,
                       err-> newTypeConversionError(PROP_NAME_fileName));
               }
               setFileName(typedValue);
               break;
            }
        
            case PROP_ID_fileType:{
               java.lang.String typedValue = null;
               if(value != null){
                   typedValue = ConvertHelper.toString(value,
                       err-> newTypeConversionError(PROP_NAME_fileType));
               }
               setFileType(typedValue);
               break;
            }
        
            case PROP_ID_filePath:{
               java.lang.String typedValue = null;
               if(value != null){
                   typedValue = ConvertHelper.toString(value,
                       err-> newTypeConversionError(PROP_NAME_filePath));
               }
               setFilePath(typedValue);
               break;
            }
        
            case PROP_ID_fileLength:{
               java.lang.Long typedValue = null;
               if(value != null){
                   typedValue = ConvertHelper.toLong(value,
                       err-> newTypeConversionError(PROP_NAME_fileLength));
               }
               setFileLength(typedValue);
               break;
            }
        
            case PROP_ID_bizDate:{
               java.time.LocalDate typedValue = null;
               if(value != null){
                   typedValue = ConvertHelper.toLocalDate(value,
                       err-> newTypeConversionError(PROP_NAME_bizDate));
               }
               setBizDate(typedValue);
               break;
            }
        
            case PROP_ID_rptId:{
               java.lang.String typedValue = null;
               if(value != null){
                   typedValue = ConvertHelper.toString(value,
                       err-> newTypeConversionError(PROP_NAME_rptId));
               }
               setRptId(typedValue);
               break;
            }
        
            case PROP_ID_rptParams:{
               java.lang.String typedValue = null;
               if(value != null){
                   typedValue = ConvertHelper.toString(value,
                       err-> newTypeConversionError(PROP_NAME_rptParams));
               }
               setRptParams(typedValue);
               break;
            }
        
            case PROP_ID_status:{
               java.lang.Integer typedValue = null;
               if(value != null){
                   typedValue = ConvertHelper.toInteger(value,
                       err-> newTypeConversionError(PROP_NAME_status));
               }
               setStatus(typedValue);
               break;
            }
        
            case PROP_ID_description:{
               java.lang.String typedValue = null;
               if(value != null){
                   typedValue = ConvertHelper.toString(value,
                       err-> newTypeConversionError(PROP_NAME_description));
               }
               setDescription(typedValue);
               break;
            }
        
            case PROP_ID_version:{
               java.lang.Integer typedValue = null;
               if(value != null){
                   typedValue = ConvertHelper.toInteger(value,
                       err-> newTypeConversionError(PROP_NAME_version));
               }
               setVersion(typedValue);
               break;
            }
        
            case PROP_ID_createdBy:{
               java.lang.String typedValue = null;
               if(value != null){
                   typedValue = ConvertHelper.toString(value,
                       err-> newTypeConversionError(PROP_NAME_createdBy));
               }
               setCreatedBy(typedValue);
               break;
            }
        
            case PROP_ID_createTime:{
               java.sql.Timestamp typedValue = null;
               if(value != null){
                   typedValue = ConvertHelper.toTimestamp(value,
                       err-> newTypeConversionError(PROP_NAME_createTime));
               }
               setCreateTime(typedValue);
               break;
            }
        
            case PROP_ID_updatedBy:{
               java.lang.String typedValue = null;
               if(value != null){
                   typedValue = ConvertHelper.toString(value,
                       err-> newTypeConversionError(PROP_NAME_updatedBy));
               }
               setUpdatedBy(typedValue);
               break;
            }
        
            case PROP_ID_updateTime:{
               java.sql.Timestamp typedValue = null;
               if(value != null){
                   typedValue = ConvertHelper.toTimestamp(value,
                       err-> newTypeConversionError(PROP_NAME_updateTime));
               }
               setUpdateTime(typedValue);
               break;
            }
        
            case PROP_ID_remark:{
               java.lang.String typedValue = null;
               if(value != null){
                   typedValue = ConvertHelper.toString(value,
                       err-> newTypeConversionError(PROP_NAME_remark));
               }
               setRemark(typedValue);
               break;
            }
        
           default:
              super.orm_propValue(propId,value);
        }
    }

    @Override
    public void orm_internalSet(int propId, Object value) {
        switch(propId){
        
            case PROP_ID_sid:{
               onInitProp(propId);
               this._sid = (java.lang.String)value;
               orm_id(); // 如果是设置主键字段，则触发watcher
               break;
            }
        
            case PROP_ID_fileName:{
               onInitProp(propId);
               this._fileName = (java.lang.String)value;
               
               break;
            }
        
            case PROP_ID_fileType:{
               onInitProp(propId);
               this._fileType = (java.lang.String)value;
               
               break;
            }
        
            case PROP_ID_filePath:{
               onInitProp(propId);
               this._filePath = (java.lang.String)value;
               
               break;
            }
        
            case PROP_ID_fileLength:{
               onInitProp(propId);
               this._fileLength = (java.lang.Long)value;
               
               break;
            }
        
            case PROP_ID_bizDate:{
               onInitProp(propId);
               this._bizDate = (java.time.LocalDate)value;
               
               break;
            }
        
            case PROP_ID_rptId:{
               onInitProp(propId);
               this._rptId = (java.lang.String)value;
               
               break;
            }
        
            case PROP_ID_rptParams:{
               onInitProp(propId);
               this._rptParams = (java.lang.String)value;
               
               break;
            }
        
            case PROP_ID_status:{
               onInitProp(propId);
               this._status = (java.lang.Integer)value;
               
               break;
            }
        
            case PROP_ID_description:{
               onInitProp(propId);
               this._description = (java.lang.String)value;
               
               break;
            }
        
            case PROP_ID_version:{
               onInitProp(propId);
               this._version = (java.lang.Integer)value;
               
               break;
            }
        
            case PROP_ID_createdBy:{
               onInitProp(propId);
               this._createdBy = (java.lang.String)value;
               
               break;
            }
        
            case PROP_ID_createTime:{
               onInitProp(propId);
               this._createTime = (java.sql.Timestamp)value;
               
               break;
            }
        
            case PROP_ID_updatedBy:{
               onInitProp(propId);
               this._updatedBy = (java.lang.String)value;
               
               break;
            }
        
            case PROP_ID_updateTime:{
               onInitProp(propId);
               this._updateTime = (java.sql.Timestamp)value;
               
               break;
            }
        
            case PROP_ID_remark:{
               onInitProp(propId);
               this._remark = (java.lang.String)value;
               
               break;
            }
        
           default:
              super.orm_internalSet(propId,value);
        }
    }

    
    /**
     * 主键: SID
     */
    public final java.lang.String getSid(){
         onPropGet(PROP_ID_sid);
         return _sid;
    }

    /**
     * 主键: SID
     */
    public final void setSid(java.lang.String value){
        if(onPropSet(PROP_ID_sid,value)){
            this._sid = value;
            internalClearRefs(PROP_ID_sid);
            orm_id();
        }
    }
    
    /**
     * 文件名称: FILE_NAME
     */
    public final java.lang.String getFileName(){
         onPropGet(PROP_ID_fileName);
         return _fileName;
    }

    /**
     * 文件名称: FILE_NAME
     */
    public final void setFileName(java.lang.String value){
        if(onPropSet(PROP_ID_fileName,value)){
            this._fileName = value;
            internalClearRefs(PROP_ID_fileName);
            
        }
    }
    
    /**
     * 文件类型: FILE_TYPE
     */
    public final java.lang.String getFileType(){
         onPropGet(PROP_ID_fileType);
         return _fileType;
    }

    /**
     * 文件类型: FILE_TYPE
     */
    public final void setFileType(java.lang.String value){
        if(onPropSet(PROP_ID_fileType,value)){
            this._fileType = value;
            internalClearRefs(PROP_ID_fileType);
            
        }
    }
    
    /**
     * 文件路径: FILE_PATH
     */
    public final java.lang.String getFilePath(){
         onPropGet(PROP_ID_filePath);
         return _filePath;
    }

    /**
     * 文件路径: FILE_PATH
     */
    public final void setFilePath(java.lang.String value){
        if(onPropSet(PROP_ID_filePath,value)){
            this._filePath = value;
            internalClearRefs(PROP_ID_filePath);
            
        }
    }
    
    /**
     * 文件长度: FILE_LENGTH
     */
    public final java.lang.Long getFileLength(){
         onPropGet(PROP_ID_fileLength);
         return _fileLength;
    }

    /**
     * 文件长度: FILE_LENGTH
     */
    public final void setFileLength(java.lang.Long value){
        if(onPropSet(PROP_ID_fileLength,value)){
            this._fileLength = value;
            internalClearRefs(PROP_ID_fileLength);
            
        }
    }
    
    /**
     * 业务日期: BIZ_DATE
     */
    public final java.time.LocalDate getBizDate(){
         onPropGet(PROP_ID_bizDate);
         return _bizDate;
    }

    /**
     * 业务日期: BIZ_DATE
     */
    public final void setBizDate(java.time.LocalDate value){
        if(onPropSet(PROP_ID_bizDate,value)){
            this._bizDate = value;
            internalClearRefs(PROP_ID_bizDate);
            
        }
    }
    
    /**
     * 报表ID: RPT_ID
     */
    public final java.lang.String getRptId(){
         onPropGet(PROP_ID_rptId);
         return _rptId;
    }

    /**
     * 报表ID: RPT_ID
     */
    public final void setRptId(java.lang.String value){
        if(onPropSet(PROP_ID_rptId,value)){
            this._rptId = value;
            internalClearRefs(PROP_ID_rptId);
            
        }
    }
    
    /**
     * 报表参数: RPT_PARAMS
     */
    public final java.lang.String getRptParams(){
         onPropGet(PROP_ID_rptParams);
         return _rptParams;
    }

    /**
     * 报表参数: RPT_PARAMS
     */
    public final void setRptParams(java.lang.String value){
        if(onPropSet(PROP_ID_rptParams,value)){
            this._rptParams = value;
            internalClearRefs(PROP_ID_rptParams);
            
        }
    }
    
    /**
     * 状态: STATUS
     */
    public final java.lang.Integer getStatus(){
         onPropGet(PROP_ID_status);
         return _status;
    }

    /**
     * 状态: STATUS
     */
    public final void setStatus(java.lang.Integer value){
        if(onPropSet(PROP_ID_status,value)){
            this._status = value;
            internalClearRefs(PROP_ID_status);
            
        }
    }
    
    /**
     * 描述: DESCRIPTION
     */
    public final java.lang.String getDescription(){
         onPropGet(PROP_ID_description);
         return _description;
    }

    /**
     * 描述: DESCRIPTION
     */
    public final void setDescription(java.lang.String value){
        if(onPropSet(PROP_ID_description,value)){
            this._description = value;
            internalClearRefs(PROP_ID_description);
            
        }
    }
    
    /**
     * 数据版本: VERSION
     */
    public final java.lang.Integer getVersion(){
         onPropGet(PROP_ID_version);
         return _version;
    }

    /**
     * 数据版本: VERSION
     */
    public final void setVersion(java.lang.Integer value){
        if(onPropSet(PROP_ID_version,value)){
            this._version = value;
            internalClearRefs(PROP_ID_version);
            
        }
    }
    
    /**
     * 创建人: CREATED_BY
     */
    public final java.lang.String getCreatedBy(){
         onPropGet(PROP_ID_createdBy);
         return _createdBy;
    }

    /**
     * 创建人: CREATED_BY
     */
    public final void setCreatedBy(java.lang.String value){
        if(onPropSet(PROP_ID_createdBy,value)){
            this._createdBy = value;
            internalClearRefs(PROP_ID_createdBy);
            
        }
    }
    
    /**
     * 创建时间: CREATE_TIME
     */
    public final java.sql.Timestamp getCreateTime(){
         onPropGet(PROP_ID_createTime);
         return _createTime;
    }

    /**
     * 创建时间: CREATE_TIME
     */
    public final void setCreateTime(java.sql.Timestamp value){
        if(onPropSet(PROP_ID_createTime,value)){
            this._createTime = value;
            internalClearRefs(PROP_ID_createTime);
            
        }
    }
    
    /**
     * 修改人: UPDATED_BY
     */
    public final java.lang.String getUpdatedBy(){
         onPropGet(PROP_ID_updatedBy);
         return _updatedBy;
    }

    /**
     * 修改人: UPDATED_BY
     */
    public final void setUpdatedBy(java.lang.String value){
        if(onPropSet(PROP_ID_updatedBy,value)){
            this._updatedBy = value;
            internalClearRefs(PROP_ID_updatedBy);
            
        }
    }
    
    /**
     * 修改时间: UPDATE_TIME
     */
    public final java.sql.Timestamp getUpdateTime(){
         onPropGet(PROP_ID_updateTime);
         return _updateTime;
    }

    /**
     * 修改时间: UPDATE_TIME
     */
    public final void setUpdateTime(java.sql.Timestamp value){
        if(onPropSet(PROP_ID_updateTime,value)){
            this._updateTime = value;
            internalClearRefs(PROP_ID_updateTime);
            
        }
    }
    
    /**
     * 备注: REMARK
     */
    public final java.lang.String getRemark(){
         onPropGet(PROP_ID_remark);
         return _remark;
    }

    /**
     * 备注: REMARK
     */
    public final void setRemark(java.lang.String value){
        if(onPropSet(PROP_ID_remark,value)){
            this._remark = value;
            internalClearRefs(PROP_ID_remark);
            
        }
    }
    
    /**
     * 报表定义
     */
    public final io.nop.report.dao.entity.NopReportDefinition getReportDefinition(){
       return (io.nop.report.dao.entity.NopReportDefinition)internalGetRefEntity(PROP_NAME_reportDefinition);
    }

    public final void setReportDefinition(io.nop.report.dao.entity.NopReportDefinition refEntity){
   
           if(refEntity == null){
           
                   this.setRptId(null);
               
           }else{
           internalSetRefEntity(PROP_NAME_reportDefinition, refEntity,()->{
           
                           this.setRptId(refEntity.getRptId());
                       
           });
           }
       
    }
       
}
// resume CPD analysis - CPD-ON
