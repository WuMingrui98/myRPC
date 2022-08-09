package xyz.raygetoffer.proxy;

import xyz.raygetoffer.config.RpcServiceConfig;
import xyz.raygetoffer.remoting.communication.IRpcRequestCommunication;

import java.lang.reflect.Proxy;

/**
 * @author mingruiwu
 * @create 2022/7/9 17:49
 * @description
 */
public class RpcClientProxyFactory {

    /**
     *
     * @param tClass 目标对象的类型
     * @param rpcClient
     * @param rpcServiceConfig
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getProxy(Class<T> tClass, IRpcRequestCommunication rpcClient,
                                 RpcServiceConfig rpcServiceConfig) {
        return (T) Proxy.newProxyInstance(tClass.getClassLoader(),
                new Class<?>[]{tClass}, new RpcClientInvocationHandler(rpcClient, rpcServiceConfig));
    }
}
