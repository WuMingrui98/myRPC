package xyz.raygetoffer.proxy.cglib;

import net.sf.cglib.proxy.Enhancer;

/**
 * @author mingruiwu
 * @create 2022/7/1 16:38
 * @description
 */
public class CglibProxyFactory {
    public static Object getProxy(Class<?> clazz) {
        Enhancer enhancer = new Enhancer();
        // 设置类加载器
        enhancer.setClassLoader(clazz.getClassLoader());
        // 设置被代理类
        enhancer.setSuperclass(clazz);
        // 设置方法拦截器
        enhancer.setCallback(new DebugMethodInterceptor());
        // 创建代理类
        return enhancer.create();
    }
}
