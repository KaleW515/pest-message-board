package com.kalew515.pestmessageboardbackend;

import com.kalew515.pestmessageboardbackend.mapper.UserMapper;
import com.kalew515.pestmessageboardbackend.model.User;
import com.kalew515.pestmessageboardbackend.util.RedisTool;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@SpringBootTest
class PestMessageBoardBackendApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTool redisTool;

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate redisTemplate;

    @Test
    void contextLoads () {
        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        Assert.assertEquals(1, userList.size());
        userList.forEach(System.out::println);
    }

    @Test
    void redisTest () {
        Object freeze = redisTool.hget("info" + "2", "freeze");
        System.out.println(freeze);
    }

}
