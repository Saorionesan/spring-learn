package com.epoint.redis.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.Set;

@Service
public class RedisService {

    private static final Logger logger= LoggerFactory.getLogger(RedisService.class);
    @Autowired
    @Qualifier("stringRedisTemplate")
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedisTemplate redisTemplate;


//    public boolean execKill(String prodId){
//        ValueOperations<String,String> valueOperations=stringRedisTemplate.opsForValue();
//
//        ValueOperations<String,Set> valueOperations1=redisTemplate.opsForValue();
//
//        //抢购的用户ID
//        String userId=new Random().nextInt(5000)+"";
//
//        //拼接key，商品库存、秒杀成功用户ID
//        //库存key
//        String kckey="Seckill:"+prodId+":kc";
//
//        String userKey="Seckill:"+prodId+":user";
//
//        /***
//         * 如果商品的剩余量大于0 则减库存并将用户的ID存入抢购成功的模块
//         */
//
//        //秒杀未开始时 商品库存还没有 库存为null
//        //获取商品库存时为null秒杀还未开始
//       if(valueOperations.get(kckey)==null){
//         logger.warn("秒杀还未开始");
//         return false;
//       }
//
//        //用户已经秒杀成功了 存储用户uid的set中已经有该用户的uid
//
//
//
//        //秒杀成功扣减库存 若库存大于0 扣减库存加入用户uid，否则秒杀结束
//
//
//
//
//
//
//
//
//
//
//
//
//
//    }

    @Transactional
    public void tractionTest(){
       ValueOperations<String,String> valueOperations= stringRedisTemplate.opsForValue();
        valueOperations.set("a","bc");
        //当此处执行报错时，由于redis事务不具有回滚的特性，所以此时会继续执行下去，只有该条命令会报错
        valueOperations.increment("a");
        valueOperations.set("count","1");
       // 事务不可见性 查询到的值为null
        logger.info("当前查询到的信息:"+ valueOperations.get("a"));
    }

}
