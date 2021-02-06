package com.kalew515.pestmessageboardbackend.checker;

import com.kalew515.pestmessageboardbackend.service.CurrUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class AdminChecker implements BasicChecker {

    @Autowired
    private CurrUserService currUserService;

    @Override
    public boolean check (HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return currUserService.getCurrUser()
                              .getIsSuperuser() == 1;
    }

    @Override
    public String errorMessage () {
        return "you are not admin";
    }
}
