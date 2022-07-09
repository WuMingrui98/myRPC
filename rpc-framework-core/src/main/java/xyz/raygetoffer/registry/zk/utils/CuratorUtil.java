package xyz.raygetoffer.registry.zk.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import xyz.raygetoffer.enums.RpcConfigEnum;
import xyz.raygetoffer.utils.PropertiesFileUtil;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * ZooKeeper客户端工具
 *
 * @author mingruiwu
 * @create 2022/7/6 10:55
 * @description
 */
@Slf4j
public class CuratorUtil {
    private static final int BASE_SLEEP_TIME = 1000;
    private static final int MAX_RETRIES = 3;
    private static final String ZK_REGISTER_ROOT_PATH = "/my-rpc";
    private static final Map<String, List<String>> SERVICE_ADDRESS_MAP = new ConcurrentHashMap<>();
    private static final Set<String> REGISTER_PATH_SET = ConcurrentHashMap.newKeySet();
    private static CuratorFramework zkClient;
    private static final String DEFAULT_ZOOKEEPER_ADDRESS = "127.0.0.1:2181";


    private CuratorUtil() {
    }

    /**
     * 获得ZooKeeper客户端实例
     *
     * @return
     */
    public static CuratorFramework getZkClient() {
        // 如果zkClient开启，则直接返回
        if (zkClient != null && zkClient.getState() == CuratorFrameworkState.STARTED) {
            return zkClient;
        }
        Properties properties = PropertiesFileUtil.readProperties(RpcConfigEnum.RPC_CONFIG_PATH.getPropertyValue());
        String zookeeperAddress = DEFAULT_ZOOKEEPER_ADDRESS;
        if (properties != null) {
            String configAddress = properties.getProperty(RpcConfigEnum.ZK_ADDRESS.getPropertyValue());
            if (configAddress != null) {
                zookeeperAddress = configAddress;
            }
        }
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(BASE_SLEEP_TIME, MAX_RETRIES);
        zkClient = CuratorFrameworkFactory.builder()
                .connectString(zookeeperAddress)
                .retryPolicy(retryPolicy)
                .build();
        zkClient.start();
        try {
            // 给30s连接zookeeper的时间
            if (!zkClient.blockUntilConnected(30, TimeUnit.SECONDS)) {
                throw new RuntimeException("Time out waiting to connect to ZK!");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return zkClient;
    }

    /**
     * 创建持久节点
     *
     * @param zkClient
     * @param path     节点路径
     */
    public static void createPersistentNode(CuratorFramework zkClient, String path) {
        try {
            if (REGISTER_PATH_SET.contains(path) || zkClient.checkExists().forPath(path) != null) {
                log.info("The node already exists. The node is:[{}]", path);
            } else {
                //eg: /my-rpc/github.javaguide.HelloService/127.0.0.1:9999
                zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path);
                log.info("The node was created successfully. The node is:[{}]", path);
            }
            REGISTER_PATH_SET.add(path);
        } catch (Exception e) {
            log.error("The node was created fail. The node is:[{}]", path);
        }
    }

    /**
     * 创建持久节点
     *
     * @param zkClient
     * @param rpcServiceName        rpc服务名
     * @param inetSocketAddress     ip+port
     */
    public static void createPersistentNode(CuratorFramework zkClient, String rpcServiceName, InetSocketAddress inetSocketAddress) {
        String path = ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName + inetSocketAddress.toString();
        createPersistentNode(zkClient, path);
    }

    /**
     * 获得服务节点下的子节点
     *
     * @param zkClient
     * @param rpcServiceName rpc的服务名
     * @return 返回rpc服务节点下的所有子节点
     */
    public static List<String> getChildrenNodes(CuratorFramework zkClient, String rpcServiceName) {
        if (SERVICE_ADDRESS_MAP.containsKey(rpcServiceName)) {
            return SERVICE_ADDRESS_MAP.get(rpcServiceName);
        }
        String servicePath = ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName;
        List<String> serviceAddresses = null;
        try {
            serviceAddresses = zkClient.getChildren().forPath(servicePath);
            // 更新SERVICE_ADDRESS_MAP
            SERVICE_ADDRESS_MAP.put(rpcServiceName, serviceAddresses);
            registerWatcher(zkClient, rpcServiceName);
        } catch (Exception e) {
            log.error("get children nodes for path [{}] fail", servicePath);
        }
        return serviceAddresses;
    }


    /**
     * 在节点上注册监听器，监听节点数量的变化，当节点数量变化时更新SERVICE_ADDRESS_MAP
     *
     * @param zkClient
     * @param rpcServiceName rpc服务的名称
     * @throws Exception
     */
    private static void registerWatcher(CuratorFramework zkClient, String rpcServiceName) throws Exception {
        String servicePath = ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName;
        PathChildrenCache pathChildrenCache = new PathChildrenCache(zkClient, servicePath, true);
        PathChildrenCacheListener pathChildrenCacheListener = new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                List<String> serviceAddresses = client.getChildren().forPath(servicePath);
                // 更新SERVICE_ADDRESS_MAP
                SERVICE_ADDRESS_MAP.put(rpcServiceName, serviceAddresses);
            }
        };
        pathChildrenCache.getListenable().addListener(pathChildrenCacheListener);
        pathChildrenCache.start();
    }


    /**
     * 注销zookeeper节点
     *
     * @param zkClient
     * @param inetSocketAddress ip:port
     */
    public static void clearRegistry(CuratorFramework zkClient, InetSocketAddress inetSocketAddress) {
        Set<String> removeSet = ConcurrentHashMap.newKeySet();
        REGISTER_PATH_SET.stream().parallel().forEach(s -> {
            try {
                if (s.endsWith(inetSocketAddress.toString())) {
                    zkClient.delete().forPath(s);
                    removeSet.add(s);
                }
            } catch (Exception e) {
                log.error("clear registry for path [{}] fail", s);
            }
        });
        log.info("All registered services on the server are cleared:[{}]", removeSet);
        REGISTER_PATH_SET.removeAll(removeSet);
    }
}
