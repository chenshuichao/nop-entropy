
CREATE TABLE nop_report_definition(
  RPT_ID VARCHAR(200) NOT NULL    COMMENT '主键',
  RPT_NAME VARCHAR(200) NOT NULL    COMMENT '报表名称',
  DESCRIPTION VARCHAR(1000) NULL    COMMENT '描述',
  RPT_TEXT MEDIUMTEXT NOT NULL    COMMENT '报表文件',
  STATUS INTEGER NOT NULL    COMMENT '状态',
  VERSION INTEGER NOT NULL    COMMENT '数据版本',
  CREATED_BY VARCHAR(50) NOT NULL    COMMENT '创建人',
  CREATE_TIME DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3)  NOT NULL    COMMENT '创建时间',
  UPDATED_BY VARCHAR(50) NOT NULL    COMMENT '修改人',
  UPDATE_TIME DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3)  NOT NULL    COMMENT '修改时间',
  REMARK VARCHAR(200) NULL    COMMENT '备注',
  constraint PK_nop_report_definition primary key (RPT_ID)
);

CREATE TABLE nop_report_dataset(
  DS_ID VARCHAR(200) NOT NULL    COMMENT '主键',
  DS_NAME VARCHAR(200) NOT NULL    COMMENT '数据集名称',
  DESCRIPTION VARCHAR(1000) NULL    COMMENT '描述',
  DS_TYPE VARCHAR(100) NOT NULL    COMMENT '数据集类型',
  DS_CONFIG VARCHAR(4000) NOT NULL    COMMENT '数据集配置',
  DS_TEXT MEDIUMTEXT NOT NULL    COMMENT '数据集文本',
  DS_META MEDIUMTEXT NOT NULL    COMMENT '数据集元数据',
  DS_VIEW MEDIUMTEXT NULL    COMMENT '数据集显示配置',
  STATUS INTEGER NOT NULL    COMMENT '状态',
  VERSION INTEGER NOT NULL    COMMENT '数据版本',
  CREATED_BY VARCHAR(50) NOT NULL    COMMENT '创建人',
  CREATE_TIME DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3)  NOT NULL    COMMENT '创建时间',
  UPDATED_BY VARCHAR(50) NOT NULL    COMMENT '修改人',
  UPDATE_TIME DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3)  NOT NULL    COMMENT '修改时间',
  REMARK VARCHAR(200) NULL    COMMENT '备注',
  constraint PK_nop_report_dataset primary key (DS_ID)
);

CREATE TABLE nop_report_dataset_auth(
  DS_ID VARCHAR(200) NOT NULL    COMMENT '主键',
  ROLE_ID VARCHAR(200) NOT NULL    COMMENT '角色ID',
  PERMISSIONS VARCHAR(100) NOT NULL    COMMENT '许可权限',
  VERSION INTEGER NOT NULL    COMMENT '数据版本',
  CREATED_BY VARCHAR(50) NOT NULL    COMMENT '创建人',
  CREATE_TIME DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3)  NOT NULL    COMMENT '创建时间',
  UPDATED_BY VARCHAR(50) NOT NULL    COMMENT '修改人',
  UPDATE_TIME DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3)  NOT NULL    COMMENT '修改时间',
  REMARK VARCHAR(200) NULL    COMMENT '备注',
  constraint PK_nop_report_dataset_auth primary key (DS_ID)
);

CREATE TABLE nop_report_result_file(
  SID VARCHAR(100) NOT NULL    COMMENT '主键',
  FILE_NAME VARCHAR(200) NOT NULL    COMMENT '文件名称',
  FILE_TYPE VARCHAR(10) NOT NULL    COMMENT '文件类型',
  FILE_PATH VARCHAR(100) NOT NULL    COMMENT '文件路径',
  DS_PARAMS VARCHAR(4000) NOT NULL    COMMENT '数据集参数',
  DS_ID VARCHAR(200) NULL    COMMENT '数据集ID',
  BIZ_DATE DATE NULL    COMMENT '业务日期',
  RPT_ID VARCHAR(200) NULL    COMMENT '报表ID',
  STATUS INTEGER NOT NULL    COMMENT '状态',
  DESCRIPTION VARCHAR(1000) NULL    COMMENT '描述',
  VERSION INTEGER NOT NULL    COMMENT '数据版本',
  CREATED_BY VARCHAR(50) NOT NULL    COMMENT '创建人',
  CREATE_TIME DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3)  NOT NULL    COMMENT '创建时间',
  UPDATED_BY VARCHAR(50) NOT NULL    COMMENT '修改人',
  UPDATE_TIME DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3)  NOT NULL    COMMENT '修改时间',
  REMARK VARCHAR(200) NULL    COMMENT '备注',
  constraint PK_nop_report_result_file primary key (SID)
);


   ALTER TABLE nop_report_definition COMMENT '报表定义';
                
   ALTER TABLE nop_report_dataset COMMENT '数据集定义';
                
   ALTER TABLE nop_report_dataset_auth COMMENT '数据集权限';
                
   ALTER TABLE nop_report_result_file COMMENT '报表结果文件';
                
