package xyz.raygetoffer.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import xyz.raygetoffer.annotation.RpcReference;
import xyz.raygetoffer.config.RpcServiceConfig;
import xyz.raygetoffer.factory.SingletonFactory;
import xyz.raygetoffer.proxy.RpcClientProxyFactory;
import xyz.raygetoffer.remoting.communication.IRpcRequestCommunication;
import xyz.raygetoffer.remoting.communication.netty.client.NettyRpcClient;

import java.lang.reflect.Field;

/**
 * 当bean实例化并赋值后，在初始化的前后会分别调用BeanPostProcessor的postProcessBeforeInitialization和postProcessAfterInitialization
 *
 * 对于@RpcReference注解的字段（服务），用动态代理生成其代理类，通过RpcClient调用远端的服务
 *
 * @author mingruiwu
 * @create 2022/7/14 17:08
 * @description
 */
@Slf4j
@Component
public class RpcReferenceBeanPostProcessor implements BeanPostProcessor {
    private IRpcRequestCommunication rpcClient = SingletonFactory.getInstance(NettyRpcClient.class);

    public RpcReferenceBeanPostProcessor() {

    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Field[] declaredFields = bean.getClass().getDeclaredFields();
        for(Field field : declaredFields) {
            if (field.isAnnotationPresent(RpcReference.class)) {
                // 获得RpcReference注解
                RpcReference reference = field.getAnnotation(RpcReference.class);
                RpcServiceConfig rpcServiceConfig = RpcServiceConfig.builder()
                        .version(reference.version())
                        .group(reference.group())
                        .build();
                // 动态代理生成代理对象并替换
                Object proxy = RpcClientProxyFactory.getProxy(field.getType(), rpcClient, rpcServiceConfig);
                field.setAccessible(true);
                try {
                    field.set(bean, proxy);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
