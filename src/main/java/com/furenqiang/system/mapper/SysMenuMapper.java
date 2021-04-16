package com.furenqiang.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.furenqiang.system.entity.SysMenu;
import com.furenqiang.system.vo.MenuIndexAndCode;
import com.furenqiang.system.vo.SysMenuTreeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<SysMenuTreeVO> getMenuList();

    List<SysMenu> getMenuChildrenById(@Param("id") Integer id);

    int updateMenuById(SysMenu sysMenu);

    int addMenu(SysMenu sysMenu);

    MenuIndexAndCode getIndexByParentId(@Param("parentId") int parentId);

    Integer getNewIndex();
}
