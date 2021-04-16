package com.furenqiang.system.service;

import com.furenqiang.system.common.ResponseResult;
import com.furenqiang.system.entity.SysMenu;

public interface SysMenuService {

    ResponseResult getMenuTree();

    ResponseResult addMenu(SysMenu sysMenu);

    ResponseResult updateMenu(SysMenu sysMenu);

    ResponseResult deleteMenu(Integer id);

}
