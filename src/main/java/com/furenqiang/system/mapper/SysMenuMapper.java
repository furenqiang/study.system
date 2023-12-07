package com.furenqiang.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.furenqiang.system.entity.SysMenu;
import com.furenqiang.system.vo.SysMenuTreeVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<SysMenuTreeVO> getMenuList();
}
