package com.furenqiang.system.mapper;

import com.furenqiang.system.entity.Test;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TestMapper {
    List<Test> getTest();
}
