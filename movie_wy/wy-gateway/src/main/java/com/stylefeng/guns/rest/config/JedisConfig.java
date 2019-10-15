package com.stylefeng.guns.rest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

/**
 * @Program: movie_wy
 * @Author: ZyEthan
 * @Description: Redis连接配置类
 * @Date: 2019-10-15-15:34
 */
@Configuration
public class JedisConfig {

    @Bean
    public Jedis initJedis() {
        Jedis jedis = new Jedis("127.0.0.1",6379);
        return jedis;
    }
}
