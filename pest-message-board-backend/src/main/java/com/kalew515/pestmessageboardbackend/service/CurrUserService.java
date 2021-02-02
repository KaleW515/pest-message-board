package com.kalew515.pestmessageboardbackend.service;

import com.kalew515.pestmessageboardbackend.model.User;
import org.springframework.stereotype.Service;

@Service
public class CurrUserService {
    private static final ThreadLocal<User> user = new ThreadLocal<>();

    public static void destroy () {
        CurrUserService.user.remove();
    }

    public void setUser (User user) {
        if (user != null) {
            CurrUserService.user.set(user);
        }
    }

    public Boolean isAnonymous () {
        return this.getCurrUser() == null;
    }

    public User getCurrUser () {
        return CurrUserService.user.get();
    }
}
