package com.stylefeng.guns.rest.modular.auth.util;

import com.alibaba.dubbo.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.io.*;

/**
 * @Program: movie_wy
 * @Author: ZyEthan
 * @Description: 播放厅座位信息
 * @Date: 2019-10-16-17:33
 */
public class SeatsInfoUtils {

    @Autowired
    private Jedis jedis;

    // 1.读取文件
    public static String getJsonString(String path) throws IOException {
        String filepath = "C:\\json\\" + path;
        File file = new File(filepath);                    //定义一个file对象，用来初始化FileReader
        FileReader reader = new FileReader(file);          //定义一个fileReader对象，用来初始化BufferedReader
        BufferedReader bReader = new BufferedReader(reader);//new一个BufferedReader对象，将文件内容读取到缓存
        StringBuilder sb = new StringBuilder();             //定义一个字符串缓存，将字符串存放缓存中
        String s = "";
        while ((s =bReader.readLine()) != null) {           //逐行读取文件内容，不读取换行符和末尾的空格
            sb.append(s + "\n");                            //将读取的字符串添加换行符后累加存放在缓存中
            System.out.println(s);
        }
        bReader.close();
        String strJson = sb.toString();
        System.out.println(strJson );
        return strJson;
    }

    // 2.将各个信息存储到redis中
    public void insertRedis(String path, String strJson) {
        if (StringUtils.isBlank(jedis.get(path))) {
            jedis.set(path,strJson);
            jedis.expire(path, 3600);
        }
        jedis.expire(path, 3600);    }
}
