/**
 * Copyright (c) 2017-2024 Nop Platform. All rights reserved.
 * Author: canonical_entropy@163.com
 * Blog:   https://www.zhihu.com/people/canonical-entropy
 * Gitee:  https://gitee.com/canonical-entropy/nop-entropy
 * Github: https://github.com/entropy-cloud/nop-entropy
 */
package io.nop.api.core.auth;

/**
 * 检查是否允许访问指定业务对象，或者业务对象上的某个属性。
 * <p>
 * 内部可能通过IActionAuthChecker和IDataAuthChecker实现。
 */
public interface IBizAuthChecker {
    boolean checkAuth(String bizObjName, String objId, String fieldName, ISecurityContext context);

    void forceCheckAuth(String bizObjName, String objId, String fieldName, ISecurityContext context);
}
