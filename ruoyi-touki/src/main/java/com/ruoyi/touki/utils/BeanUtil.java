package com.ruoyi.touki.utils;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class BeanUtil {
    /**
     * 对象复制
     */
    public static <T> T copy(Object source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }

        try {
            T target = targetClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, target);
            return target;
        } catch (Exception e) {
            throw new RuntimeException("Bean复制失败", e);
        }
    }

    /**
     * List复制
     */
    public static <S, T> List<T> copyList(List<S> sourceList, Class<T> targetClass) {

        if (sourceList == null || sourceList.isEmpty()) {
            return new ArrayList<>();
        }

        List<T> targetList = new ArrayList<>(sourceList.size());

        for (S source : sourceList) {
            targetList.add(copy(source, targetClass));
        }

        return targetList;
    }
}
