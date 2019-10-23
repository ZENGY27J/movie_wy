package com.stylefeng.guns.rest.config.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


/**
 * @author: jia.xue
 * @create: 2019-06-24 14:47
 * @Description
 **/
@Configuration
public class RedisConfig{

    private transient static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);
    /**
     *
     * @param
     * @return
     */
    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){

        /**设置序列化*/
        Jackson2JsonRedisSerializer jacksonredisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL,JsonAutoDetect.Visibility.ANY);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jacksonredisSerializer.setObjectMapper(mapper);
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        /**为redisTemplate 配置相应的序列化方式*/
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);


        redisTemplate.setValueSerializer(jacksonredisSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(jacksonredisSerializer);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
