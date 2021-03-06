package com.stylefeng.guns.rest.modular.auth.util;


import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @Program: movie_wy
 * @Author: ZyEthan
 * @Description: 操作json和bean转换方法
 * @Date: 2019-10-16-12:23
 */
public class Json2BeanUtils {
    /*
     * 001.json转换成对象
     * @param:传入对象，json字符串
     * @return:Object
     */
    public static Object jsonToObj(Object obj,String jsonStr) throws JsonParseException,JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        return obj = mapper.readValue(jsonStr, obj.getClass());
    }

    public static Object json2Bean(Object object, String jsonStr) {
        Object o = JSON.parseObject(jsonStr, object.getClass());
        return o;
    }
    /*
     * 002.对象转换成json
     * @param:传入对象
     * @return:json字符串
     */
    public static String objToJson(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }
}
