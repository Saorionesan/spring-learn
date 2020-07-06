package com.epoint.cache.cache;

import com.epoint.cache.entity.User;
import com.epoint.cache.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
//统一配置 缓存名称为user
@CacheConfig(cacheNames = "user")
public class CacheTest {

    @Autowired
    UserMapper userMapper;

    private Logger logger= LoggerFactory.getLogger(CacheTest.class);
    /**
     * 当我们在调用一个缓存方法时会把该方法参数和返回结果作为一个键值对存放在缓存中，等到下次利用同样的参数来调用该方法时将不再执行该方法，而是直接从缓存中获取结果进行返回。
     * 所以在使用Spring Cache的时候我们要保证我们缓存的方法对于相同的方法参数要有相同的返回结果。
     * @param userId
     * @return
     *
     * 根据方法对其返回结果进行缓存，下次请求时，如果缓存存在，则直接读取缓存数据返回(缓存可以是redis cache 也可以是其他类型)；
     * 如果缓存不存在，则执行方法（从数据库中去获取缓存），并把返回的结果存入缓存中。一般用在查询方法上。
     *
     *value 代表缓存的namespace 该参数必须有 （缓存名称 注意不是存入redis中的key）
     *
     * key 代表缓存的
     */
    /**
     * 此处的value值在存入redis时会进行拼接
     * 将value的值拼接为key
     * @param userId
     * @return
     */
    @Cacheable(value = "user",key="#userId")
    public User getUserById(Integer userId){
        return userMapper.selectByUserId(userId);
    }

    /**
     * 使用spel 表达式  以user中的userid为key
     * @CachePut 注解不管如何一定会执行方法，并将结果缓存，后续调用时可以直接走缓存。该方法通常用于新增操作
     * @param user
     * @return
     */
    @CachePut(cacheNames = "user",key = "#user.userid")
    public User addUser(User user){
        userMapper.insert(user);
        return user;
    }

/**
 * 该注解用于清除缓存
 * 清除指定key的缓存 key
 * 常用于更新或删除操作
 */
    @CacheEvict(key="#userid")
   public void delUser(Integer userid){

    }

    /**
     * @CacheEvict 注解有一个参数allEntries 可以清除所有缓存
     *
     */

    @CacheEvict(allEntries = true)
    public void delAll(){
        logger.info("清除缓存中的所有数据");
    }
}
