package com.dk.foundation.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by duguk on 2018/1/5.
 */
public class R extends HashMap<String, Object> implements Serializable {
	private static final long serialVersionUID = 1L;

	public R() {
		put("code", 0);
		put("msg", "success");
	}

	public static R error() {
		return error(500, "未知异常，请联系管理员");
	}

	public static R error(String msg) {
		return error(500, msg);
	}

	public static R error(int code, String msg) {
		R r = new R();
		r.put("code", code);
		r.put("msg", msg);
		return r;
	}

	public static R ok(String msg) {
		R r = new R();
		r.put("msg", msg);
		return r;
	}

	public static R ok(Map<String, Object> map) {
		R r = new R();
		r.putAll(map);
		return r;
	}

	public static R ok() {
		return new R();
	}

	public R data(Object data){
		this.put("data",data);
		return this;
	}

	@Override
	public R put(String key, Object value) {
		super.put(key, value);
		return this;
	}

	@Override
	public String toString() {
		String dateFormat = "yyyy-MM-dd HH:mm:ss";
		SerializerFeature[] serializerFeatures = {SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteNullListAsEmpty };
		String json = JSON.toJSONStringWithDateFormat(this, dateFormat, serializerFeatures);
		return json;
	}
}
