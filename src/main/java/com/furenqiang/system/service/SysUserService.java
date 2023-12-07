package com.furenqiang.system.service;

import com.furenqiang.system.common.ResponseResult;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface SysUserService extends UserDetailsService {

    ResponseResult registerUser(String username, String password, Integer creatorId, String creatorName);

    ResponseResult getUserListByParams(String username, String creatorName, int page, int size);

    ResponseResult deleteUser(int id);

    ResponseResult updateUser(int id, String password);
}
