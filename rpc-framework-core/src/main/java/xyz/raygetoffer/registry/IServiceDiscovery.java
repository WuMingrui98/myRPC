package xyz.raygetoffer.registry;

import xyz.raygetoffer.extension.SPI;
import xyz.raygetoffer.remoting.dto.RpcRequest;

import java.net.InetSocketAddress;

/**
 * 服务发现
 *
 * @author mingruiwu
 * @create 2022/7/6 10:28
 * @description
 */
@SPI(value = "zk")
public interface IServiceDiscovery {
    /**
     * 根据rpcRequest获取远程服务地址
     *
     * @param rpcRequest
     * @return      远程服务地址
     */
    InetSocketAddress discoverService(RpcRequest rpcRequest);
}