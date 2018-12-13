package com.dk.foundation.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.Serializable;

public class StandResponse<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    public final static int BUSSINESS_EXCEPTION=1;
    public final static int INTERNAL_SERVER_ERROR=500;
    public final static int ACCESS_TOKEN_EXPIRED=401;
    public final static int SUCCESS=0;

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
        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        SerializerFeature[] serializerFeatures = {SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteNullListAsEmpty };
        return JSON.toJSONStringWithDateFormat(this, dateFormat, serializerFeatures);
    }

}
