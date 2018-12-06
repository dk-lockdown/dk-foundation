package com.dk.foundation.common;

public class BusinessException extends Exception {
    private static final long serialVersionUID = 165367809284687797L;

    private int code;

    /**
     * 错误消息
     */
    private String message;

    /**
     * @param code 错误码
     * @param message 错误描述
     */
    public BusinessException(int code, String message) {
        this.code = code;
        this.message = message;
    }

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
