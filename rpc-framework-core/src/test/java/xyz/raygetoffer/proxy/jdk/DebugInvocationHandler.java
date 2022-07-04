package xyz.raygetoffer.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author mingruiwu
 * @create 2022/7/1 15:51
 * @description
 */
public class DebugInvocationHandler implements InvocationHandler {
    /**
     * 代理类中的真实对象
     */
    private final Object target;

    public DebugInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("before method send()");
        Object result = method.invoke(target, args);
        System.out.println("after method send()");
        return result;
    }
}
