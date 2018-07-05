package com.dk.foundation.common;

import java.util.HashMap;


/**
 * Created by duguk on 2018/1/5.
 */
public class MapUtils extends HashMap<String, Object> {

    @Override
    public MapUtils put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
