package com.dk.foundation.engine.baseentity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class StandResponse<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    public final static int SUCCESS=200;
    public final static int ACCESS_TOKEN_EXPIRED=401;
    public final static int INTERNAL_SERVER_ERROR=500;
    public final static int BUSINESS_EXCEPTION=600;
    public final static int ARGUMENT_EXCEPTION=700;
    public final static int ARGUMENT_MISSING=701;
    public final static int ARGUMENT_TYPE_MISSMATCH=702;
    public final static int HEADER_MISSING=703;
    public final static int COOKIE_MISSING=702;

    private Boolean success;
    private Integer code;
    private String msg;
    private T data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
        if(this.code==SUCCESS){
            this.success=true;
        }
        else{
            this.success=false;
        }
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
