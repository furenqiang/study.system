package com.furenqiang.system.mapper;

import com.furenqiang.system.entity.SysLog;
import com.furenqiang.system.vo.SysLogCountVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysLogMapper {

    int addSysLog(SysLog sysLog);

    List<SysLog> getLogListByParams(@Param("username") String username, @Param("operation") String operation, @Param("ip") String ip, @Param("orderField") Integer orderField, @Param("orderType") Integer orderType);

    List<SysLogCountVO> countTimeByParams();

}
