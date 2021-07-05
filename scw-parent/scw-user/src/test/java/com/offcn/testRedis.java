package com.offcn;

import com.offcn.user.UserStartApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {UserStartApplication.class})
public class testRedis {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Test
    public void test1(){
        redisTemplate.boundValueOps("a1").set("aaaa");
      String s= (String) redisTemplate.boundValueOps("a1").get();
        System.out.println("从redis读取:"+s);
    }
    @Test
    public void test2(){
       // stringRedisTemplate.boundValueOps("a1").set("asddadaa");
       // String a1 = stringRedisTemplate.boundValueOps("a1").get();
        String a1 = redisTemplate.boundValueOps("a1").get();
        System.out.println(a1);
    }
}
