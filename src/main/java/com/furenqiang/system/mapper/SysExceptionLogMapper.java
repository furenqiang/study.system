package com.furenqiang.system.mapper;

import com.furenqiang.system.entity.SysExceptionLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysExceptionLogMapper {

    int addExceptLog(SysExceptionLog sysExceptionLog);

    List<SysExceptionLog> getExceptionLogListByParams(@Param("username") String username, @Param("excName") String excName, @Param("method") String method, @Param("ip") String ip);
}
