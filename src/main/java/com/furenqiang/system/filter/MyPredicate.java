package com.furenqiang.system.filter;

/**
 * @Author Eric
 * @params 不能为空的参数
 * */
@FunctionalInterface
public interface MyPredicate {

    boolean predicateParam(Object... o);
}
