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

package com.hpb.bc.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.*;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    @Autowired
    ClusterConfigurationProperties clusterProperties;

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        connectionFactory.getClusterConnection().clusterGetNodes().forEach(node -> {
            System.out.println(node.getHost() + ":" + node.getPort());
        });
        Set<String> cacheNames = new HashSet<>();
        cacheNames.add("cms");
        cacheNames.add("article");
        cacheNames.add("version");
        cacheNames.add("packetKeys");
        cacheNames.add("packetVlaues");
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig();
        Map<String, RedisCacheConfiguration> configMap = new HashMap<>();

        Duration ttl = Duration.ofDays(clusterProperties.getDuration());
        configMap.put("cms", defaultCacheConfig.entryTtl(ttl));
        configMap.put("article", defaultCacheConfig.entryTtl(ttl));
        configMap.put("version", defaultCacheConfig.entryTtl(ttl));
        configMap.put("packetKeys", defaultCacheConfig.entryTtl(ttl));
        configMap.put("packetVlaues", defaultCacheConfig.entryTtl(ttl));
        RedisCacheManager redisCacheManager = RedisCacheManager.builder(connectionFactory)
                .initialCacheNames(cacheNames)
                .withInitialCacheConfigurations(configMap)
                .build();
        return redisCacheManager;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Bean
    public RedisTemplate<Serializable, Serializable> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Serializable, Serializable> redisTemplate = new RedisTemplate<Serializable, Serializable>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());// key序列化
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(Object.class)); // value序列化
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }


    @Bean
    public KeyGenerator accountKeyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                String[] value = new String[1];
                Cacheable cacheable = method.getAnnotation(Cacheable.class);
                if (cacheable != null) {
                    value = cacheable.value();
                }
                CachePut cachePut = method.getAnnotation(CachePut.class);
                if (cachePut != null) {
                    value = cachePut.value();
                }
                CacheEvict cacheEvict = method.getAnnotation(CacheEvict.class);
                if (cacheEvict != null) {
                    value = cacheEvict.value();
                }
                sb.append(value[0]);
                for (Object obj : params) {
                    sb.append(":" + obj.toString());
                }
                return sb.toString();
            }
        };
    }
}
