package xyz.raygetoffer.provider.impl;

import lombok.extern.slf4j.Slf4j;
import xyz.raygetoffer.config.RpcServiceConfig;
import xyz.raygetoffer.enums.RpcErrorMessageEnum;
import xyz.raygetoffer.exception.RpcException;
import xyz.raygetoffer.provider.IServiceProvider;
import xyz.raygetoffer.registry.IServiceRegistry;
import xyz.raygetoffer.registry.zk.ZkServiceRegistry;
import xyz.raygetoffer.remoting.communication.netty.server.NettyRpcServer;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mingruiwu
 * @create 2022/7/9 20:10
 * @description
 */
@Slf4j
public class ZkServiceProvider implements IServiceProvider {
    /**
     * 存放服务的容器，key: rpcServiceName，value: Service
     */
    private final Map<String, Object> serviceMap;
    private final IServiceRegistry serviceRegistry;

    public ZkServiceProvider() {
        this.serviceMap = new ConcurrentHashMap<>();
        // TODO 不同的serviceRegistry
        this.serviceRegistry = new ZkServiceRegistry();
    }

    @Override
    public void publishService(RpcServiceConfig rpcServiceConfig) {
        try {
            String host = InetAddress.getLocalHost().getHostAddress();
            addService(rpcServiceConfig);
            serviceRegistry.registerService(rpcServiceConfig.getRpcServiceName(), new InetSocketAddress(host, NettyRpcServer.PORT));
        } catch (UnknownHostException e) {
            log.error("Occur exception when getHostAddress", e);
        }
    }

    @Override
    public Object getService(String rpcServiceName) {
        Object service = serviceMap.get(rpcServiceName);
        if (service == null) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND, rpcServiceName);
        }
        return service;
    }

    private void addService(RpcServiceConfig rpcServiceConfig) {
        String rpcServiceName = rpcServiceConfig.getRpcServiceName();
        if (serviceMap.containsKey(rpcServiceName)) {
            return;
        }
        serviceMap.put(rpcServiceName, rpcServiceConfig.getService());
        log.info("Add service: {} and interfaces:{}", rpcServiceName, rpcServiceConfig.getService().getClass().getInterfaces()[0]);
    }

}
