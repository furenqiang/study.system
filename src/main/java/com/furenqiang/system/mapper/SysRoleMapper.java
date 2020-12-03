package com.furenqiang.system.mapper;

import com.furenqiang.system.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysRoleMapper {

    @Select("select sr.id,sr.rolename roleName,sr.roledesc roleDesc " +
            "from sys_role sr,sys_user_role sur where sr.id=sur.rid and sur.uid=#{uid}")
    public List<SysRole> findByUid(Integer uid);
}
