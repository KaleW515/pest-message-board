package com.kalew515.pestmessageboardbackend.controller;

import com.kalew515.pestmessageboardbackend.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class ErrorController extends AbstractErrorController {

    @Autowired
    ApplicationContext context;

    public ErrorController (ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @RequestMapping("/error")
    public Response<String> errorHandler (HttpServletRequest httpServletRequest,
                                          HttpServletResponse httpServletResponse) {
        String errorMessage = (String) httpServletRequest.getAttribute("com.kalew515.CheckError");
        if (errorMessage != null) {
            httpServletResponse.setStatus(400);
            return Response.invalid(errorMessage);
        } else {
            return Response.failed("Internal Error");
        }
    }

    @Override
    public String getErrorPath () {
        return "/error";
    }

}
