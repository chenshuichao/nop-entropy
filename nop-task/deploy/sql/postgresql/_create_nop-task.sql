
CREATE TABLE nop_task_definition(
  TASK_DEF_ID VARCHAR(32) NOT NULL ,
  TASK_NAME VARCHAR(500) NOT NULL ,
  TASK_VERSION INT8 NOT NULL ,
  DISPLAY_NAME VARCHAR(200) NOT NULL ,
  DESCRIPTION VARCHAR(1000)  ,
  MODEL_TEXT TEXT NOT NULL ,
  STATUS INT4 NOT NULL ,
  VERSION INT4 NOT NULL ,
  CREATED_BY VARCHAR(50) NOT NULL ,
  CREATE_TIME TIMESTAMP NOT NULL ,
  UPDATED_BY VARCHAR(50) NOT NULL ,
  UPDATE_TIME TIMESTAMP NOT NULL ,
  REMARK VARCHAR(200)  ,
  constraint PK_nop_task_definition primary key (TASK_DEF_ID)
);

CREATE TABLE nop_task_instance(
  TASK_INSTANCE_ID VARCHAR(32) NOT NULL ,
  TASK_NAME VARCHAR(500) NOT NULL ,
  TASK_VERSION INT8 NOT NULL ,
  TASK_INPUTS VARCHAR(4000)  ,
  TASK_GROUP VARCHAR(100) NOT NULL ,
  STATUS INT4 NOT NULL ,
  START_TIME TIMESTAMP  ,
  END_TIME TIMESTAMP  ,
  DUE_TIME TIMESTAMP  ,
  BIZ_KEY VARCHAR(200)  ,
  BIZ_OBJ_NAME VARCHAR(200)  ,
  BIZ_OBJ_ID VARCHAR(200)  ,
  PARENT_TASK_NAME VARCHAR(500)  ,
  PARENT_TASK_VERSION INT8  ,
  PARENT_TASK_ID VARCHAR(32)  ,
  PARENT_STEP_ID VARCHAR(200)  ,
  STARTER_ID VARCHAR(50)  ,
  STARTER_NAME VARCHAR(50)  ,
  STARTER_DEPT_ID VARCHAR(50)  ,
  MANAGER_TYPE VARCHAR(50)  ,
  MANAGER_DEPT_ID VARCHAR(50)  ,
  MANAGER_NAME VARCHAR(50)  ,
  MANAGER_ID VARCHAR(50)  ,
  PRIORITY INT4 NOT NULL ,
  SIGNAL_TEXT VARCHAR(1000)  ,
  TAG_TEXT VARCHAR(200)  ,
  JOB_INSTANCE_ID VARCHAR(32)  ,
  ERR_CODE VARCHAR(200)  ,
  ERR_MSG VARCHAR(500)  ,
  WORKER_ID VARCHAR(50)  ,
  VERSION INT4 NOT NULL ,
  CREATED_BY VARCHAR(50) NOT NULL ,
  CREATE_TIME TIMESTAMP NOT NULL ,
  UPDATED_BY VARCHAR(50) NOT NULL ,
  UPDATE_TIME TIMESTAMP NOT NULL ,
  REMARK VARCHAR(200)  ,
  constraint PK_nop_task_instance primary key (TASK_INSTANCE_ID)
);

CREATE TABLE nop_task_definition_auth(
  SID VARCHAR(32) NOT NULL ,
  TASK_DEF_ID VARCHAR(32) NOT NULL ,
  ACTOR_TYPE VARCHAR(10) NOT NULL ,
  ACTOR_ID VARCHAR(100) NOT NULL ,
  ACTOR_DEPT_ID VARCHAR(50)  ,
  ACTOR_NAME VARCHAR(100) NOT NULL ,
  ALLOW_EDIT BOOLEAN NOT NULL ,
  ALLOW_MANAGE BOOLEAN NOT NULL ,
  ALLOW_START BOOLEAN NOT NULL ,
  VERSION INT4 NOT NULL ,
  CREATED_BY VARCHAR(50) NOT NULL ,
  CREATE_TIME TIMESTAMP NOT NULL ,
  UPDATED_BY VARCHAR(50) NOT NULL ,
  UPDATE_TIME TIMESTAMP NOT NULL ,
  REMARK VARCHAR(200)  ,
  constraint PK_nop_task_definition_auth primary key (SID)
);

