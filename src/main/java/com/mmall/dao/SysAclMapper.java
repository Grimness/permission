package com.mmall.dao;


import com.mmall.beans.PageQuery;
import com.mmall.model.SysAcl;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysAclMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysAcl record);

    int insertSelective(SysAcl record);

    SysAcl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAcl record);

    int updateByPrimaryKey(SysAcl record);

    /**
     * 根据权限名称和权限模块 ID 获取权限数量
     *
     * @param aclModuleId
     * @param name
     * @param id
     * @return
     */
    int countByNameAndAclModuId(@Param("aclModuleId") int aclModuleId, @Param("name") String name, @Param("id") Integer id);

    /**
     * 根据权限模块 ID 获取权限数量
     *
     * @param aclModuleId
     * @return
     */
    int countByAclModuleId(int aclModuleId);

    /**
     * 根据权限模块 ID 获取权限列表
     *
     * @param aclModuleId
     * @param pageQuery
     * @return
     */
    List<SysAcl> getPageByAclModuleId(@Param("aclModuleId") int aclModuleId, @Param("page") PageQuery pageQuery);

    /**
     * 获取全部权限列表
     *
     * @return
     */
    List<SysAcl> getAll();

    /**
     * 根据权限 ID 列表获取权限列表
     *
     * @param idList
     * @return
     */
    List<SysAcl> getByIdList(List<Integer> idList);

    /**
     * 根据 url获取权限点信息
     *
     * @param url
     * @return
     */
    List<SysAcl> getByUrl(String url);


}