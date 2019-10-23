package com.stylefeng.guns.fastjson;

import com.alibaba.fastjson.JSON;
import com.stylefeng.guns.core.util.MD5Util;
import com.stylefeng.guns.rest.common.SimpleObject;
import com.stylefeng.guns.rest.modular.auth.converter.BaseTransferEntity;
import org.junit.Test;

import java.util.Arrays;

/**
 * json测试
 *
 * @author fengshuonan
 * @date 2017-08-25 16:11
 */


public class JsonTest {
    @Test
    public void myTest(){
        String string = "#3#4#18#";
        String[] parts = string.split("#");
        System.out.println(Arrays.toString(parts));
        for (String part : parts) {
            System.out.println(part);
        }
    }


    public static void main(String[] args) {
        String randomKey = "1xm7hw";

        BaseTransferEntity baseTransferEntity = new BaseTransferEntity();
        SimpleObject simpleObject = new SimpleObject();
        simpleObject.setUser("fsn");
        baseTransferEntity.setObject("123123");

        String json = JSON.toJSONString(simpleObject);

        //md5签名
        String encrypt = MD5Util.encrypt(json + randomKey);
        baseTransferEntity.setSign(encrypt);

        System.out.println(JSON.toJSONString(baseTransferEntity));
    }
}


