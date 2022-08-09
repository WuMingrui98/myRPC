package xyz.raygetoffer.remoting.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import xyz.raygetoffer.exception.RpcException;
import xyz.raygetoffer.extension.ExtensionLoader;
import xyz.raygetoffer.provider.IServiceProvider;
import xyz.raygetoffer.remoting.dto.RpcRequest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 处理Rpc请求，调用对应的服务
 * @author mingruiwu
 * @create 2022/8/6 21:53
 * @description
 */
@Slf4j
@Component
public class RpcRequestHandler {
    private final IServiceProvider serviceProvider = ExtensionLoader.getExtensionLoader(IServiceProvider.class).getExtension();

    /**
     * 调用RpcRequest中的对应服务的方法，并返回结果
     */
    public Object handle(RpcRequest rpcRequest) {
        Object service = serviceProvider.getService(rpcRequest.getRpcServiceName());
        return invokeTargetMethod(rpcRequest, service);
    }

    /**
     * 调用目标方法，返回结果
     * @param rpcRequest
     * @param service 需要调用的服务
     * @return 返回服务的调用结果
     */
    private Object invokeTargetMethod(RpcRequest rpcRequest, Object service) {
        Object result;
        try {
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParaClasses());
            result = method.invoke(service, rpcRequest.getParameters());
            log.info("service:[{}] successful invoke method:[{}]", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RpcException(e.getMessage(), e);
        }
        return result;
    }
}
