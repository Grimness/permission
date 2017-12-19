package com.mmall.service.impl;

import com.google.common.base.Preconditions;
import com.mmall.common.RequestHolder;
import com.mmall.dao.SysAclMapper;
import com.mmall.dao.SysAclModuleMapper;
import com.mmall.exception.ParamException;
import com.mmall.model.SysAclModule;
import com.mmall.param.AclModuleParam;
import com.mmall.service.SysAclModuleService;
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
public class SysAclModuleServiceImpl implements SysAclModuleService {

    @Resource
    private SysAclModuleMapper sysAclModuleMapper;

    @Resource
    private SysAclMapper sysAclMapper;

    @Autowired
    private SysLogService sysLogService;

    @Override
    public void save(AclModuleParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("同意层级下存在相同名称的权限模块");
        }
        SysAclModule sysAclModule = SysAclModule.builder()
                .name(param.getName())
                .parentId(param.getParentId())
                .seq(param.getSeq())
                .status(param.getStatus())
                .remark(param.getRemark()).build();

        sysAclModule.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
        sysAclModule.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysAclModule.setOperatorIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysAclModule.setOperatorTime(new Date());
        sysAclModuleMapper.insertSelective(sysAclModule);
        // 记录日志
        sysLogService.saveAclModuleLog(null,sysAclModule);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AclModuleParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("同意层级下存在相同名称的权限模块");
        }
        SysAclModule beforeSysAclModule = sysAclModuleMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(beforeSysAclModule, "待更新的权限模块不存在");
        SysAclModule afterSysAclModule = SysAclModule.builder()
                .id(param.getId())
                .name(param.getName())
                .parentId(param.getParentId())
                .seq(param.getSeq())
                .status(param.getStatus())
                .remark(param.getRemark()).build();

        afterSysAclModule.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
        afterSysAclModule.setOperator(RequestHolder.getCurrentUser().getUsername());
        afterSysAclModule.setOperatorIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        afterSysAclModule.setOperatorTime(new Date());

        updateWithChild(beforeSysAclModule, afterSysAclModule);
        // 记录日志
        sysLogService.saveAclModuleLog(beforeSysAclModule, afterSysAclModule);
    }

    private void updateWithChild(SysAclModule beforeSysAclModule, SysAclModule afterSysAclModule) {

        String afterSysAclModuleLevel = afterSysAclModule.getLevel();
        String beforeSysAclModuleLevel = beforeSysAclModule.getLevel();

        if (!afterSysAclModuleLevel.equals(beforeSysAclModuleLevel)) {

            List<SysAclModule> sysAclModuleList = sysAclModuleMapper.getChildAclModuleListByLevel(beforeSysAclModuleLevel);

            if (CollectionUtils.isNotEmpty(sysAclModuleList)) {

                for (SysAclModule sysAclModule : sysAclModuleList) {

                    String level = sysAclModule.getLevel();

                    if (level.indexOf(beforeSysAclModuleLevel) == 0) {

                        level = afterSysAclModuleLevel + level.substring(beforeSysAclModuleLevel.length());

                        sysAclModule.setLevel(level);
                    }
                }

                sysAclModuleMapper.batchUpdateLevel(sysAclModuleList);
            }
        }

        sysAclModuleMapper.updateByPrimaryKeySelective(afterSysAclModule);

    }

    private boolean checkExist(Integer parentId, String aclModuleName, Integer aclModuleId) {
        return sysAclModuleMapper.countByNameAndParentId(parentId, aclModuleName, aclModuleId) > 0;
    }


    private String getLevel(Integer aclModuleId) {
        SysAclModule sysAclModule = sysAclModuleMapper.selectByPrimaryKey(aclModuleId);
        if (sysAclModule == null) {
            return null;
        }
        return sysAclModule.getLevel();
    }

    @Override
    public void delete(int id) {
        SysAclModule sysAclModule = sysAclModuleMapper.selectByPrimaryKey(id);
        Preconditions.checkNotNull(sysAclModule, "要删除的权限模块不存在,无法删除");
        // 权限模块下是否有子模块
        if (sysAclModuleMapper.countByParentId(sysAclModule.getId()) > 0) {
            throw new ParamException("当前权限模块下有子模块,无法删除");
        }
        // 权限模块下是否有权限点
        if (sysAclMapper.countByAclModuleId(sysAclModule.getId()) > 0) {
            throw new ParamException("当前权限下面有权限点,无法删除");
        }
        sysAclModuleMapper.deleteByPrimaryKey(id);
    }
}
