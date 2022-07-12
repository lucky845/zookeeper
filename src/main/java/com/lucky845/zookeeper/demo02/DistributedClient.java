package com.lucky845.zookeeper.demo02;

import com.lucky845.zookeeper.constant.ZKConstant;
import com.lucky845.zookeeper.utils.ZKUtils;
import org.apache.zookeeper.ZooKeeper;

import java.util.List;

/**
 * @author lucky845
 * @description 测试分布客户端
 * @date 2022/07/12
 */
public class DistributedClient {

    private ZooKeeper zkClient;

    public static void main(String[] args) throws Exception {

        DistributedClient client = new DistributedClient();
        // 1. 获取zk连接
        client.getConnect();

        // 2. 监听/servers下面的节点的变化
        client.getServerList();

        // 3. 业务逻辑
        client.businese();

    }

    /**
     * 处理业务逻辑
     */
    private void businese() throws Exception {
        ZKUtils.sleep(Long.MAX_VALUE);
    }

    /**
     * 获取服务列表
     */
    private void getServerList() throws Exception {
        List<String> servers = ZKUtils.getServerList(zkClient, "/servers", true);
        // 打印
        servers.forEach(System.out::println);
    }

    /**
     * 获取zk连接
     */
    private void getConnect() throws Exception {
        zkClient = ZKUtils.getZKClient(ZKConstant.CONNECT_STRING, ZKConstant.SESSION_TIMEOUT, watchedEvent -> {
        });
    }

}
