package xyz.raygetoffer.loadbalance;

import xyz.raygetoffer.extension.SPI;
import xyz.raygetoffer.remoting.dto.RpcRequest;

import java.util.List;

/**
 * 负载均衡策略
 *
 * @author mingruiwu
 * @create 2022/7/6 14:46
 * @description
 */
@SPI(value = "consistentHash")
public interface ILoadBalance {
    /**
     * 通过负载均衡策略从服务地址列表中选择一个目标地址
     * @param serviceUrlList
     * @param rpcRequest
     * @return 目标服务地址
     */
    String selectServiceAddress(List<String> serviceUrlList, RpcRequest rpcRequest);
}
