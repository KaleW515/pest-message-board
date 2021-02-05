package com.kalew515.pestmessageboardbackend.interceptor;

import com.kalew515.pestmessageboardbackend.checker.BasicChecker;
import com.kalew515.pestmessageboardbackend.model.User;
import com.kalew515.pestmessageboardbackend.service.CurrUserService;
import com.kalew515.pestmessageboardbackend.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public class Interceptor implements HandlerInterceptor {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private CurrUserService currUserService;
    @Autowired
    private ApplicationContext context;

    @Override
    public boolean preHandle (HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws RuntimeException, CheckFailException {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        String token = httpServletRequest.getHeader("Authorization");
        User user = null;
        if (token != null) {
            token = token.replace("Bearer ", "");
            user = jwtService.verifyToken(token);
        }
        currUserService.setUser(user);

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        if (method.isAnnotationPresent(InterceptCheck.class)) {
            Class[] classes = method.getAnnotation(InterceptCheck.class)
                                    .checkers();
            for (Class clazz : classes) {
                BasicChecker checker = (BasicChecker) context.getBean(clazz);
                if (!checker.check(httpServletRequest, httpServletResponse)) {
                    httpServletRequest.setAttribute("com.kalew515.CheckError", checker.errorMessage());
                    throw new CheckFailException("Check failed in " + clazz.getName() + " with " + method.getName());
                }
            }
        }
        //        Class methodClass = method.getDeclaringClass();
        //        if (methodClass.isAnnotationPresent(InterceptCheck.class)) {
        //            InterceptCheck intercept = (InterceptCheck) methodClass.getAnnotation(InterceptCheck.class);
        //            Class[] classes = intercept.checkers();
        //            for (Class clazz : classes) {
        //                BasicChecker checker = (BasicChecker) context.getBean(clazz);
        //                if (!checker.check(httpServletRequest, httpServletResponse)) {
        //                    httpServletRequest.setAttribute("com.kalew515.CheckError", checker.errorMessage());
        //                    throw new CheckFailException("Check failed in " + clazz.getName() + " with " + method.getName());
        //                }
        //            }
        //        }
        return true;
    }

    @Override
    public void afterCompletion (HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        CurrUserService.destroy();
    }
}