package com.furenqiang.system.filter;

/*
* @Author Eric
* */
public class PredicateParams implements MyPredicate {
    @Override
    public boolean predicateParam(Object... os) {
        for (Object o:os){
            if(null==o){
                return true;
            }
        }
        return false;
    }
}
