package com.furenqiang.system.service;

import com.furenqiang.system.common.ResponseResult;

public interface SysLogService {

    ResponseResult getLogListByParams(String username, String operation, String ip, Integer orderField, Integer orderType, int page, int size);

    ResponseResult countTimeByParams();
}
