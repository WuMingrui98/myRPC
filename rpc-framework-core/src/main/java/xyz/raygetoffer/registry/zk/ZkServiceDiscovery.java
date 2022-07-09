package xyz.raygetoffer.registry.zk;

import org.apache.curator.framework.CuratorFramework;
import xyz.raygetoffer.enums.RpcErrorMessageEnum;
import xyz.raygetoffer.exception.RpcException;
import xyz.raygetoffer.loadbalance.LoadBalance;
import xyz.raygetoffer.loadbalance.impl.RandomLoadBalance;
import xyz.raygetoffer.registry.IServiceDiscovery;
import xyz.raygetoffer.registry.zk.utils.CuratorUtil;
import xyz.raygetoffer.remoting.dto.RpcRequest;
import xyz.raygetoffer.utils.CollectionUtil;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * 使用zookeeper实现注册发现
 *
 * @author mingruiwu
 * @create 2022/7/6 10:54
 * @description
 */
public class ZkServiceDiscovery implements IServiceDiscovery {
    // TODO 根据定义选择负载均衡策略
    private final LoadBalance loadBalance = new RandomLoadBalance();


    @Override
    public InetSocketAddress discoverService(RpcRequest rpcRequest) {
        String rpcServiceName = rpcRequest.getRpcServiceName();
        CuratorFramework zkClient = CuratorUtil.getZkClient();
        List<String> serviceUrlList = CuratorUtil.getChildrenNodes(zkClient, rpcServiceName);
        if (CollectionUtil.isEmpty(serviceUrlList)) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND, rpcServiceName);
        }

        String url = loadBalance.selectServiceAddress(serviceUrlList, rpcRequest);
        String[] hostAndPort = url.split(":");
        String hostname = hostAndPort[0];
        int port = Integer.parseInt(hostAndPort[1]);
        return new InetSocketAddress(hostname, port);
    }
}
