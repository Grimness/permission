package com.mmall.dao;

import com.mmall.model.SysAclModule;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysAclModuleMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(SysAclModule record);

    int insertSelective(SysAclModule record);

    SysAclModule selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAclModule record);

    int updateByPrimaryKey(SysAclModule record);

    /**
     * 根据权限模块名称和父ID 查询模块数量
     *
     * @param parentId
     * @param name
     * @param id
     * @return
     */
    int countByNameAndParentId(@Param("parentId") Integer parentId, @Param("name") String name, @Param("id") Integer id);

    /**
     * 根据权限模块层级查询权限列表
     *
     * @param level
     * @return
     */
    List<SysAclModule> getChildAclModuleListByLevel(String level);

    /**
     * 批量添加权限模块
     *
     * @param sysAclModuleList
     * @return
     */
    int batchUpdateLevel(List<SysAclModule> sysAclModuleList);

    /**
     * 获取全部权限模块
     *
     * @return
     */
    List<SysAclModule> getAllAclModule();

    /**
     * 根据 父ID获取权限模块数量
     *
     * @param parentId
     * @return
     */
    int countByParentId(int parentId);


}