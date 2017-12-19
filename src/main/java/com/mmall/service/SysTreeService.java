package com.mmall.service;


import com.mmall.dto.AclModuleLevelDto;
import com.mmall.dto.DeptLevelDto;

import java.util.*;


/**
 * @author liliang
 * @date 2017/11/17.
 */
public interface SysTreeService {


    /**
     * 根据角色 ID 获取权限树
     * @param roleId
     * @return
     */
   List<AclModuleLevelDto> roleTree(int roleId);

    /**
     * 权限模块列表生成树
     *
     * @return
     */
    List<AclModuleLevelDto> aclModuleTree();


    /**
     * 获取部门树
     * @return
     */
    List<DeptLevelDto> deptTree();

    /**
     * 获取部门
     *         用户树
     * @param userId
     * @return
     */
    List<AclModuleLevelDto> userAclTree(int userId);


}
