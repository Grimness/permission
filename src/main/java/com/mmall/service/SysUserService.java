package com.mmall.service;

import com.mmall.beans.PageQuery;
import com.mmall.beans.PageResult;

import com.mmall.model.SysUser;
import com.mmall.param.UserParam;

import java.util.List;


/**
 * @author liliang
 * @date 2017/11/20.
 */
public interface SysUserService {

    /**
     * 保存用户
     * @param userParam
     */
    void save(UserParam userParam);

    /**
     * 更新用户信息
     * @param userParam
     */
    void update(UserParam userParam);

    /**
     * 根据邮箱或手机号查询用户
     * @param keyword
     * @return
     */
    SysUser findByKeyWord(String keyword);

    /**
     * 根据部门 ID 获取 用户列表
     * @param deptId
     * @param pageQuery
     * @return
     */
    PageResult<SysUser> getPageByDeptId(int deptId, PageQuery pageQuery);

    /**
     * 用户所有的用户
     * @return
     */
    List<SysUser> getAll();
}
