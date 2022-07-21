package xyz.raygetoffer.factory;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 获得单例对象的工厂类，懒加载机制
 *
 * @author mingruiwu
 * @create 2022/7/9 20:37
 * @description
 */
public class SingletonFactory {
    private static final Map<Class<?>, Object> OBJECT_MAP = new ConcurrentHashMap<>();

    private SingletonFactory() {}

    public static <T> T getInstance(Class<T> tClass) {
        if (tClass == null) {
            throw new IllegalArgumentException();
        }
        return tClass.cast(OBJECT_MAP.computeIfAbsent(tClass, k -> {
            try {
                // 默认返回空参构造函数构造的对象
                return tClass.getDeclaredConstructor().newInstance();
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }));
    }
}
