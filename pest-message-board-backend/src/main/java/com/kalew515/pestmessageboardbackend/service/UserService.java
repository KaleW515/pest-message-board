package com.kalew515.pestmessageboardbackend.service;

import com.kalew515.pestmessageboardbackend.dao.UserDao;
import com.kalew515.pestmessageboardbackend.model.User;
import com.kalew515.pestmessageboardbackend.param.user.LoginParam;
import com.kalew515.pestmessageboardbackend.param.user.RegisterParam;
import com.kalew515.pestmessageboardbackend.param.user.ResponseUserInfoParam;
import com.kalew515.pestmessageboardbackend.util.HashTool;
import com.kalew515.pestmessageboardbackend.util.RedisTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RedisTool redisTool;

    @Autowired
    private CurrUserService currUserService;

    @Value("${base-url}")
    private String baseUrl;

    // 根据用户名查询用户
    public List<User> getUserByUsername (String username) {
        return userDao.getUserByUsername(username);
    }

    // 根据id查询用户
    public User getUserById (Integer userId) {
        return userDao.getUserById(userId);
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
        return Objects.requireNonNull(HashTool.SHA256sum(salt + loginParam.getPassword()))
                      .equals(expectPassword);
    }

    // 进行密码比对
    public boolean validatePassword (User user, String password) {
        String truePassword = userDao.getPasswordByUsername(user.getUsername());
        String salt = truePassword.substring(0, 16);
        String expectPassword = truePassword.substring(17, 81);
        return Objects.requireNonNull(HashTool.SHA256sum(salt + password))
                      .equals(expectPassword);
    }

    // 更改用户密码
    public void updatePassword (User user, String password) {
        String salt = HashTool.getRandomString(16);
        String newPassword = salt + ":" + HashTool.SHA256sum(salt + password);
        user.setPassword(newPassword);
        userDao.updateUser(user);
    }

    // 检查是否已经被冻结
    public boolean isFreeze (String username) {
        int userId = userDao.getUserByUsername(username)
                            .get(0)
                            .getUserId();
        Object pwdFalseCount = redisTool.hget("info" + userId, "pwd_false_count");
        if (pwdFalseCount == null) {
            redisTool.hset("info" + userId, "pwd_false_count", 0);
            return false;
        }
        return Integer.parseInt(pwdFalseCount.toString()) >= 5;
    }

    // 增加错误次数
    public void addPwdFalseCount (String username) {
        int userId = userDao.getUserByUsername(username)
                            .get(0)
                            .getUserId();
        redisTool.hincr("info" + userId, "pwd_false_count", 1);
    }

    // 解冻用户
    public boolean unFreezeUser (Integer userId) {
        return redisTool.hset("info" + userId, "pwd_false_count", 0);
    }

    // 冻结用户
    public boolean freezeUser (Integer userId) {
        return redisTool.hset("info" + userId, "pwd_false_count", 6);
    }

    // 获取当前用户信息
    public ResponseUserInfoParam getUserInfo () {
        User user = currUserService.getCurrUser();
        ResponseUserInfoParam responseUserInfoParam = new ResponseUserInfoParam();
        responseUserInfoParam.setAvatarUuid(baseUrl + user.getAvatarUUID());
        responseUserInfoParam.setUsername(user.getUsername());
        responseUserInfoParam.setSignature(user.getSignature());
        responseUserInfoParam.setUserId(user.getUserId());
        responseUserInfoParam.setIsSuperuser(user.getIsSuperuser());
        return responseUserInfoParam;
    }

    // 修改用户签名
    public void updateSignature (User user, String signature) {
        user.setSignature(signature);
        userDao.updateUser(user);
    }

    // 修改头像
    public void updateAvatar (User user) {
        userDao.updateUser(user);
    }

    // 获取全部用户
    public List<User> getAllUser () {
        return userDao.getAllUser();
    }
}
