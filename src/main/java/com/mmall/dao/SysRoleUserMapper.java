package com.mmall.dao;

import com.mmall.model.SysRoleUser;

import java.util.List;

public interface SysRoleUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRoleUser record);

    int insertSelective(SysRoleUser record);

    SysRoleUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRoleUser record);

    int updateByPrimaryKey(SysRoleUser record);

    /**
     * 根据用户 ID 查找角色 ID 列表
     *
     * @param userId 用户 ID
     * @return
     */
    List<Integer> getRoleIdListByUserId(int userId);

    /**
     * 根据角色 ID 查找用户 ID 列表
     *
     * @param roleId 角色 ID
     * @return
     */
    List<Integer> getUserIdListByRoleId(int roleId);

    /**
     * 根据角色 ID 删除关联的用户
     *
     * @param roleId
     */
    void deleteByRoleId(int roleId);

    /**
     * 批量添加角色与用户的关联关系
     *
     * @param roleUserList
     */
    void batchInsert(List<SysRoleUser> roleUserList);

    /**
     * 根据角色 ID 列表获取用户 ID 列表
     *
     * @param roleIdList
     * @return
     */
    List<Integer> getUserIdListByRoleIdList(List<Integer> roleIdList);
}