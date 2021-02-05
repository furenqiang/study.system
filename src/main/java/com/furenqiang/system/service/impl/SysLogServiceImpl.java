package com.furenqiang.system.service.impl;

import com.furenqiang.system.common.ResponseEnum;
import com.furenqiang.system.common.ResponseResult;
import com.furenqiang.system.entity.SysLog;
import com.furenqiang.system.mapper.SysLogMapper;
import com.furenqiang.system.service.SysLogService;
import com.furenqiang.system.vo.SysLogCountVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SysLogServiceImpl implements SysLogService {

    @Autowired
    SysLogMapper sysLogMapper;

    @Override
    public ResponseResult getLogListByParams(String username, String operation, String ip, Integer orderField, Integer orderType, int page, int size) {
        PageHelper.startPage(page, size);
        List<SysLog> logListByParams = sysLogMapper.getLogListByParams(username, operation, ip, orderField,orderType);
        PageInfo<SysLog> pageInfo = new PageInfo<>(logListByParams);
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(ResponseEnum.SUCCESS.getCode());
        responseResult.setMessage(ResponseEnum.SUCCESS.getMessage());
        responseResult.setData(pageInfo.getList());
        responseResult.setTotal(pageInfo.getTotal());
        return responseResult;
    }

    @Override
    public ResponseResult countTimeByParams() {
        //统计近七天每天的请求总时长
        List<SysLogCountVO> sysLogs = sysLogMapper.countTimeByParams();
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(ResponseEnum.SUCCESS.getCode());
        responseResult.setMessage(ResponseEnum.SUCCESS.getMessage());
        responseResult.setData(sysLogs);
        return responseResult;
    }
}
