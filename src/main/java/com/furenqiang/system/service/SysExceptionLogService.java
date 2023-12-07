package com.furenqiang.system.service;

import com.furenqiang.system.common.ResponseResult;

public interface SysExceptionLogService {

    ResponseResult getExceptionLogListByParams(String username, String excName, String method, String ip, int page, int size);

    ResponseResult countExceptByParams();

    ResponseResult countExceptTop3();
}
