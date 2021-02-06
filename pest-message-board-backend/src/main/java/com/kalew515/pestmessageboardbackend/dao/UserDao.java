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

    // 根据用户id查询用户
    public User getUserById (Integer userId) {
        return userMapper.selectById(userId);
    }

    // 插入用户
    public Integer insertUser (User user) {
        return userMapper.insert(user);
    }

    // 获取用户密码
    public String getPasswordByUsername (String username) {
        return getUserByUsername(username).get(0)
                                          .getPassword();
    }

    // 更新用户
    public void updateUser (User user) {
        userMapper.updateById(user);
    }

    // 获取全部用户
    public List<User> getAllUser () {
        return userMapper.selectList(null);
    }
}
