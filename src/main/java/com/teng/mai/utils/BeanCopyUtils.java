package com.teng.mai.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/3/22 22:23
 */
public class BeanCopyUtils {
    public static <T> T copyObject(Object source, Class<T> target) {
        T temp = null;
        try {
            temp = target.newInstance();
            if (null != source) {
                org.springframework.beans.BeanUtils.copyProperties(source, temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }

    public static <T, S> List<T> copyList(List<S> source, Class<T> target) {
        List<T> list = new ArrayList<>();
        if (null != source && source.size() > 0) {
            for (Object obj : source) {
                list.add(BeanCopyUtils.copyObject(obj, target));
            }
        }
        return list;
    }
}
