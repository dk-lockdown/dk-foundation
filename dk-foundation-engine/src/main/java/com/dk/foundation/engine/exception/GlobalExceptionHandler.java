package com.dk.foundation.engine.exception;

import com.dk.foundation.engine.baseentity.StandResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public @ResponseBody
    StandResponse exception(HttpMessageNotReadableException e, HttpServletResponse response) {
        logger.error("#######ERROR#######", e);
        StandResponse standResponse = new StandResponse();
        standResponse.setCode(StandResponse.ARGUMENT_EXCEPTION);
        standResponse.setSuccess(false);
        standResponse.setMsg("解析请求参数体出错");
        return standResponse;
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public @ResponseBody
    StandResponse exception(MethodArgumentTypeMismatchException e, HttpServletResponse response) {
        logger.error("#######ERROR#######", e);
        StandResponse standResponse = new StandResponse();
        standResponse.setCode(StandResponse.ARGUMENT_TYPE_MISSMATCH);
        standResponse.setSuccess(false);
        standResponse.setMsg("参数类型不匹配:" + e.getName());
        return standResponse;
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    public @ResponseBody
    StandResponse exception(MissingServletRequestParameterException e, HttpServletResponse response) {
        logger.error("#######ERROR#######", e);
        StandResponse standResponse = new StandResponse();
        standResponse.setCode(StandResponse.ARGUMENT_MISSING);
        standResponse.setSuccess(false);
        standResponse.setMsg("缺少参数：" + e.getParameterName());
        return standResponse;
    }

    @ExceptionHandler({MissingRequestHeaderException.class})
    public @ResponseBody StandResponse exception(MissingRequestHeaderException e, HttpServletResponse response) {
        logger.error("#######ERROR#######", e);
        StandResponse standResponse = new StandResponse();
        standResponse.setSuccess(false);
        standResponse.setCode(StandResponse.HEADER_MISSING);
        standResponse.setMsg("缺少request header");
        return standResponse;
    }

    @ExceptionHandler({MissingRequestCookieException.class})
    public @ResponseBody
    StandResponse exception(MissingRequestCookieException e, HttpServletRequest request, HttpServletResponse response) {
        logger.error("#######ERROR#######", e);
        StandResponse standResponse = new StandResponse();
        standResponse.setSuccess(false);
        standResponse.setCode(StandResponse.COOKIE_MISSING);
        standResponse.setMsg("缺少cookie");
        return standResponse;
    }

    @ExceptionHandler({BusinessException.class})
    public @ResponseBody
    StandResponse exception(BusinessException e, HttpServletRequest request, HttpServletResponse response) {
        e.printStackTrace();
        logger.error("#######ERROR#######", e);
        StandResponse standResponse = new StandResponse();
        standResponse.setSuccess(false);
        standResponse.setCode(StandResponse.BUSINESS_EXCEPTION);
        standResponse.setMsg(e.getMessage());
        return standResponse;
    }

    @ExceptionHandler({Exception.class})
    public @ResponseBody
    StandResponse exception(Exception e, HttpServletRequest request, HttpServletResponse response) {
        e.printStackTrace();
        logger.error("#######ERROR#######", e);
        StandResponse standResponse = new StandResponse();
        standResponse.setSuccess(false);
        standResponse.setCode(StandResponse.INTERNAL_SERVER_ERROR);
        standResponse.setMsg(e.getMessage());
        return standResponse;
    }
}
