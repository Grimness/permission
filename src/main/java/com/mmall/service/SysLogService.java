package com.mmall.service;

import com.mmall.beans.PageQuery;
import com.mmall.beans.PageResult;
import com.mmall.model.*;
import com.mmall.param.SearchLogParam;

import java.util.List;

/**
 * @author liliang
 * @date 2017/11/30.
 */
public interface SysLogService {

    /**
     * 记录部门操作日志
     *
     * @param before
     * @param after
     */
    void saveDeptLog(SysDept before, SysDept after);

    /**
     * 记录用户操作日志
     *
     * @param before
     * @param after
     */
    void saveUserLog(SysUser before, SysUser after);

    /**
     * 记录权限模块操作日志
     *
     * @param before
     * @param after
     */
    void saveAclModuleLog(SysAclModule before, SysAclModule after);

    /**
     * 记录权限操作日志
     *
     * @param before
     * @param after
     */
    void saveAclLog(SysAcl before, SysAcl after);

    /**
     * 记录角色操作日志
     *
     * @param before
     * @param after
     */
    void saveRoleLog(SysRole before, SysRole after);


    /**
     * 搜索日志
     * @param param
     * @param query
     * @return
     */
    PageResult<SysLogWithBLOBs> searchPageList(SearchLogParam param, PageQuery query);

    /**
     * 还原日志
     * @param id
     */
    void recover(int id);
}
