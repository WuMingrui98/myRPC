package xyz.raygetoffer.proxy;

import lombok.extern.slf4j.Slf4j;
import xyz.raygetoffer.config.RpcServiceConfig;
import xyz.raygetoffer.enums.RpcErrorMessageEnum;
import xyz.raygetoffer.enums.RpcResponseCodeEnum;
import xyz.raygetoffer.exception.RpcException;
import xyz.raygetoffer.remoting.communication.IRpcRequestCommunication;
import xyz.raygetoffer.remoting.dto.RpcRequest;
import xyz.raygetoffer.remoting.dto.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Rpc动态代理类的InvocationHandler，当动态代理类调用方法时，实际是调用的是下述的invoke方法。
 * @author mingruiwu
 * @create 2022/7/9 15:47
 * @description
 */
@Slf4j
public class RpcClientInvocationHandler implements InvocationHandler {
    private static final String INTERFACE_NAME = "interfaceName";

    // rpc的网络通信的客户端
    private final IRpcRequestCommunication rpcClient;

    private final RpcServiceConfig rpcServiceConfig;

    public RpcClientInvocationHandler(IRpcRequestCommunication rpcClient, RpcServiceConfig rpcServiceConfig) {
        this.rpcClient = rpcClient;
        this.rpcServiceConfig = rpcServiceConfig;
    }

    public RpcClientInvocationHandler(IRpcRequestCommunication rpcClient) {
        this.rpcClient = rpcClient;
        this.rpcServiceConfig = new RpcServiceConfig();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("Invoke method: [{}]", method.getName());
        // 构造RpcRequest
        RpcRequest rpcRequest = RpcRequest.builder()
                .methodName(method.getName())
                .interfaceName(method.getDeclaringClass().getCanonicalName())
                .parameters(args)
                .paraClasses(method.getParameterTypes())
                .group(rpcServiceConfig.getGroup())
                .version(rpcServiceConfig.getVersion())
                .requestId(UUID.randomUUID().toString())
                .build();

        RpcResponse<Object> rpcResponse = null;
        // 使用netty来进行网络通信
        CompletableFuture<RpcResponse<Object>> completableFuture = (CompletableFuture<RpcResponse<Object>>) rpcClient.sendRpcRequest(rpcRequest);
        rpcResponse = completableFuture.get();
        // 检查rpcResponse
        check(rpcRequest, rpcResponse);
        return rpcResponse.getData();
    }


    /**
     * 检查rpcRequest和rpcResponse是否匹配，以及rpcResponse的合法性
     * @param rpcRequest
     * @param rpcResponse
     */
    private void check(RpcRequest rpcRequest, RpcResponse<Object> rpcResponse) {
        if(rpcResponse == null) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }

        if (!rpcRequest.getRequestId().equals(rpcResponse.getRequestId())) {
            throw new RpcException(RpcErrorMessageEnum.REQUEST_NOT_MATCH_RESPONSE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }

        if (rpcResponse.getCode() == null || !rpcResponse.getCode().equals(RpcResponseCodeEnum.SUCCESS.getCode())) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }
    }
}
