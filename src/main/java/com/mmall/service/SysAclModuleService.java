package com.mmall.service;

import com.mmall.param.AclModuleParam;


/**
 * @author liliang
 * @date 2017/11/22.
 */
public interface SysAclModuleService {

    /**
     * 保存模块
     * @param param
     */
    void save(AclModuleParam param);

    /**
     * 更新模块
     * @param param
     */
    void update(AclModuleParam param);

    /**
     * 删除权限模块
     * @param id
     */
    void delete(int id);
}
