package com.mmall.dao;

import com.mmall.beans.PageQuery;
import com.mmall.model.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    /**
     * 根据关键字查询用户
     * @param keyword
     * @return
     */
    SysUser findByKeyWord(String keyword);

    /**
     * 根据邮箱和 ID 统计用户数
     * @param mail
     * @param id
     * @return
     */
    int countByMail(@Param("mail") String mail, @Param("id") Integer id);

    /**
     * 根据电话号码统计用户数
     * @param telephone
     * @param id
     * @return
     */
    int countByTelephone(@Param("telephone") String telephone, @Param("id") Integer id);

    /**
     * 根据部门 ID 统计用户数
     * @param deptId
     * @return
     */
    int countByDeptId(int deptId);

    /**
     * 根据部门 ID 查找用户列表
     * @param deptId
     * @param page
     * @return
     */
    List<SysUser> getPageByDeptId(@Param("deptId") int deptId, @Param("page") PageQuery page);

    /**
     * 根据 用户ID列表查找用户列表
     * @param idList
     * @return
     */
    List<SysUser> getByIdList(List<Integer> idList);

    /**
     * 获取所有用户
     * @return
     */
    List<SysUser> getAll();

}