package com.furenqiang.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;

@TableName("sys_Role") //指定对应数据表
public class SysRole implements GrantedAuthority {

    @TableId(value = "id",type = IdType.AUTO)//指定自增策略
    private Integer id;

    private String roleName;

    private String roleDesc;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    //JsonIgnore的作用是生成toString方法时 忽略此方法的字段
    @JsonIgnore
    @Override
    public String getAuthority() {
        return roleName;
    }
}
