package com.kalew515.pestmessageboardbackend.checker;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface BasicChecker {

    boolean check (HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);

    String errorMessage ();
}
