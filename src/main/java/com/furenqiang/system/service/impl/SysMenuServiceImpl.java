package com.furenqiang.system.service.impl;

import com.furenqiang.system.common.ResponseEnum;
import com.furenqiang.system.common.ResponseResult;
import com.furenqiang.system.entity.SysMenu;
import com.furenqiang.system.mapper.SysMenuMapper;
import com.furenqiang.system.service.SysMenuService;
import com.furenqiang.system.vo.MenuIndexAndCode;
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
            if(smtv.getHasChild()==1||smtv.getLevel()==1){
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

    @Override
    public ResponseResult addMenu(SysMenu sysMenu) {
        ResponseResult res=new ResponseResult();
        if(sysMenu.getParentId()==null||sysMenu.getParentId()==0){
            Integer index=sysMenuMapper.getNewIndex();
            sysMenu.setIndex(String.valueOf(index));
            sysMenu.setCode(index);
        }else{
            MenuIndexAndCode indexByParentId = sysMenuMapper.getIndexByParentId(sysMenu.getParentId());
            sysMenu.setIndex(indexByParentId.getNewIndex());
            sysMenu.setCode(indexByParentId.getNewCode());
        }
        int insert=sysMenuMapper.addMenu(sysMenu);
        res.setMessage(ResponseEnum.SUCCESS.getMessage());
        res.setCode(ResponseEnum.SUCCESS.getCode());
        res.setData(sysMenu);
        res.setTotal((long) insert);
        return res;
    }

    @Override
    public ResponseResult updateMenu(SysMenu sysMenu) {
        ResponseResult res=new ResponseResult();
        int update = sysMenuMapper.updateMenuById(sysMenu);
        res.setMessage(ResponseEnum.SUCCESS.getMessage());
        res.setCode(ResponseEnum.SUCCESS.getCode());
        res.setData(sysMenu);
        res.setTotal((long) update);
        return res;
    }

    @Override
    public ResponseResult deleteMenu(Integer id) {
        ResponseResult res=new ResponseResult();
        List<SysMenu> menuChildrenById = sysMenuMapper.getMenuChildrenById(id);
        if(menuChildrenById.size()>0){
            res.setCode(ResponseEnum.BADPARAM.getCode());
            res.setMessage(ResponseEnum.BADPARAM.getMessage()+"请先删除其子菜单!");
            res.setData(menuChildrenById);
            res.setTotal((long) menuChildrenById.size());
        }else{
            int delete = sysMenuMapper.deleteById(id);
            res.setMessage(ResponseEnum.SUCCESS.getMessage());
            res.setCode(ResponseEnum.SUCCESS.getCode());
            res.setData(id);
            res.setTotal((long) delete);
        }
        return res;
    }
}
