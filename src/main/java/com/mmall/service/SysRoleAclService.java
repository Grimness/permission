package com.mmall.service;

import java.util.List;

/**
 * @author liliang
 * @date 2017/11/27.
 */
public interface SysRoleAclService {

    /**
     * 改变角色对应的权限
     * @param roleId
     * @param aclIdList
     */
    void changeRoleAcls(Integer roleId, List<Integer> aclIdList);


}
