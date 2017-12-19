package com.mmall.service.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.mmall.beans.Mail;
import com.mmall.beans.PageQuery;
import com.mmall.beans.PageResult;
import com.mmall.common.RequestHolder;
import com.mmall.dao.SysUserMapper;
import com.mmall.exception.ParamException;
import com.mmall.model.SysUser;
import com.mmall.param.UserParam;
import com.mmall.service.SysLogService;
import com.mmall.service.SysUserService;
import com.mmall.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author liliang
 * @date 2017/11/27.
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysLogService sysLogService;

    @Autowired
    private ThreadPoolTaskExecutor threadPool;


    @Override
    public void save(UserParam userParam) {
        BeanValidator.check(userParam);
        if (checkTelephoneExist(userParam.getTelephone(), userParam.getId())) {
            throw new ParamException("电话已经被占用!");
        }
        if (checkEmailExist(userParam.getMail(), userParam.getId())) {
            throw new ParamException("邮箱已经被占用!");
        }
        String password = PasswordUtil.randomPassword();
        String encryptedPassword = MD5Util.encrypt(password);
        SysUser sysUser = SysUser.builder()
                .username(userParam.getUsername())
                .telephone(userParam.getTelephone())
                .mail(userParam.getMail())
                .password(encryptedPassword)
                .deptId(userParam.getDeptId())
                .status(userParam.getStatus())
                .remark(userParam.getRemark()).build();
        sysUser.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysUser.setOperatorIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysUser.setOperatorTime(new Date());

        threadPool.execute(() -> MailUtil.send(new Mail("密码分配通知",password, Sets.newHashSet(userParam.getMail()))));


        sysUserMapper.insertSelective(sysUser);

        // 记录日志
        sysLogService.saveUserLog(null, sysUser);
    }

    @Override
    public void update(UserParam userParam) {
        BeanValidator.check(userParam);
        if (checkTelephoneExist(userParam.getTelephone(), userParam.getId())) {
            throw new ParamException("电话已经被占用!");
        }
        if (checkEmailExist(userParam.getMail(), userParam.getId())) {
            throw new ParamException("邮箱已经被占用!");
        }
        SysUser beforeSysUser = sysUserMapper.selectByPrimaryKey(userParam.getId());
        Preconditions.checkNotNull(beforeSysUser, "待更新的用户不存在.");
        SysUser afterSysUser = SysUser.builder()
                .id(userParam.getId())
                .username(userParam.getUsername())
                .telephone(userParam.getTelephone())
                .mail(userParam.getMail())
                .deptId(userParam.getDeptId())
                .status(userParam.getStatus())
                .remark(userParam.getRemark()).build();
        afterSysUser.setOperator(RequestHolder.getCurrentUser().getUsername());
        afterSysUser.setOperatorIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        afterSysUser.setOperatorTime(new Date());
        sysUserMapper.updateByPrimaryKeySelective(afterSysUser);
        // 记录日志
        sysLogService.saveUserLog(beforeSysUser, afterSysUser);
    }

    public boolean checkEmailExist(String mail, Integer userId) {
        return sysUserMapper.countByMail(mail, userId) > 0;
    }

    public boolean checkTelephoneExist(String telephone, Integer userId) {
        return sysUserMapper.countByTelephone(telephone, userId) > 0;
    }

    @Override
    public SysUser findByKeyWord(String keyword) {
        return sysUserMapper.findByKeyWord(keyword);
    }

    @Override
    public PageResult<SysUser> getPageByDeptId(int deptId, PageQuery pageQuery) {
        BeanValidator.check(pageQuery);
        int count = sysUserMapper.countByDeptId(deptId);
        if (count > 0) {
            List<SysUser> userList = sysUserMapper.getPageByDeptId(deptId, pageQuery);
            return PageResult.<SysUser>builder().total(count).data(userList).build();
        }
        return PageResult.<SysUser>builder().build();
    }

    @Override
    public List<SysUser> getAll() {
        return sysUserMapper.getAll();
    }


}