CREATE TABLE nop_task_step_instance(
  STEP_INSTANCE_ID VARCHAR(32) NOT NULL ,
  TASK_INSTANCE_ID VARCHAR(32) NOT NULL ,
  STEP_TYPE VARCHAR(20) NOT NULL ,
  STEP_NAME VARCHAR(200) NOT NULL ,
  DISPLAY_NAME VARCHAR(200) NOT NULL ,
  STEP_STATUS INT4 NOT NULL ,
  SUB_TASK_ID VARCHAR(32)  ,
  SUB_TASK_NAME VARCHAR(200)  ,
  SUB_TASK_VERSION INT8  ,
  START_TIME TIMESTAMP  ,
  FINISH_TIME TIMESTAMP  ,
  DUE_TIME TIMESTAMP  ,
  NEXT_RETRY_TIME TIMESTAMP  ,
  RETRY_COUNT INT4  ,
  INTERNAL BOOLEAN  ,
  ERR_CODE VARCHAR(200)  ,
  ERR_MSG VARCHAR(4000)  ,
  PRIORITY INT4 NOT NULL ,
  TAG_TEXT VARCHAR(200)  ,
  PARENT_STEP_ID VARCHAR(32)  ,
  WORKER_ID VARCHAR(50)  ,
  STEP_PATH VARCHAR(2000)  ,
  RUN_ID INT4 NOT NULL ,
  BODY_STEP_INDEX INT4 NOT NULL ,
  STATE_BEAN_DATA VARCHAR(4000)  ,
  VERSION INT4 NOT NULL ,
  CREATED_BY VARCHAR(50) NOT NULL ,
  CREATE_TIME TIMESTAMP NOT NULL ,
  UPDATED_BY VARCHAR(50) NOT NULL ,
  UPDATE_TIME TIMESTAMP NOT NULL ,
  REMARK VARCHAR(200)  ,
  constraint PK_nop_task_step_instance primary key (STEP_INSTANCE_ID)
);


      COMMENT ON TABLE nop_task_definition IS '逻辑流模型定义';
                
      COMMENT ON COLUMN nop_task_definition.TASK_DEF_ID IS '主键';
                    
      COMMENT ON COLUMN nop_task_definition.TASK_NAME IS '逻辑流名称';
                    
      COMMENT ON COLUMN nop_task_definition.TASK_VERSION IS '逻辑流版本';
                    
      COMMENT ON COLUMN nop_task_definition.DISPLAY_NAME IS '显示名称';
                    
      COMMENT ON COLUMN nop_task_definition.DESCRIPTION IS '描述';
                    
      COMMENT ON COLUMN nop_task_definition.MODEL_TEXT IS '模型文本';
                    
      COMMENT ON COLUMN nop_task_definition.STATUS IS '状态';
                    
      COMMENT ON COLUMN nop_task_definition.VERSION IS '数据版本';
                    
      COMMENT ON COLUMN nop_task_definition.CREATED_BY IS '创建人';
                    
      COMMENT ON COLUMN nop_task_definition.CREATE_TIME IS '创建时间';
                    
      COMMENT ON COLUMN nop_task_definition.UPDATED_BY IS '修改人';
                    
      COMMENT ON COLUMN nop_task_definition.UPDATE_TIME IS '修改时间';
                    
      COMMENT ON COLUMN nop_task_definition.REMARK IS '备注';
                    
      COMMENT ON TABLE nop_task_instance IS '逻辑流实例';
                
      COMMENT ON COLUMN nop_task_instance.TASK_INSTANCE_ID IS '主键';
                    
      COMMENT ON COLUMN nop_task_instance.TASK_NAME IS '逻辑流名称';
                    
      COMMENT ON COLUMN nop_task_instance.TASK_VERSION IS '逻辑流版本';
                    
      COMMENT ON COLUMN nop_task_instance.TASK_INPUTS IS '逻辑流参数';
                    
      COMMENT ON COLUMN nop_task_instance.TASK_GROUP IS '逻辑流分组';
                    
      COMMENT ON COLUMN nop_task_instance.STATUS IS '状态';
                    
      COMMENT ON COLUMN nop_task_instance.START_TIME IS '启动时间';
                    
      COMMENT ON COLUMN nop_task_instance.END_TIME IS '结束时间';
                    
      COMMENT ON COLUMN nop_task_instance.DUE_TIME IS '完成时限';
                    
      COMMENT ON COLUMN nop_task_instance.BIZ_KEY IS '业务唯一键';
                    
      COMMENT ON COLUMN nop_task_instance.BIZ_OBJ_NAME IS '业务对象名';
                    
      COMMENT ON COLUMN nop_task_instance.BIZ_OBJ_ID IS '业务对象ID';
                    
      COMMENT ON COLUMN nop_task_instance.PARENT_TASK_NAME IS '父流程名称';
                    
      COMMENT ON COLUMN nop_task_instance.PARENT_TASK_VERSION IS '父流程版本';
                    
      COMMENT ON COLUMN nop_task_instance.PARENT_TASK_ID IS '父流程ID';
                    
      COMMENT ON COLUMN nop_task_instance.PARENT_STEP_ID IS '父流程步骤ID';
                    
      COMMENT ON COLUMN nop_task_instance.STARTER_ID IS '启动人ID';
                    
      COMMENT ON COLUMN nop_task_instance.STARTER_NAME IS '启动人';
                    
      COMMENT ON COLUMN nop_task_instance.STARTER_DEPT_ID IS '启动人单位ID';
                    
      COMMENT ON COLUMN nop_task_instance.MANAGER_TYPE IS '管理者类型';
                    
      COMMENT ON COLUMN nop_task_instance.MANAGER_DEPT_ID IS '管理者单位ID';
                    
      COMMENT ON COLUMN nop_task_instance.MANAGER_NAME IS '管理者';
                    
      COMMENT ON COLUMN nop_task_instance.MANAGER_ID IS '管理者ID';
                    
      COMMENT ON COLUMN nop_task_instance.PRIORITY IS '优先级';
                    
      COMMENT ON COLUMN nop_task_instance.SIGNAL_TEXT IS '信号集合';
                    
      COMMENT ON COLUMN nop_task_instance.TAG_TEXT IS '标签';
                    
      COMMENT ON COLUMN nop_task_instance.JOB_INSTANCE_ID IS 'Job ID';
                    
      COMMENT ON COLUMN nop_task_instance.ERR_CODE IS '错误码';
                    
      COMMENT ON COLUMN nop_task_instance.ERR_MSG IS '错误消息';
                    
      COMMENT ON COLUMN nop_task_instance.WORKER_ID IS 'Worker ID';
                    
      COMMENT ON COLUMN nop_task_instance.VERSION IS '数据版本';
                    
      COMMENT ON COLUMN nop_task_instance.CREATED_BY IS '创建人';
                    
      COMMENT ON COLUMN nop_task_instance.CREATE_TIME IS '创建时间';
                    
      COMMENT ON COLUMN nop_task_instance.UPDATED_BY IS '修改人';
                    
      COMMENT ON COLUMN nop_task_instance.UPDATE_TIME IS '修改时间';
                    
      COMMENT ON COLUMN nop_task_instance.REMARK IS '备注';
                    
      COMMENT ON TABLE nop_task_definition_auth IS '逻辑流定义权限';
                
      COMMENT ON COLUMN nop_task_definition_auth.SID IS '主键';
                    
      COMMENT ON COLUMN nop_task_definition_auth.TASK_DEF_ID IS '工作流定义ID';
                    
      COMMENT ON COLUMN nop_task_definition_auth.ACTOR_TYPE IS '参与者类型';
                    
      COMMENT ON COLUMN nop_task_definition_auth.ACTOR_ID IS '参与者ID';
                    
      COMMENT ON COLUMN nop_task_definition_auth.ACTOR_DEPT_ID IS '参与者部门ID';
                    
      COMMENT ON COLUMN nop_task_definition_auth.ACTOR_NAME IS '参与者名称';
                    
      COMMENT ON COLUMN nop_task_definition_auth.ALLOW_EDIT IS '允许编辑';
                    
      COMMENT ON COLUMN nop_task_definition_auth.ALLOW_MANAGE IS '允许管理';
                    
      COMMENT ON COLUMN nop_task_definition_auth.ALLOW_START IS '允许启动';
                    
      COMMENT ON COLUMN nop_task_definition_auth.VERSION IS '数据版本';
                    
      COMMENT ON COLUMN nop_task_definition_auth.CREATED_BY IS '创建人';
                    
      COMMENT ON COLUMN nop_task_definition_auth.CREATE_TIME IS '创建时间';
                    
      COMMENT ON COLUMN nop_task_definition_auth.UPDATED_BY IS '修改人';
                    
      COMMENT ON COLUMN nop_task_definition_auth.UPDATE_TIME IS '修改时间';
                    
      COMMENT ON COLUMN nop_task_definition_auth.REMARK IS '备注';
                    
      COMMENT ON TABLE nop_task_step_instance IS '逻辑流步骤实例';
                
      COMMENT ON COLUMN nop_task_step_instance.STEP_INSTANCE_ID IS '步骤ID';
                    
      COMMENT ON COLUMN nop_task_step_instance.TASK_INSTANCE_ID IS '逻辑流实例ID';
                    
      COMMENT ON COLUMN nop_task_step_instance.STEP_TYPE IS '步骤类型';
                    
      COMMENT ON COLUMN nop_task_step_instance.STEP_NAME IS '步骤名称';
                    
      COMMENT ON COLUMN nop_task_step_instance.DISPLAY_NAME IS '步骤显示名称';
                    
      COMMENT ON COLUMN nop_task_step_instance.STEP_STATUS IS '状态';
                    
      COMMENT ON COLUMN nop_task_step_instance.SUB_TASK_ID IS '子流程ID';
                    
      COMMENT ON COLUMN nop_task_step_instance.SUB_TASK_NAME IS '子流程名称';
                    
      COMMENT ON COLUMN nop_task_step_instance.SUB_TASK_VERSION IS '子流程版本';
                    
      COMMENT ON COLUMN nop_task_step_instance.START_TIME IS '开始时间';
                    
      COMMENT ON COLUMN nop_task_step_instance.FINISH_TIME IS '结束时间';
                    
      COMMENT ON COLUMN nop_task_step_instance.DUE_TIME IS '到期时间';
                    
      COMMENT ON COLUMN nop_task_step_instance.NEXT_RETRY_TIME IS '下次重试时间';
                    
      COMMENT ON COLUMN nop_task_step_instance.RETRY_COUNT IS '已重试次数';
                    
      COMMENT ON COLUMN nop_task_step_instance.INTERNAL IS '是否内部';
                    
      COMMENT ON COLUMN nop_task_step_instance.ERR_CODE IS '错误码';
                    
      COMMENT ON COLUMN nop_task_step_instance.ERR_MSG IS '错误消息';
                    
      COMMENT ON COLUMN nop_task_step_instance.PRIORITY IS '优先级';
                    
      COMMENT ON COLUMN nop_task_step_instance.TAG_TEXT IS '标签';
                    
      COMMENT ON COLUMN nop_task_step_instance.PARENT_STEP_ID IS '父步骤ID';
                    
      COMMENT ON COLUMN nop_task_step_instance.WORKER_ID IS '工作者ID';
                    
      COMMENT ON COLUMN nop_task_step_instance.STEP_PATH IS '步骤路径';
                    
      COMMENT ON COLUMN nop_task_step_instance.RUN_ID IS '运行ID';
                    
      COMMENT ON COLUMN nop_task_step_instance.BODY_STEP_INDEX IS '步骤下标';
                    
      COMMENT ON COLUMN nop_task_step_instance.STATE_BEAN_DATA IS '状态数据';
                    
      COMMENT ON COLUMN nop_task_step_instance.VERSION IS '数据版本';
                    
      COMMENT ON COLUMN nop_task_step_instance.CREATED_BY IS '创建人';
                    
      COMMENT ON COLUMN nop_task_step_instance.CREATE_TIME IS '创建时间';
                    
      COMMENT ON COLUMN nop_task_step_instance.UPDATED_BY IS '修改人';
                    
      COMMENT ON COLUMN nop_task_step_instance.UPDATE_TIME IS '修改时间';
                    
      COMMENT ON COLUMN nop_task_step_instance.REMARK IS '备注';
                    
