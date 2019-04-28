package com.dk.foundation.engine.exception;

import com.dk.foundation.engine.baseentity.StandResponse;

public class BusinessException extends Exception {
    private static final long serialVersionUID = 165367809284687797L;

    private int code;

    /**
     * 错误消息
     */
    private String message;

    /**
     * @param message 错误描述
     */
    public BusinessException(String message) {
        super(message);
        this.code = StandResponse.BUSINESS_EXCEPTION;
        this.message = message;
    }

    /**
     * @param code 错误码
     * @param message 错误描述
     */
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    /**
     *
     * @param code
     * @param message
     * @param cause
     */
    public BusinessException(int code, String message,Throwable cause) {
        super(message,cause);
        this.code = code;
        this.message = message;
    }


    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
