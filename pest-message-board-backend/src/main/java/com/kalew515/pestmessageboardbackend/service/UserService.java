package com.kalew515.pestmessageboardbackend.service;

import com.kalew515.pestmessageboardbackend.dao.UserDao;
import com.kalew515.pestmessageboardbackend.model.User;
import com.kalew515.pestmessageboardbackend.param.user.LoginParam;
import com.kalew515.pestmessageboardbackend.param.user.RegisterParam;
import com.kalew515.pestmessageboardbackend.util.HashTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    // 根据用户名查询用户
    public List<User> getUserByUsername (String username) {
        return userDao.getUserByUsername(username);
    }

    // 添加用户
    public Integer insertUser (RegisterParam registerParam) {
        User user = new User();
        user.setUsername(registerParam.getUsername());
        String salt = HashTool.getRandomString(16);
        String password = salt + ":" + HashTool.SHA256sum(salt + registerParam.getPassword());
        user.setPassword(password);
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        user.setDateJoined(dateTime.format(formatter));
        user.setSignature("这个人很懒, 什么都没有留下");
        return userDao.insertUser(user);
    }

    // 进行密码比对
    public boolean validatePassword (LoginParam loginParam) {
        String truePassword = userDao.getPasswordByUsername(loginParam.getUsername());
        String salt = truePassword.substring(0, 16);
        String expectPassword = truePassword.substring(17, 81);
        return Objects.requireNonNull(HashTool.SHA256sum(salt + loginParam.getPassword())).equals(expectPassword);
    }
}
