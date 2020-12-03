package com.furenqiang.system.mapper;

import com.furenqiang.system.entity.SysUser;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

@Mapper
public interface SysUserMapper {

    @Select("select * from sys_user where username=#{username}")
    @Results({
            @Result(id = true, column = "id", property = "id", jdbcType = JdbcType.INTEGER),
            @Result(column = "username", property = "username", jdbcType = JdbcType.VARCHAR),
            @Result(column = "password", property = "password", jdbcType = JdbcType.VARCHAR),
            @Result(column = "id", property = "roles", javaType = List.class,
                    many = @Many(select = "com.furenqiang.system.mapper.SysRoleMapper.findByUid"))
    })
    public SysUser findByName(String username);
}
