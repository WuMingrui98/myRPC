package xyz.raygetoffer.registry;

import xyz.raygetoffer.extension.SPI;

import java.net.InetSocketAddress;

/**
 * 服务注册
 *
 * @author mingruiwu
 * @create 2022/7/6 10:28
 * @description
 */
@SPI(value = "zk")
public interface IServiceRegistry {
    /**
     * 将服务注册到注册中心
     *
     * @param rpcServiceName        完整的服务名称(class name + group + version)
     * @param inetSocketAddress     远程服务地址
     */
    void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress);
}
