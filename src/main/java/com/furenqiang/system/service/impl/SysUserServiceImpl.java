package com.furenqiang.system.service.impl;

import com.furenqiang.system.common.ResponseEnum;
import com.furenqiang.system.common.ResponseResult;
import com.furenqiang.system.entity.SysRole;
import com.furenqiang.system.entity.SysUser;
import com.furenqiang.system.mapper.SysRoleMapper;
import com.furenqiang.system.mapper.SysUserMapper;
import com.furenqiang.system.service.SysUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    SysUserMapper sysUserMapper;

    @Autowired
    SysRoleMapper sysRoleMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return sysUserMapper.findByName(s);
    }

    @Override
    public ResponseResult registerUser(String username, String password, Integer creatorId, String creatorName) {
        SysUser byName = sysUserMapper.findByName(username);
        if (null != byName) {
            return new ResponseResult(ResponseEnum.SUCCESS.getCode(), "用户" + username + "已存在！", ResponseEnum.SUCCESS.getMessage());
        }
        if (null == password || "".equals(password)) {
            password = "123456";
        }
        SysUser sysUser=new SysUser(username, BCrypt.hashpw(password, BCrypt.gensalt()), creatorId, creatorName);
        int insert = sysUserMapper.registerUser(sysUser);
        if (insert > 0) {
            //给新建的用户自动添加查询权限
            int i = sysUserMapper.addSelectRole(sysUser.getId());
            if(i>0){
                return new ResponseResult(ResponseEnum.SUCCESS.getCode(), "添加用户" + username + "成功！", ResponseEnum.SUCCESS.getMessage());
            }else {
                return new ResponseResult(ResponseEnum.SUCCESS.getCode(), "添加用户" + username + "成功！授予查询权限失败！",
                        ResponseEnum.SUCCESS.getMessage());
            }
        }
        return new ResponseResult(ResponseEnum.ERROR.getCode(), "添加用户" + username + "失败！", ResponseEnum.ERROR.getMessage());
    }

    @Override
    public ResponseResult getUserListByParams(String username, String creatorName, int page, int size) {
        PageHelper.startPage(page, size);
        List<SysUser> userListByParams = sysUserMapper.getUserListByParams(username, creatorName);
        PageInfo<SysUser> pageInfo = new PageInfo<>(userListByParams);
        userListByParams.stream().forEach((sysUser) -> {
            List<SysRole> byUid = sysRoleMapper.findByUid(sysUser.getId());
            sysUser.setRoles(byUid);
        });
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(ResponseEnum.SUCCESS.getCode());
        responseResult.setMessage(ResponseEnum.SUCCESS.getMessage());
        responseResult.setData(pageInfo.getList());
        responseResult.setTotal(pageInfo.getTotal());
        return responseResult;
    }

    @Override
    public ResponseResult deleteUser(int id) {
        int i = sysUserMapper.changeStatus(id, 0);
        ResponseResult responseResult = new ResponseResult();
        if (i > 0) {
            return new ResponseResult(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMessage());
        } else {
            return new ResponseResult(ResponseEnum.ERROR.getCode(), ResponseEnum.ERROR.getMessage());
        }
    }

    @Override
    public ResponseResult updateUser(int id, String password) {
        int i = sysUserMapper.updateUser(id, BCrypt.hashpw(password, BCrypt.gensalt()));
        if (i > 0) {
            return new ResponseResult(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMessage());
        } else {
            return new ResponseResult(ResponseEnum.ERROR.getCode(), ResponseEnum.ERROR.getMessage());
        }
    }

}
