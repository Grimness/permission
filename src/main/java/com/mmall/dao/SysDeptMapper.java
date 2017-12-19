package com.mmall.dao;

import com.mmall.model.SysDept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysDeptMapper {
    /**
     * 主键删除
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 新增
     * @param record
     * @return
     */
    int insert(SysDept record);

    /**
     * 条件新增
     * @param record
     * @return
     */
    int insertSelective(SysDept record);

    /**
     * 主键查询
     * @param id
     * @return
     */
    SysDept selectByPrimaryKey(Integer id);

    /**
     * 条件更新
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(SysDept record);

    /**
     * 主键更新
     * @param record
     * @return
     */
    int updateByPrimaryKey(SysDept record);

    /**
     * 查询全部
     * @return
     */
    List<SysDept> getAllDept();

    /**
     * 根据 level 查询SysDept 列表
     * @param level
     * @return
     */
    List<SysDept> getChildDeptListByLevel(String level);

    /**
     * 批量更新 Level
     * @param sysDeptList
     */
    void batchUpdateLevel(List<SysDept> sysDeptList);

    /**
     * 根据部门模块ID,父ID 和部门名称获取部门数量
     * @param parentId
     * @param name
     * @param id
     * @return
     */
    int countByNameAndParentId(@Param("parentId") Integer parentId, @Param("name") String name,@Param("id")  Integer id);

    /**
     *  根据父ID 获取部门数量
     * @param parentId
     * @return
     */
    int countByParentId(int parentId);
}