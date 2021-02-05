package com.kalew515.pestmessageboardbackend.checker;

import com.kalew515.pestmessageboardbackend.service.CurrUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class LoginChecker implements BasicChecker {

    @Autowired
    CurrUserService currUserService;

    @Override
    public boolean check (HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return !currUserService.isAnonymous();
    }

    @Override
    public String errorMessage () {
        return "Please login first";
    }
}
