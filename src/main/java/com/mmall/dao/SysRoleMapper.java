package com.mmall.dao;

import com.mmall.model.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

    /**
     * 获取全部角色信息
     * @return
     */
    List<SysRole> getAll();

    /**
     * 根据角色名称获取角色数量
     * @param name
     * @param id
     * @return
     */
    int countByName(@Param("name") String name,@Param("id") Integer id);

    /**
     * 根据角色 ID 列表获取角色信息列表
     * @param idList
     * @return
     */
    List<SysRole> getByIdList(List<Integer> idList);
}