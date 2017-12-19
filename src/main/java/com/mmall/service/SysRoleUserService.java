package com.mmall.service;

import com.mmall.model.SysUser;

import java.util.List;

/**
 * @author liliang
 * @date 2017/11/27.
 */
public interface SysRoleUserService {

    /**
     * 根据角色 ID 查找角色下的用户列表
     *
     * @param roleId 角色 ID
     * @return
     */
    List<SysUser> getListByRoleId(int roleId);

    /**
     * 更新角色下的用户列表
     * @param roleId 角色 ID
     * @param userIdList 用户列表 ID
     */
    void changRoleUsers(int roleId, List<Integer> userIdList);

}
