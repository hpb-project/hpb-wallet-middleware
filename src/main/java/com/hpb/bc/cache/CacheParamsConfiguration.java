package com.hpb.bc.cache;

import com.hpb.bc.entity.cache.CacheParam;
import org.apache.ibatis.cache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;


@Configuration
@EnableCaching
@EnableAsync
@CacheConfig(cacheNames = "cacheParams")
public class CacheParamsConfiguration {

    public static final String CACHE_NAME = "cacheParams";
    public static final String CACHE_KEY_PREFIX = "'cacheParam'";
    private static final Logger log = LoggerFactory.getLogger(CacheParamsConfiguration.class);

    @CacheEvict(value = CACHE_NAME, key = CACHE_KEY_PREFIX + "+#proccessId")
    public void delete(String proccessId) {

    }

    @CachePut(value = CACHE_NAME, key = CACHE_KEY_PREFIX + "+#cacheParam.getProccessId()")
    public CacheParam update(CacheParam cacheParam) throws CacheException {
        log.info("更新缓存[{}-{}]", CACHE_KEY_PREFIX, cacheParam.getProccessId());
        return cacheParam;
    }

}