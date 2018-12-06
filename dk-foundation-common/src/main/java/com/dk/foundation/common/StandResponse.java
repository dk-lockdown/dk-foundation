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

    private int code;
    private String msg;
    private T data;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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
