package com.furenqiang.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.furenqiang.system.entity.Test;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TestMapper extends BaseMapper<Test> {
    List<Test> getTest();
}
