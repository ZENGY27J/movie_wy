package com.stylefeng.guns.rest.modular.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * @Program: movie_wy
 * @Author: ZyEthan
 * @Description: 缓存类，提供put和get方法
 * @Date: 2019-10-23-14:15
 */
@Component
public class CacheService {

    private Cache<String,Object> cache;

    @PostConstruct
    public void init() {
        cache = CacheBuilder.newBuilder()
                .initialCapacity(10)    // 初始化容量
                .maximumSize(100)       // 达到最大容量后会根据lru算法进行淘汰
                .expireAfterWrite(30, TimeUnit.SECONDS)
                .build();
    }

    /**
     * 存数据
     * @param key
     * @param object
     */
    public void set(String key, Object object) {
        cache.put(key,object);
    }

    /**
     * 取数据
     * @param key
     * @return
     */
    public Object get(String key) {
        return cache.getIfPresent(key);
    }
}
