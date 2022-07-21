package xyz.raygetoffer.loadbalance.impl;

import xyz.raygetoffer.loadbalance.AbstractLoadBalance;
import xyz.raygetoffer.remoting.dto.RpcRequest;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author mingruiwu
 * @create 2022/7/14 18:40
 * @description
 */
public class PollLoadBalance extends AbstractLoadBalance {
    private ConcurrentHashMap<String, AtomicInteger> concurrentHashMap = new ConcurrentHashMap<>();
    @Override
    protected String doSelect(List<String> serviceUrlList, RpcRequest rpcRequest) {
        String rpcServiceName = rpcRequest.getRpcServiceName();
        AtomicInteger atomicInteger = concurrentHashMap.computeIfAbsent(rpcServiceName, k -> new AtomicInteger(0));
        return serviceUrlList.get(atomicInteger.incrementAndGet() % serviceUrlList.size());
    }
}
