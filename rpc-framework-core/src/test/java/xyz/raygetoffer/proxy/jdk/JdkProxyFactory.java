package xyz.raygetoffer.proxy.jdk;

import java.lang.reflect.Proxy;

/**
 * @author mingruiwu
 * @create 2022/7/1 15:59
 * @description
 */
public class JdkProxyFactory {
    public static Object getProxy(Object target) {
        return Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new DebugInvocationHandler(target)
        );
    }
}
