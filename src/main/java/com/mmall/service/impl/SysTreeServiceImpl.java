package com.mmall.service.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.mmall.dao.SysAclMapper;
import com.mmall.dao.SysAclModuleMapper;
import com.mmall.dao.SysDeptMapper;
import com.mmall.dto.AclDto;
import com.mmall.dto.AclModuleLevelDto;
import com.mmall.dto.DeptLevelDto;
import com.mmall.model.SysAcl;
import com.mmall.model.SysAclModule;
import com.mmall.model.SysDept;
import com.mmall.service.SysCoreService;
import com.mmall.service.SysTreeService;
import com.mmall.util.LevelUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author liliang
 * @date 2017/11/27.
 */
@Service
public class SysTreeServiceImpl implements SysTreeService {

    @Resource
    private SysDeptMapper sysDeptMapper;

    @Resource
    private SysAclModuleMapper sysAclModuleMapper;

    @Resource
    private SysAclMapper sysAclMapper;

    @Autowired
    private SysCoreService sysCoreService;

    @Override
    public List<AclModuleLevelDto> userAclTree(int userId) {
        // 1,当前用户已分配的权限点
        List<SysAcl> userAclList = sysCoreService.getUserAclList(userId);
        List<AclDto> aclDtoList = Lists.newArrayList();
        for (SysAcl acl : userAclList) {
            AclDto dto = AclDto.adapt(acl);
            dto.setHasAcl(true);
            dto.setChecked(true);
            aclDtoList.add(dto);
        }
        return aclListToTree(aclDtoList);
    }

    @Override
    public List<AclModuleLevelDto> roleTree(int roleId) {
        // 1,当前用户已分配的权限点
        List<SysAcl> userAclList = sysCoreService.getCurrentUserAclList();
        // 2,当前角色分配的权限点
        List<SysAcl> roleAclList = sysCoreService.getRoleAclList(roleId);
        // 3,当前系统所有权限点
        List<AclDto> aclDtoList = Lists.newArrayList();

        Set<Integer> userAclIdSet = userAclList.stream().map(sysAcl -> sysAcl.getId()).collect(Collectors.toSet());
        Set<Integer> roleAclIdSet = roleAclList.stream().map(sysAcl -> sysAcl.getId()).collect(Collectors.toSet());

        List<SysAcl> allAclList = sysAclMapper.getAll();
        for (SysAcl acl : allAclList) {
            AclDto dto = AclDto.adapt(acl);
            if (userAclIdSet.contains(acl.getId())) {
                dto.setHasAcl(true);
            }
            if (roleAclIdSet.contains(acl.getId())) {
                dto.setChecked(true);
            }
            aclDtoList.add(dto);
        }
        return aclListToTree(aclDtoList);
    }

    public List<AclModuleLevelDto> aclListToTree(List<AclDto> aclDtoList) {
        if (CollectionUtils.isEmpty(aclDtoList)) {
            return Lists.newArrayList();
        }
        List<AclModuleLevelDto> aclModuleLevelList = aclModuleTree();

        Multimap<Integer, AclDto> moduleIdAclMap = ArrayListMultimap.create();
        for (AclDto aclDto : aclDtoList) {
            if (aclDto.getStatus() == 1) {
                moduleIdAclMap.put(aclDto.getAclModuleId(), aclDto);
            }
        }
        bindAclsWithOrder(aclModuleLevelList, moduleIdAclMap);
        return aclModuleLevelList;
    }

    public void bindAclsWithOrder(List<AclModuleLevelDto> aclModuleLevelList, Multimap<Integer, AclDto> moduleIdAclMap) {
        if (CollectionUtils.isEmpty(aclModuleLevelList)) {
            return;
        }
        for (AclModuleLevelDto dto : aclModuleLevelList) {
            List<AclDto> aclDtoList = (List<AclDto>)moduleIdAclMap.get(dto.getId());
            if (CollectionUtils.isNotEmpty(aclDtoList)){
                Collections.sort(aclDtoList,aclSeqComparator);
                dto.setAclList(aclDtoList);
            }
            bindAclsWithOrder(dto.getAclModuleList(),moduleIdAclMap);
        }
    }

    @Override
    public List<AclModuleLevelDto> aclModuleTree() {
        List<SysAclModule> aclModuleList = sysAclModuleMapper.getAllAclModule();
        List<AclModuleLevelDto> aclModuleLevelDtoList = Lists.newArrayList();
        for (SysAclModule sysAclModule : aclModuleList) {
            AclModuleLevelDto aclModuleLevelDto = AclModuleLevelDto.adept(sysAclModule);
            aclModuleLevelDtoList.add(aclModuleLevelDto);
        }
        return aclMOduleListToTree(aclModuleLevelDtoList);
    }

    /**
     * 将权限模块列表转成树
     *
     * @param aclModuleLevelDtoList
     * @return
     */
    public List<AclModuleLevelDto> aclMOduleListToTree(List<AclModuleLevelDto> aclModuleLevelDtoList) {
        if (CollectionUtils.isEmpty(aclModuleLevelDtoList)) {
            return Lists.newArrayList();
        }
        // level -> [aclModule1,aclModule1,...]  Multimap : Map<String,List<Object>> 结构
        Multimap<String, AclModuleLevelDto> levelDtoMultimap = ArrayListMultimap.create();
        List<AclModuleLevelDto> rootList = Lists.newArrayList();
        for (AclModuleLevelDto dto : aclModuleLevelDtoList) {
            levelDtoMultimap.put(dto.getLevel(), dto);
            if (LevelUtil.ROOT.equals(dto.getLevel())) {
                rootList.add(dto);
            }
        }
        // 按照 seq从小到大排序
        Collections.sort(rootList, aclModuleSeqComparator);
        // 生成树到 aclModuleLevelDto 中
        transformAclModuleTree(rootList, LevelUtil.ROOT, levelDtoMultimap);
        return rootList;
    }

