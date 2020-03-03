/*
 * Copyright 2020 HPB Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hpb.bc.cache;

import com.hpb.bc.entity.cache.CacheSession;
import org.apache.ibatis.cache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.*;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
@CacheConfig(cacheNames = "cacheSessions")
public class CacheSessionConfiguration {

    public static final String CACHE_NAME = "cacheSessions";
    public static final String CACHE_KEY_PREFIX = "'cacheSession'";
    private static final Logger log = LoggerFactory.getLogger(CacheSessionConfiguration.class);

    @CacheEvict(value = CACHE_NAME, key = CACHE_KEY_PREFIX + "+#sessionId") // 这是清除缓存
    public void delete(String sessionId) {

    }

    @CachePut(value = CACHE_NAME, key = CACHE_KEY_PREFIX + "+#cacheSession.getSessionId()")
    public CacheSession update(CacheSession cacheSession) throws CacheException {
        log.info("更新缓存！[{}-{}]", CACHE_KEY_PREFIX, cacheSession.getSessionId());
        return cacheSession;
    }

    @Cacheable(value = CACHE_NAME, key = CACHE_KEY_PREFIX + "+#sessionId")
    public CacheSession findBySessionId(String sessionId) {
        log.info("没有走缓存！[{}-{}]", CACHE_KEY_PREFIX, sessionId);
        CacheSession cacheSession = new CacheSession();
        return cacheSession;
    }

}