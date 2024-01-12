package com.autel.sdksample.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CollectionUtil {
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static <S, R> List<R> transform(List<S> sourceList, Transformer<S, R> transformer) {
        List<R> list = new ArrayList<>();
        if (isNotEmpty(sourceList)) {
            for (S source : sourceList) {
                R target = transformer.transform(source);
                list.add(target);
            }
        }
        return list;
    }

    /**
     * api 版本问题，目前不能使用系统预置的Function接口，自己定义一个
     *
     * @param <S>
     * @param <R>
     */
    public interface Transformer<S, R> {
        R transform(S source);
    }
}
