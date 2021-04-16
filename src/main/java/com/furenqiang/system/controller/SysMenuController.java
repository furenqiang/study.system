package com.furenqiang.system.controller;

import com.furenqiang.system.aop.Log;
import com.furenqiang.system.common.ResponseResult;
import com.furenqiang.system.entity.SysMenu;
import com.furenqiang.system.entity.SysUser;
import com.furenqiang.system.service.SysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/sysMenu")
@Api(tags = "菜单相关功能")
public class SysMenuController {

    @Autowired
    SysMenuService sysMenuService;

    /**
     * @return
     * @Description 获取菜单树
     * @Params
     * @Time 2021年3月11日
     * @Author Eric
     */
    //@Log("查询系统日志列表")
    @PreAuthorize("hasAnyAuthority('vip','sysUser','select')")
    @ApiOperation(value = "获取菜单树", httpMethod = "GET")
    //@ApiImplicitParams({@ApiImplicitParam(name = "username", value = "操作人", dataType = "String")})
    @GetMapping("/getMenuTree")
    public ResponseResult getMenuTree(HttpServletRequest request) {
        //先看session是否有存，没有再去查，后面往redis存
        ResponseResult menuTreeBySession = (ResponseResult) request.getSession().getAttribute("menuTree");
        if(menuTreeBySession==null){
            ResponseResult menuTree = sysMenuService.getMenuTree();
            request.getSession().setAttribute("menuTree",menuTree);
            return menuTree;
        }else {
            return menuTreeBySession;
        }
    }

    /**
     * @return
     * @Description 添加菜单
     * @Params
     * @Time 2021年4月13日
     * @Author Eric
     */
    @Log("添加菜单")
    @PreAuthorize("hasAnyAuthority('vip','sysUser')")
    @ApiOperation(value = "添加菜单", httpMethod = "POST")
    @PostMapping("/addMenu")
    public ResponseResult addMenu(HttpServletRequest request,SysMenu sysMenu) {
        SysUser userInfo = (SysUser) request.getSession().getAttribute("userInfo");
        Integer creatorId = userInfo.getId();
        String creatorName = userInfo.getUsername();
        sysMenu.setCreatorId(creatorId);
        sysMenu.setCreatorName(creatorName);
        ResponseResult responseResult = sysMenuService.addMenu(sysMenu);
        request.getSession().setAttribute("menuTree",null);
        return responseResult;
    }

    /**
     * @return
     * @Description 修改菜单
     * @Params
     * @Time 2021年4月13日
     * @Author Eric
     */
    @Log("修改菜单")
    @PreAuthorize("hasAnyAuthority('vip','sysUser')")
    @ApiOperation(value = "修改菜单", httpMethod = "POST")
    @PostMapping("/updateMenu")
    public ResponseResult updateMenu(HttpServletRequest request,SysMenu sysMenu) {
        ResponseResult responseResult = sysMenuService.updateMenu(sysMenu);
        request.getSession().setAttribute("menuTree",null);
        return responseResult;
    }

    /**
     * @return
     * @Description 删除菜单
     * @Params
     * @Time 2021年4月13日
     * @Author Eric
     */
    @Log("删除菜单")
    @PreAuthorize("hasAnyAuthority('vip','sysUser')")
    @ApiOperation(value = "删除菜单", httpMethod = "POST")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "菜单ID", dataType = "String")})
    @PostMapping("/deleteMenu")
    public ResponseResult deleteMenu(Integer id) {
        return sysMenuService.deleteMenu(id);
    }
}
