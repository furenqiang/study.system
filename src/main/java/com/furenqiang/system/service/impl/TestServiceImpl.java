package com.furenqiang.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.furenqiang.system.entity.Test;
import com.furenqiang.system.mapper.TestMapper;
import com.furenqiang.system.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestServiceImpl extends ServiceImpl<TestMapper, Test> implements TestService {

    @Autowired
    TestMapper testMapper;


    @Override
    public List<Test> getTest() {
        List<Test> testList = testMapper.getTest();
        return testList;
    }
}
