package com.epoint.redis.controller;

import com.epoint.redis.entity.Student;
import com.epoint.redis.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@RestController
public class RedisController {

    Logger logger= LoggerFactory.getLogger(RedisController.class);
    @Autowired
    RedisService redisService;

    @RequestMapping("/txTest")
    public Student txTest(){
        Student student=new Student();
        student.setID(1);
        student.setDate(new Date());
        student.setRank(10);
        student.setUserName("小明");
        return student;
    }

    /**
     * get请求中 body中也可以添加参数 如果在body添加参数，那么server端的注解则根据数据的解析格式来定
     * 即如果传输的格式Content-Type 为 application/json 格式则使用@RequestBody 注解来进行接收
     * 如果解析格式为Content-Type 为application/x-www-form-urlencoded 即 key1=value1&key2=value2 这种情况那么即使用@RequestParam 注解来进行接收数据
     * 在url后面添加的参数为application/x-www-form-urlencoded 格式解析 使用@RequestParam 注解来接收
     *
     * post 请求在url后面也可以添加参数 参数解析格式为application/x-www-form-urlencoded 使用@RequestParam 来接收数据
     *
     * 在post的 body中有多种格式解析application/x-www-form-urlencoded  、application/json 根据对应的数据格式选择对应的注解进行接收数据
     *
     * 建议get请求只在url后面添加参数
     *
     * post请求数据一般都放在body中 url后面看情况也可以添加参数
     *
     */
    @GetMapping(value = "/loadBody.do")
    public String testGet(@RequestBody Student student){
        logger.info("用户名："+student.getUserName());
        return "调用成功";
    }

    @RequestMapping("/getSession.do")
    public String getSession(HttpServletRequest request){
       HttpSession session= request.getSession();
        return "session 创建成功";
    }


}
