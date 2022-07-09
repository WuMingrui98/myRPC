package xyz.raygetoffer.registry.zk;

import xyz.raygetoffer.loadbalance.LoadBalance;
import xyz.raygetoffer.loadbalance.impl.RandomLoadBalance;
import xyz.raygetoffer.registry.IServiceRegistry;
import xyz.raygetoffer.registry.zk.utils.CuratorUtil;

import java.net.InetSocketAddress;

/**
 * 使用zookeeper注册服务
 *
 * @author mingruiwu
 * @create 2022/7/6 10:54
 * @description
 */
public class ZkServiceRegistry implements IServiceRegistry {

    @Override
    public void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress) {
        CuratorUtil.createPersistentNode(CuratorUtil.getZkClient(), rpcServiceName, inetSocketAddress);
    }
}
