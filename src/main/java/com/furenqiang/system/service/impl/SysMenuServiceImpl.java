package com.furenqiang.system.service.impl;

import com.furenqiang.system.common.ResponseEnum;
import com.furenqiang.system.common.ResponseResult;
import com.furenqiang.system.mapper.SysMenuMapper;
import com.furenqiang.system.service.SysMenuService;
import com.furenqiang.system.vo.SysMenuTreeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@Service
@Transactional
public class SysMenuServiceImpl implements SysMenuService {

    @Autowired
    SysMenuMapper sysMenuMapper;

    @Override
    public ResponseResult getMenuTree() {
        //时间复杂度 O(n^2)
        ResponseResult responseResult=new ResponseResult();
        List<SysMenuTreeVO> treeList =new LinkedList<SysMenuTreeVO>();
        List<SysMenuTreeVO> menuList = sysMenuMapper.getMenuList();
        for(SysMenuTreeVO smtv:menuList){
            if(smtv.getHasChildren()==1||smtv.getLevel()==1){
                int smtvId = smtv.getId();
                List<SysMenuTreeVO> childrenList =new LinkedList<SysMenuTreeVO>();
                menuList.forEach((sm)->{
                    if(sm.getParentId()==smtvId){
                        childrenList.add(sm);
                    }
                });
                smtv.setChildren(childrenList);
                treeList.add(smtv);
            }
        }
        responseResult.setCode(ResponseEnum.SUCCESS.getCode());
        responseResult.setTotal((long) treeList.size());
        responseResult.setData(treeList);
        responseResult.setMessage(ResponseEnum.SUCCESS.getMessage());
        return responseResult;
    }
}
