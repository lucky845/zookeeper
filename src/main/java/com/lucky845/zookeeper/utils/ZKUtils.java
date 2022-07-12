package com.lucky845.zookeeper.utils;

import com.lucky845.zookeeper.constant.ZKConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lucky845
 * @description zk工具类
 * @date 2022/07/12
 */
@Slf4j
public class ZKUtils {

    /**
     * 获取zk客户端
     *
     * @param connectString  zk服务地址
     * @param sessionTimeout 连接超时间
     * @return zk客户端
     */
    public static ZooKeeper getZKClient(String connectString, Integer sessionTimeout, Watcher watcher) throws Exception {
        return new ZooKeeper(connectString, sessionTimeout, watcher);
    }

    /**
     * 创建子节点
     *
     * @param zkClient   zk客户端
     * @param path       子节点路径
     * @param data       节点的初始数据
     * @param acl        节点的acl
     * @param createMode 指定要创建的节点是临时的和/或顺序的
     * @return zk节点信息
     */
    public static String creatChildrenNode(ZooKeeper zkClient, String path, String data, List<ACL> acl, CreateMode createMode) throws Exception {
        return zkClient.create(path, data.getBytes(StandardCharsets.UTF_8), acl, createMode);
    }

    /**
     * 获取服务列表
     *
     * @param zkClient zk客户端连接
     * @param path     节点路径
     * @param watch    是否监视
     * @return zk服务列表
     */
    public static List<String> getServerList(ZooKeeper zkClient, String path, Boolean watch) throws Exception {
        List<String> childrenList = zkClient.getChildren(path, watch);
        return childrenList.stream().map(children -> {
            byte[] data = null;
            try {
                data = zkClient.getData(path + children, watch, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new String(data);
        }).collect(Collectors.toList());
    }

    /**
     * 删除子节点
     *
     * @param zkClient zk客户端
     * @param path     子节点路径
     * @param version  预期的节点版本
     */
    public static void deleteChildrenNode(ZooKeeper zkClient, String path, Integer version) throws Exception {
        zkClient.delete(path, version);
    }

    /**
     * 判断节点是否存在
     *
     * @param zkClient zk客户端
     * @param path     节点路径
     * @param watch    是否监视
     * @return 节点是否存在
     */
    public static String exist(ZooKeeper zkClient, String path, Boolean watch) throws Exception {
        Stat stat = zkClient.exists(path, watch);
        return stat == null ? ZKConstant.NOT_EXIST : ZKConstant.EXIST;
    }

    /**
     * 模拟处理业务逻辑
     *
     * @param millis 时间
     */
    public static void sleep(Long millis) throws Exception {
        Thread.sleep(millis);
    }

}
