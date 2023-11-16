package com.furenqiang.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.furenqiang.system.entity.Test;

import java.util.List;

public interface TestService extends IService<Test> {

    public List<Test> getTest();
}
