package com.dk.foundation.engine.exception;

import com.dk.foundation.engine.baseentity.StandResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({BusinessException.class})
    public @ResponseBody
    StandResponse exception(BusinessException e, HttpServletRequest request, HttpServletResponse response) {
        e.printStackTrace();
        logger.error("#######ERROR#######", e);
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "OPTIONS,GET,POST");
        StandResponse standResponse = new StandResponse();
        standResponse.setSuccess(false);
        standResponse.setCode(StandResponse.BUSSINESS_EXCEPTION);
        standResponse.setMsg(e.getMessage());
        return standResponse;
    }

    @ExceptionHandler({Exception.class})
    public @ResponseBody
    StandResponse exception(Exception e, HttpServletRequest request, HttpServletResponse response) {
        e.printStackTrace();
        logger.error("#######ERROR#######", e);
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "OPTIONS,GET,POST");
        StandResponse standResponse = new StandResponse();
        standResponse.setSuccess(false);
        standResponse.setCode(StandResponse.INTERNAL_SERVER_ERROR);
        standResponse.setMsg(e.getMessage());
        return standResponse;
    }
}
