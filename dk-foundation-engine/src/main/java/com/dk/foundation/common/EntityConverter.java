package com.dk.foundation.common;

import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;

import java.util.List;

public final class EntityConverter {

    public static <T> T copyAndGetSingle(Object source, Class<T> type) {
        if (source == null || type == null) {
            return null;
        }

        T target = BeanUtils.instantiateClass(type);
        BeanUtils.copyProperties(source, target);
        return target;
    }

    public static <T> T copyAndGetSingle(Object source, Class<T> type, String... ignoreProperties) {
        if (source == null || type == null) {
            return null;
        }

        T target = BeanUtils.instantiateClass(type);
        BeanUtils.copyProperties(source, target, ignoreProperties);
        return target;
    }

    public static <T> List<T> copyAndGetList(Object source, Class<T> type) {
        List<T> result = Lists.newArrayList();
        if (source == null || type == null || !(source instanceof List)
                || ((List<?>) source).isEmpty()) {
            return result;
        }

        for (Object obj : (List<?>) source) {
            T target = BeanUtils.instantiateClass(type);
            BeanUtils.copyProperties(obj, target);
            result.add(target);
        }
        return result;
    }

    public static <T> List<T> copyAndGetList(Object source, Class<T> type, String... ignoreProperties) {
        List<T> result = Lists.newArrayList();
        if (source == null || type == null || !(source instanceof List)
                || ((List<?>) source).isEmpty()) {
            return result;
        }

        for (Object obj : (List<?>) source) {
            T target = BeanUtils.instantiateClass(type);
            BeanUtils.copyProperties(obj, target, ignoreProperties);
            result.add(target);
        }
        return result;
    }
}
