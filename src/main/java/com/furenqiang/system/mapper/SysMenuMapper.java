package com.furenqiang.system.mapper;

import com.furenqiang.system.vo.SysMenuTreeVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysMenuMapper {

    List<SysMenuTreeVO> getMenuList();
}
