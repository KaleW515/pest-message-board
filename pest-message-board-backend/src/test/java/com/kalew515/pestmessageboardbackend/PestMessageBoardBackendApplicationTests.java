package com.kalew515.pestmessageboardbackend;

import com.kalew515.pestmessageboardbackend.mapper.UserMapper;
import com.kalew515.pestmessageboardbackend.model.User;
import com.kalew515.pestmessageboardbackend.util.RedisTool;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;
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

    @Test
    void pestDataTest () {
        String url = "https://c.m.163.com/ug/api/wuhan/app/data/list-total?t=1581959283780";
        try {
            Document document = Jsoup.connect(url)
                                     .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.106 Safari/537.36")
                                     .get();
            System.out.println(document.text());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
