package com.mmall.dao;

import com.mmall.model.SysRoleAcl;

import java.util.List;

public interface SysRoleAclMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRoleAcl record);

    int insertSelective(SysRoleAcl record);

    SysRoleAcl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRoleAcl record);

    int updateByPrimaryKey(SysRoleAcl record);

    /**
     * 根据角色 ID 列表获取权限点 ID 列表
     * @param roleIdList
     * @return
     */
    List<Integer> getAclIdListByRoleIdList(List<Integer> roleIdList);

    /**
     * 根据角色 ID 删除数据
     * @param roleId
     */
    void deleteByRoleId(int roleId);

    /**
     * 批量添加角色权限关联关系
     * @param roleAclList
     */
    void batchInsert(List<SysRoleAcl> roleAclList);

    /**
     * 根据权限点获取角色 ID 列表
     * @param aclId
     * @return
     */
    List<Integer> getRoleIdListByAclId(int aclId);
}