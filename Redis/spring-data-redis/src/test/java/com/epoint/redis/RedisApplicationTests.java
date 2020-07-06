package com.epoint.redis;

import com.epoint.redis.entity.Student;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.data.redis.hash.Jackson2HashMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@SpringBootTest
class RedisApplicationTests {

	private Logger logger= LoggerFactory.getLogger(RedisApplicationTests.class);
	@Autowired
	RedisTemplate<String,Object> redisTemplate;

	@Autowired
	StringRedisTemplate stringRedisTemplate;
	@Test
	void contextLoads() {


	}

	@Test
	public void  test(){
		ValueOperations<String,Object> valueOperations=redisTemplate.opsForValue();
		Student student=new Student(1,"haha",2,new Date());
		Student student1=new Student(2,"TEST",3,new Date());
		List<Student> list=new ArrayList<>();
		list.add(student);
		list.add(student1);
		valueOperations.set("list",list);
	}
	@Test
	public void  test1(){
		ValueOperations<String,Object> valueOperations=redisTemplate.opsForValue();
		/**
		 * 将其反序列化为Java对象
		 */
		Student student=(Student) valueOperations.get("test");
		logger.info("用户名称"+student.getUserName());
	}
	@Test
	public void test2(){
		ValueOperations<String,Object> valueOperations=redisTemplate.opsForValue();
		System.out.println(redisTemplate.getConnectionFactory().getConnection().toString());
		/**
		 * 将其反序列化为Java对象
		 */
		List<Student> list=(List<Student>) valueOperations.get("list");
		logger.info("集合长度"+list.size());
	}

	@Test
	public void stringRedisTest(){
		/**
		 * StringRedisTemplate 实际上就是继承了RedisTemplate 并将k v都设置为String 类型，保证其opsForValue()返回的类型为ValueOperations<String, String>
		 */
	ValueOperations<String,String> valueOperations=stringRedisTemplate.opsForValue();
     valueOperations.set("user","haha");
	}

	@Test
	public void hashTest(){
		HashOperations<String,String,String> hashOperations=stringRedisTemplate.opsForHash();
		hashOperations.put("user1","name","ysk");
	}

	/**
	 * 当需要将object直接作为hash类型存入redis中时，可以使用hashmapper类型
	 * https://www.docs4dev.com/docs/zh/spring-data-redis/1.8.18.RELEASE/reference/all.html#redis
	 *
	 */

	@Test
	public void hashMapperTest(){
		/**
		 * https://www.docs4dev.com/docs/zh/spring-data-redis/1.8.18.RELEASE/reference/all.html#redis
		 * 三个泛型 此处泛型的类型要看具体使用的实现类的类型
		 * 即HashMapper 接口的具体实现类的类型，比如此处的Jackson2HashMapper为<Object, String, Object> 类型
		 */
		HashMapper<Object,String,Object> mapper=new Jackson2HashMapper(true);
		Student student=new Student(1,"haha",2,new Date());
		Map<String,Object> map=mapper.toHash(student);
       redisTemplate.opsForHash().putAll("user3",map);
       HashOperations<String,String,Object> operations=redisTemplate.opsForHash();
       Map<String,Object> hashmap=operations.entries("user3");
		Student student1=(Student)mapper.fromHash(hashmap);
		System.out.println(student1.getDate());
	}
   @Test
	public void setTest(){
		SetOperations<String,String> setOperations=stringRedisTemplate.opsForSet();
		setOperations.add("userlist","1");
   }

   @Test
	public void test10(){
	ValueOperations<String,Object> valueOperations=redisTemplate.opsForValue();
	valueOperations.set("key",2);
   }

   @Test
   //执行pipline
	public void pipline(){
	   List<Object> results = stringRedisTemplate.executePipelined(
			   new RedisCallback<Object>() {
				   public Object doInRedis(RedisConnection connection) throws DataAccessException {
					   StringRedisConnection redisConnection=(StringRedisConnection)connection;
					   	redisConnection.get("test");
					   	redisConnection.get("haha");
					   return null;
				   }
			   });
	   System.out.println(results);
 }




}
