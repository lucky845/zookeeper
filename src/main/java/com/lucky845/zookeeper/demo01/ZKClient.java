package com.lucky845.zookeeper.demo01;

import com.lucky845.zookeeper.constant.ZKConstant;
import com.lucky845.zookeeper.utils.ZKUtils;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author lucky845
 * @description 测试zk api
 * @date 2022/07/12
 */
public class ZKClient {

    private ZooKeeper zkClient;

    /**
     * 初始化zk客户端
     */
    @Before
    public void init() throws Exception {
        zkClient = ZKUtils.getZKClient(ZKConstant.CONNECT_STRING, ZKConstant.SESSION_TIMEOUT, watchedEvent -> {
        });
    }

    /**
     * 创建子节点
     */
    @Test
    public void creat() throws Exception {
        String nodeCreate = zkClient.create("/lucky845", "lucky".getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("nodeCreate = " + nodeCreate);
    }

    /**
     * 获取子节点,并监听子节点状态
     */
    @Test
    public void getChildren() throws Exception {
        List<String> childrenList = zkClient.getChildren("/", true);
        childrenList.forEach(System.out::println);
        // 延时
        ZKUtils.sleep(Long.MAX_VALUE);
    }

    /**
     * 判断某个子节点是否存在
     */
    @Test
    public void exist() throws Exception {
        Stat stat = zkClient.exists("/lucky845", false);
        System.out.println(stat == null ? ZKConstant.NOT_EXIST : ZKConstant.EXIST);
    }

}
