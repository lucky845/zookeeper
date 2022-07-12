package com.lucky845.zookeeper.demo02;

import com.lucky845.zookeeper.constant.ZKConstant;
import com.lucky845.zookeeper.utils.ZKUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

/**
 * @author lucky845
 * @description 测试分布服务器
 * @date 2022/07/12
 */
public class DistributedServer {

    private ZooKeeper zkClient;

    public static void main(String[] args) throws Exception {

        DistributedServer server = new DistributedServer();
        // 1. 获取zk连接
        server.getConnect();

        // 2. 注册服务器到zk集群
        server.regist(args[0]);

        // 3. 启动业务逻辑
        server.businese();

    }

    /**
     * 执行业务逻辑
     */
    private void businese() throws Exception {
        ZKUtils.sleep(Long.MAX_VALUE);
    }

    /**
     * 注册服务器到zk集群
     *
     * @param data 节点初始数据
     */
    private void regist(String data) throws Exception {
        String createNode = ZKUtils.creatChildrenNode(zkClient, "/servers", data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(data + "is online");
    }

    /**
     * 获取zk连接
     */
    private void getConnect() throws Exception {
        zkClient = ZKUtils.getZKClient(ZKConstant.CONNECT_STRING, ZKConstant.SESSION_TIMEOUT, watchedEvent -> {
        });
    }
}
