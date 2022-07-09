package xyz.raygetoffer.utils;

import java.util.Collection;

/**
 * @author mingruiwu
 * @create 2022/7/9 15:22
 * @description
 */
public class CollectionUtil {
    public static boolean isEmpty(Collection<?> c) {
        return c == null || c.isEmpty();
    }
}
