package xyz.raygetoffer.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import xyz.raygetoffer.annotation.RpcService;
import xyz.raygetoffer.config.RpcServiceConfig;
import xyz.raygetoffer.extension.ExtensionLoader;
import xyz.raygetoffer.provider.IServiceProvider;
import xyz.raygetoffer.remoting.communication.IRpcRequestCommunication;

/**
 * 当bean实例化并赋值后，在初始化的前后会分别调用BeanPostProcessor的postProcessBeforeInitialization和postProcessAfterInitialization
 * 在被@RpcService注解的服务实例化后，将其注册到注册中心
 *
 * @author mingruiwu
 * @create 2022/7/14 16:36
 * @description
 */
@Slf4j
@Component
public class RpcServiceBeanPostProcessor implements BeanPostProcessor {
    private final IServiceProvider serviceProvider;

    public RpcServiceBeanPostProcessor() {
        this.serviceProvider = ExtensionLoader.getExtensionLoader(IServiceProvider.class).getExtension();
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(RpcService.class)) {
            log.info("[{}] is annotated with  [{}]", bean.getClass().getName(), RpcService.class.getCanonicalName());
            // 获得RpcService注解，并获得响应属性
            RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);
            // 构造RpcServiceConfig
            RpcServiceConfig rpcServiceConfig = RpcServiceConfig.builder()
                    .version(rpcService.version())
                    .group(rpcService.group())
                    .service(bean).build();
            serviceProvider.publishService(rpcServiceConfig);
        }
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }
}
