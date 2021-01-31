package com.kalew515.pestmessageboardbackend.dao;

import com.kalew515.pestmessageboardbackend.mapper.UserMapper;
import com.kalew515.pestmessageboardbackend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public class UserDao {

    @Autowired
    private UserMapper userMapper;

    // 根据用户名查询用户
    public List<User> getUserByUsername (String username) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("username", username);
        return userMapper.selectByMap(map);
    }

    // 插入用户
    public Integer insertUser (User user) {
        return userMapper.insert(user);
    }

    // 获取用户密码
    public String getPasswordByUsername (String username) {
        return getUserByUsername(username).get(0).getPassword();
    }
}
