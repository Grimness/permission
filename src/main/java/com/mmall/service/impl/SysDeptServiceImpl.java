package com.mmall.service.impl;

import com.google.common.base.Preconditions;
import com.mmall.common.RequestHolder;
import com.mmall.dao.SysDeptMapper;
import com.mmall.dao.SysUserMapper;
import com.mmall.exception.ParamException;
import com.mmall.model.SysDept;
import com.mmall.param.DeptParam;
import com.mmall.service.SysDeptService;
import com.mmall.service.SysLogService;
import com.mmall.util.BeanValidator;
import com.mmall.util.IpUtil;
import com.mmall.util.LevelUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author liliang
 * @date 2017/11/27.
 */
@Service
public class SysDeptServiceImpl implements SysDeptService {

    @Resource
    private SysDeptMapper sysDeptMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysLogService sysLogService;

    @Override
    public void save(DeptParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("同意层级下存在相同名称的部门");
        }
        SysDept sysDept = SysDept.builder()
                .name(param.getName())
                .parentId(param.getParentId())
                .seq(param.getSeq())
                .remark(param.getRemark()).build();

        sysDept.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
        sysDept.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysDept.setOperatorIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysDept.setOperatorTime(new Date());
        sysDeptMapper.insertSelective(sysDept);
        // 记录日志
        sysLogService.saveDeptLog(null, sysDept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(DeptParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("同意层级下存在相同名称的部门");
        }
        SysDept beforeSysDept = sysDeptMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(beforeSysDept, "待更新的部门不存在.");
        SysDept afterSysDept = SysDept.builder()
                .id(param.getId())
                .name(param.getName())
                .parentId(param.getParentId())
                .seq(param.getSeq())
                .remark(param.getRemark()).build();
        afterSysDept.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
        afterSysDept.setOperator(RequestHolder.getCurrentUser().getUsername());
        afterSysDept.setOperatorIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        afterSysDept.setOperatorTime(new Date());

        updateWithChild(beforeSysDept, afterSysDept);
        sysLogService.saveDeptLog(beforeSysDept, afterSysDept);
    }

    private void updateWithChild(SysDept beforeSysDept, SysDept afterSysDept) {
        String afterSysDeptLevel = afterSysDept.getLevel();
        String beforeSysDeptLevel = beforeSysDept.getLevel();
        if (!afterSysDeptLevel.equals(beforeSysDeptLevel)) {
            List<SysDept> deptList = sysDeptMapper.getChildDeptListByLevel(beforeSysDeptLevel);
            if (CollectionUtils.isNotEmpty(deptList)) {
                for (SysDept sysDept : deptList) {
                    String level = sysDept.getLevel();
                    if (level.indexOf(beforeSysDeptLevel) == 0) {
                        level = afterSysDeptLevel + level.substring(beforeSysDeptLevel.length());
                        sysDept.setLevel(level);
                    }
                }
                sysDeptMapper.batchUpdateLevel(deptList);
            }
        }
        sysDeptMapper.updateByPrimaryKey(afterSysDept);
    }

    private boolean checkExist(Integer parentId, String deptName, Integer deptId) {
        return sysDeptMapper.countByNameAndParentId(parentId, deptName, deptId) > 0;
    }

    /**
     * 获取 level
     *
     * @param deptId
     * @return
     */
    private String getLevel(Integer deptId) {
        SysDept sysDept = sysDeptMapper.selectByPrimaryKey(deptId);
        if (sysDept == null) {
            return null;
        }
        return sysDept.getLevel();
    }

    @Override
    public void delete(int id) {
        SysDept sysDept = sysDeptMapper.selectByPrimaryKey(id);
        Preconditions.checkNotNull(sysDept, "要删除的部门不存在,无法删除");
        // 是否有子部门
        if (sysDeptMapper.countByParentId(sysDept.getId()) > 0) {
            throw new ParamException("当前部门下面有子部门,无法删除");
        }
        // 部门下是否有用户
        if (sysUserMapper.countByDeptId(sysDept.getId()) > 0) {
            throw new ParamException("当前部门下面有用户,无法删除");
        }
        sysDeptMapper.deleteByPrimaryKey(id);
    }
}
