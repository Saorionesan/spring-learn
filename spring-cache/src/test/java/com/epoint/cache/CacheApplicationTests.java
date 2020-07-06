package com.epoint.cache;

import com.epoint.cache.cache.CacheTest;
import com.epoint.cache.entity.User;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CacheApplicationTests {

	private Logger logger= LoggerFactory.getLogger(CacheApplicationTests.class);
	@Autowired
	CacheTest cacheTest;
	@Test
	void contextLoads() {
	}

	@Test
	public void test(){

		System.out.println("haha");

		System.out.println("当前代码是否还会执行");

	}

	@Test
	public void testCache(){
		logger.info("----创建缓存----");
	cacheTest.getUserById(3);
	//logger.info("当前的user介绍:"+user.getIntroduction());
	}

	@Test
	public void addTest(){
		User user=new User();
		user.setBlog("haha");
		user.setEmail("139899@qq.com");
		user.setFans(100);
		user.setUsername("user_test");
		user.setPassword("passWord");
		user.setUserid(10);
		cacheTest.addUser(user);
	}

	@Test
	public void delAll(){
		cacheTest.delAll();
	}

}
