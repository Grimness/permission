package com.mmall.service;

import com.mmall.model.SysRole;
import com.mmall.model.SysUser;
import com.mmall.param.RoleParam;

import java.util.List;

/**
 * @author liliang
 * @date 2017/11/26.
 */
public interface SysRoleService {

    /**
     * 保存角色信息
     *
     * @param param
     */
    void save(RoleParam param);

    /**
     * 更新角色信息
     *
     * @param param
     */
    void update(RoleParam param);

    /**
     * 获取全部角色信息
     *
     * @return
     */
    List<SysRole> getAll();

    /**
     * 根据用户 ID 获取用户下的角色列表
     *
     * @param userId
     * @return
     */
    List<SysRole> getRoleListByUserId(int userId);

    /**
     * 根据权限点 ID 获取角色列表
     *
     * @param aclId
     * @return
     */
    List<SysRole> getRoleListByAclId(int aclId);

    /**
     * 根据角色列表获取用户列表
     *
     * @param roleList
     * @return
     */
    List<SysUser> getUserListByRoleList(List<SysRole> roleList);

}
