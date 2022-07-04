package xyz.raygetoffer.zk;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author mingruiwu
 * @create 2022/7/2 14:30
 * @description 使用Curator操作ZooKeeper
 */
public class ZkTest {
    private static final int BASE_SLEEP_TIME = 1000;
    private static final int MAX_RETRIES = 3;

    public static void main(String[] args) throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(BASE_SLEEP_TIME, MAX_RETRIES);
        CuratorFramework zkClient = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181")
                .retryPolicy(retryPolicy)
                .build();
        zkClient.start();


        /*
        1. 创建节点
         */
        // 创建节点
//        zkClient.create().forPath("/node1");
        // 保证父节点不存在时自动创建父节点，同时设置节点的模式
//        zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath("/node2/test");
        // 创建临时节点
//        zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/node2/test1");
//        Thread.sleep(20000);
//        zkClient.close();

        // 创建节点并指定数据内容
//        zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/node1/test1", "java".getBytes(StandardCharsets.UTF_8));
//        System.out.println(new String(zkClient.getData().forPath("/node1/test1"), StandardCharsets.UTF_8));

        // 检测节点是否创建成功
//        Stat stat = zkClient.checkExists().forPath("/node1");
//        System.out.println(stat);


        /*
        2. 删除节点
         */
        // 删除一个节点
//        zkClient.delete().forPath("/node1");

        // 删除一个节点以及其下的所有子节点
//        zkClient.delete().deletingChildrenIfNeeded().forPath("/node1");

        /*
        3. 获取/更新节点数据内容
         */
//        zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/node1/00001","java".getBytes());
//        System.out.println(new String(zkClient.getData().forPath("/node1/00001")));
//        zkClient.setData().forPath("/node1/00001","c++".getBytes());//更新节点数据内容

        /*
        4. 获取某节点的所有子节点的路径
         */
//        List<String> childrenPaths = zkClient.getChildren().forPath("/node1");
//        System.out.println(childrenPaths);

        /*
        5. 注册监听器
         */
        String path = "/node3";
        PathChildrenCache pathChildrenCache = new PathChildrenCache(zkClient, path, false);
        PathChildrenCacheListener pathChildrenCacheListener = new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                if (event.getType() == PathChildrenCacheEvent.Type.CHILD_ADDED) {
                    System.out.println("增加节点");
                }
            }
        };
        pathChildrenCache.getListenable().addListener(pathChildrenCacheListener);
        pathChildrenCache.start();

//        Thread.sleep(1000);
//        zkClient.delete().forPath("/node1");
        Thread.sleep(100000);
    }
}
