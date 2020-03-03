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

import com.hpb.bc.entity.cache.CachePacketParam;
import org.apache.ibatis.cache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.List;


@Configuration
@EnableCaching
@EnableAsync
public class CachePacketConfiguration {

    public static final String VALUE_CACHE_NAME = "packetVlaues";
    public static final String KEY_CACHE_NAME = "packetKeys";
    public static final String CACHE_KEY_PREFIX = "'keyPrefix'";
    private static final Logger log = LoggerFactory.getLogger(CachePacketConfiguration.class);

    // 更新packetKeys缓存数据:key=UUIDGeneratorUtil.generate(CachePacketParam).toLowerCase()
    @CachePut(value = KEY_CACHE_NAME, key = CACHE_KEY_PREFIX)
    public List<String> updateDrawKeys(List<String> keys) throws CacheException {
        log.info("更新缓存[缓存名称：{},key：{}]", KEY_CACHE_NAME, CACHE_KEY_PREFIX);
        return keys;
    }

    @CachePut(value = VALUE_CACHE_NAME, key = "#cache.uid")
    public CachePacketParam updateDrawValues(CachePacketParam cache) throws CacheException {
        log.info("更新缓存[缓存名称：{},key：{}]", VALUE_CACHE_NAME, cache.getUid());
        return cache;
    }

    @Cacheable(value = KEY_CACHE_NAME, key = CACHE_KEY_PREFIX)
    public List<String> findAllPacketKey() {
        log.info("没有走缓存[{}{}]", CACHE_KEY_PREFIX);
        return null;
    }
}
