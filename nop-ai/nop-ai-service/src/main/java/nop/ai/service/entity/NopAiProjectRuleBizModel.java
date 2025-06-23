
package nop.ai.service.entity;

import io.nop.api.core.annotations.biz.BizModel;
import io.nop.biz.crud.CrudBizModel;

import nop.ai.dao.entity.NopAiProjectRule;

@BizModel("NopAiProjectRule")
public class NopAiProjectRuleBizModel extends CrudBizModel<NopAiProjectRule>{
    public NopAiProjectRuleBizModel(){
        setEntityName(NopAiProjectRule.class.getName());
    }
}
