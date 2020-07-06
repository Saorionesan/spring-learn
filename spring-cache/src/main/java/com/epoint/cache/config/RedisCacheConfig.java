package com.epoint.cache.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class RedisCacheConfig
{

    @Autowired
    RedisConnectionFactory redisConnectionFactory;

    @Bean
    public RedisCacheManager redisCacheManager(){

        //创建配置类
        /**
         * 在进行配置时 需要走链式调用
         * 否则配置不生效
         */
        RedisCacheConfiguration redisCacheConfiguration=RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues().//不缓存null值
                entryTtl(Duration.ofSeconds(100)).//设置过期时间
                serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())).//设置key序列化器
                serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));//设置value序列化器
        /**
         *
         * 创建对应的cacheMansger  不加锁的缓存提高了吞吐量，但是对于非原子的操作可能会导致线程不安全
         */
        RedisCacheManager redisCacheManager=RedisCacheManager.builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory)).
                cacheDefaults(redisCacheConfiguration).
                build();
        return redisCacheManager;
    }
}
