package xyz.raygetoffer.loadbalance.impl;

import xyz.raygetoffer.loadbalance.AbstractLoadBalance;
import xyz.raygetoffer.remoting.dto.RpcRequest;

import java.util.List;
import java.util.Random;

/**
 * 采用随机策略的负载均衡算法
 *
 * @author mingruiwu
 * @create 2022/7/9 14:58
 * @description
 */
public class RandomLoadBalance extends AbstractLoadBalance {
    @Override
    protected String doSelect(List<String> serviceUrlList, RpcRequest rpcRequest) {
        Random random = new Random();
        return serviceUrlList.get(random.nextInt(serviceUrlList.size()));
    }
}
