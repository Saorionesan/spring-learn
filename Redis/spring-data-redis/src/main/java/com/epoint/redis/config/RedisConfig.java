package com.epoint.redis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
/**
 * 从spring-data-redis 2.0 开始 其底层使用lettuce 作为连接工厂
 * 如果要继续使用jedis 需要引入jedis依赖，并进行配置
 */
public class RedisConfig {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    //注入名称为druid的指定bean
    //也可以使用 @Resource(name="xxx")来注入
    @Autowired
    @Qualifier("druid")
    DataSource druid;

 /*  注意在2.0以后 如果需要使用jedis来连接 那么需要在bean中配置连接相关信息
 springboot 不会再进行自动配置
 @Bean
    public RedisConnectionFactory jedisConnectionFactory(){
        JedisConnectionFactory jedisConnectionFactory=new JedisConnectionFactory();
        return jedisConnectionFactory;
    }*/

    /**
     * RedisTemplate 中的泛型与其相关的操作类有关 即ValueOperations 等操作
     * 如果选择<String,Object> 类型 那么对应的Operations 实例也只能是该类型
     * 那么需要对 K V 进行序列化机制，可以根据需要的K  V 类型进行序列化
     * 例如 k 需要string类型 （注意 redis中的键都是string 类型） 使用StringRedisSerializer 序列化器
     * v 需要json格式 可以使用 GenericJackson2JsonRedisSerializer  序列化器
     *
     * @return
     */
    @Bean
    public RedisTemplate<String,Object> redisTemplate(){
        RedisTemplate<String,Object> redisTemplate=new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        /**
         * 默认key-value 都是通过 Java 序列化和反序列化的
         * 如果需要更改序列化操作，Redis 模块提供了几个 implementation，它们可以在org.springframework.data.redis.serializer包找到相关序列化类
         *
         默认情况下，RedisCache和RedisTemplate配置为使用 Java 本机序列化。即JdkSerializationRedisSerializer
         spring 官方建议使用任何其他消息格式(例如 JSON)。
         可以为此处的value和HashKeySerializer 以及HashValueSerializer都配置为GenericJackson2JsonRedisSerializer() 序列化器

         如果RedisTemplate 泛型为 <String,Object> 类型,那么建议setValueSerializer 应该为GenericJackson2JsonRedisSerializer()或者其他能够
         序列化任何类型的序列化器。如果选择StringRedisSerializer 那么在存入一个Integer类型时会报错
         或者直接使用StringRedisTemplate 类型
         */
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        /**
         * 如果redis中 value为hash类型（具体见redis开发与运维第二章） 那么此处还需要设置对应的key和value的序列化器
         *
         * 为了保证能直接将对象映射为hash类型存入redis，此处建议不要使用StringRedisSerializer 如果使用StringRedisSerializer，如果对象中
         * 有Integer 等其他类型，会报类型异常错误
         *此处可以直接使用GenericJackson2JsonRedisSerializer序列化器
         */
        redisTemplate.setHashKeySerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        //设置事务支持
        redisTemplate.setEnableTransactionSupport(true);
        return redisTemplate;
    }


    /**
     *由于在redis k-v 都是string类型非常常见.因此 Redis 模块为RedisConnection和RedisTemplate提供了两个 extensions
     * StringRedisConnection和StringRedisTemplate.在StringRedisTemplate和StringRedisConnection默认使用StringRedisSerializer。
     * 只要代码编码和redis编码相同，那么redis中的数据即可读
     *
     * StringRedisTemplate 和RedisTemplate<String,String> 并没有什么区别 只是将k v 泛型类型都设置成String，并且对应的key value序列化器都设置为
     * StringRedisSerializer 序列化器 编码设置为UTF-8
     */
    @Bean
    public StringRedisTemplate stringRedisTemplate(){
        StringRedisTemplate stringRedisTemplate=new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(redisConnectionFactory);
        //开启事务支持
        /**
         * 当开启事务支持时，必须配置一个事务管理器，并且必须使用@Transactional 事务注解
         * 否则当使用了setEnableTransactionSupport（true）的template时而不使用@Transactional事务会导致当前连接一直不被释放
         * 所以建议配置两个template 一个开启事务 一个不开启事务
         *
         * https://www.jianshu.com/p/c9f5718e58f0
         */
        stringRedisTemplate.setEnableTransactionSupport(true);
      return stringRedisTemplate;
    }

    //添加一个事务管理器
    @Bean
    public PlatformTransactionManager transactionManager(){
        return new DataSourceTransactionManager(druid);
    }

    @Bean(name="noTx")
    public StringRedisTemplate noTxRedisTemplate(){
        StringRedisTemplate stringRedisTemplate=new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(redisConnectionFactory);
        return stringRedisTemplate;
    }
}
