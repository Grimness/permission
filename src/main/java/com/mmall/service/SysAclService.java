package com.mmall.service;

import com.mmall.beans.PageQuery;
import com.mmall.beans.PageResult;

import com.mmall.model.SysAcl;
import com.mmall.param.AclParam;


/**
 * @author liliang
 * @date 2017/11/25.
 */
public interface SysAclService {

    /**
     * 保存权限
     * @param param
     */
    void save(AclParam param);

    /**
     * 更新权限
     * @param param
     */
    void update(AclParam param);

    /**
     * 获取权限模块下的权限点列表
     * @param aclModuleId
     * @param pageQuery
     * @return
     */
    PageResult<SysAcl> getPageByAclModuleId(int aclModuleId, PageQuery pageQuery);
}
