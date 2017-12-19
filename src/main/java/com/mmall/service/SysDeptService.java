package com.mmall.service;

import com.mmall.param.DeptParam;

/**
 * @author liliang
 * @date 2017/11/17.
 */
public interface SysDeptService {


    /**
     * 保存部门信息
     *
     * @param param
     */
    void save(DeptParam param);

    /**
     * 更新部门信息
     *
     * @param param
     */
    void update(DeptParam param);


    /**
     * 根据 ID 删除部门
     *
     * @param id
     */
    void delete(int id);
}
