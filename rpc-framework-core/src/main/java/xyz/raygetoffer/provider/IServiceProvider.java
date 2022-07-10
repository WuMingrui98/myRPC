package xyz.raygetoffer.provider;


import xyz.raygetoffer.config.RpcServiceConfig;

/**
 * 服务提供者
 *
 * @author mingruiwu
 * @create 2022/7/9 20:04
 * @description
 */
public interface IServiceProvider {

    /**
     * 发布服务
     * @param rpcServiceConfig
     */
    void publishService(RpcServiceConfig rpcServiceConfig);

    /**
     * 根据rpc服务名获得对应的服务
     * @param rpcServiceName
     * @return
     */
    Object getService(String rpcServiceName);

}
