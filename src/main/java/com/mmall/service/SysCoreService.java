package com.mmall.service;

import com.mmall.model.SysAcl;

import java.util.List;

/**
 * @author liliang
 * @date 2017/11/27.
 */
public interface SysCoreService {


    /**
     * 获取当前用户的权限列表
     *
     * @return
     */
    List<SysAcl> getCurrentUserAclList();

    /**
     * 获取角色已分配的权限点
     *
     * @param roleId
     * @return
     */
    List<SysAcl> getRoleAclList(int roleId);

    /**
     * 查询指定用已分配的权限列表
     *
     * @param userId
     * @return
     */
    List<SysAcl> getUserAclList(int userId);

    /**
     * url 是否有访问权限
     * @param url
     * @return
     */
    public boolean hasUrlAcl(String url);

}