    /**
     * 递归生成树
     *
     * @param aclModuleLevelDtoList
     * @param level
     * @param levelDtoMultimap
     */
    public void transformAclModuleTree(List<AclModuleLevelDto> aclModuleLevelDtoList, String level,
                                       Multimap<String, AclModuleLevelDto> levelDtoMultimap) {
        for (int i = 0; i < aclModuleLevelDtoList.size(); i++) {
            // 遍历该层的每一个元素
            AclModuleLevelDto aclModuleLevelDto = aclModuleLevelDtoList.get(i);
            // 处理当前层级的数据
            String nextLevel = LevelUtil.calculateLevel(level, aclModuleLevelDto.getId());
            // 处理下一层
            List<AclModuleLevelDto> tempAclModuleList = (List<AclModuleLevelDto>) levelDtoMultimap.get(nextLevel);
            if (CollectionUtils.isNotEmpty(tempAclModuleList)) {
                // 排序
                Collections.sort(tempAclModuleList, aclModuleSeqComparator);
                // 设置下一层部门
                aclModuleLevelDto.setAclModuleList(tempAclModuleList);
                // 进入下一层处理
                transformAclModuleTree(tempAclModuleList, nextLevel, levelDtoMultimap);
            }
        }
    }

    @Override
    public List<DeptLevelDto> deptTree() {
        List<SysDept> deptList = sysDeptMapper.getAllDept();
        List<DeptLevelDto> deptLevelDtoList = Lists.newArrayList();
        for (SysDept sysDept : deptList) {
            DeptLevelDto deptLevelDto = DeptLevelDto.adept(sysDept);
            deptLevelDtoList.add(deptLevelDto);
        }
        return deptListToTree(deptLevelDtoList);
    }

    /**
     * 把 deptLevelDto转成 Tree
     *
     * @param deptLevelDtoList
     * @return
     */
    public List<DeptLevelDto> deptListToTree(List<DeptLevelDto> deptLevelDtoList) {
        if (CollectionUtils.isEmpty(deptLevelDtoList)) {
            return Lists.newArrayList();
        }
        // level -> [dept1,dept2,...]  Multimap : Map<String,List<Object>> 结构
        Multimap<String, DeptLevelDto> levelDtoMultimap = ArrayListMultimap.create();
        List<DeptLevelDto> rootList = Lists.newArrayList();
        for (DeptLevelDto dto : deptLevelDtoList) {
            levelDtoMultimap.put(dto.getLevel(), dto);
            if (LevelUtil.ROOT.equals(dto.getLevel())) {
                rootList.add(dto);
            }
        }
        // 按照 seq从小到大排序
        Collections.sort(rootList, new Comparator<DeptLevelDto>() {
            @Override
            public int compare(DeptLevelDto o1, DeptLevelDto o2) {
                return o1.getSeq() - o2.getSeq();
            }
        });
        // 生成树到 deptLevelDto 中
        transformDeptTree(rootList, LevelUtil.ROOT, levelDtoMultimap);
        return rootList;
    }

    /**
     * 递归生成树
     * level : 0 ,0,all
     *
     * @param deptLevelDtoList
     * @param level
     * @param levelDtoMultimap
     */
    public void transformDeptTree(List<DeptLevelDto> deptLevelDtoList, String level,
                                  Multimap<String, DeptLevelDto> levelDtoMultimap) {
        for (int i = 0; i < deptLevelDtoList.size(); i++) {
            // 遍历该层的每一个元素
            DeptLevelDto deptLevelDto = deptLevelDtoList.get(i);
            // 处理当前层级的数据
            String nextLevel = LevelUtil.calculateLevel(level, deptLevelDto.getId());
            // 处理下一层
            List<DeptLevelDto> tempDeptList = (List<DeptLevelDto>) levelDtoMultimap.get(nextLevel);
            if (CollectionUtils.isNotEmpty(tempDeptList)) {
                // 排序
                Collections.sort(tempDeptList, deptSeqComparator);
                // 设置下一层部门
                deptLevelDto.setDeptList(tempDeptList);
                // 进入下一层处理
                transformDeptTree(tempDeptList, nextLevel, levelDtoMultimap);
            }
        }
    }

    /**
     * DeptLevelDto
     * 根据 seq 进行排序
     *
     * @return
     */
    public Comparator<DeptLevelDto> deptSeqComparator = new Comparator<DeptLevelDto>() {
        @Override
        public int compare(DeptLevelDto o1, DeptLevelDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };

    /**
     * AclModuleLevelDto
     * seq 排序
     */
    public Comparator<AclModuleLevelDto> aclModuleSeqComparator = new Comparator<AclModuleLevelDto>() {
        @Override
        public int compare(AclModuleLevelDto o1, AclModuleLevelDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };

    /**
     * AclDto
     * seq 排序
     */
    public Comparator<AclDto> aclSeqComparator = new Comparator<AclDto>() {
        @Override
        public int compare(AclDto o1, AclDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };
}
