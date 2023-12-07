package com.furenqiang.system.service.impl;

import com.furenqiang.system.common.ResponseEnum;
import com.furenqiang.system.common.ResponseResult;
import com.furenqiang.system.entity.SysExceptionLog;
import com.furenqiang.system.mapper.SysExceptionLogMapper;
import com.furenqiang.system.service.SysExceptionLogService;
import com.furenqiang.system.vo.SysExceptionLogCountVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SysExceptionLogServiceImpl implements SysExceptionLogService {

    @Autowired
    SysExceptionLogMapper sysExceptionLogMapper;

    @Override
    public ResponseResult getExceptionLogListByParams(String username, String excName, String method, String ip, int page, int size) {
        PageHelper.startPage(page, size);
        List<SysExceptionLog> exceptionLogListByParams = sysExceptionLogMapper.getExceptionLogListByParams(username, excName, method, ip);
        PageInfo<SysExceptionLog> pageInfo = new PageInfo<>(exceptionLogListByParams);
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(ResponseEnum.SUCCESS.getCode());
        responseResult.setMessage(ResponseEnum.SUCCESS.getMessage());
        responseResult.setData(pageInfo.getList());
        responseResult.setTotal(pageInfo.getTotal());
        return responseResult;
    }

    @Override
    public ResponseResult countExceptByParams() {
        //统计近七次每天的异常数
        List<SysExceptionLogCountVO> sysExceptionLogs = sysExceptionLogMapper.countExceptByParams();
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(ResponseEnum.SUCCESS.getCode());
        responseResult.setMessage(ResponseEnum.SUCCESS.getMessage());
        responseResult.setData(sysExceptionLogs);
        return responseResult;
    }

    @Override
    public ResponseResult countExceptTop3() {
        //统计异常类型TOP3
        List<SysExceptionLogCountVO> sysExceptionLogs = sysExceptionLogMapper.countExceptTop3();
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(ResponseEnum.SUCCESS.getCode());
        responseResult.setMessage(ResponseEnum.SUCCESS.getMessage());
        responseResult.setData(sysExceptionLogs);
        return responseResult;
    }
}
