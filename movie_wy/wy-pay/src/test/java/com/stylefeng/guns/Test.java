package com.stylefeng.guns;

import com.stylefeng.guns.rest.common.persistence.service.impl.PayServiceImpl;

public class Test {

    @org.junit.Test
    public void test1(){
        PayServiceImpl payService = new PayServiceImpl();
        payService.getPayInfo("sadw");
    }
}
