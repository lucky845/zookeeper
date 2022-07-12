package com.lucky845.zookeeper.demo03;

import com.lucky845.zookeeper.constant.ZKConstant;
import com.lucky845.zookeeper.utils.ZKUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher.Event;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author lucky845
 * @description 测试zk分布式锁
 * @date 2022/07/12
 */
@Slf4j
public class DistributedLock {

    private ZooKeeper zkClient;
    private final CountDownLatch connectLatch = new CountDownLatch(1);
    private final CountDownLatch waitLatch = new CountDownLatch(1);
    private String waitPath;
    private String currentMode;

    public DistributedLock() {
        // 1. 获取连接
        try {
            zkClient = ZKUtils.getZKClient(ZKConstant.CONNECT_STRING, ZKConstant.SESSION_TIMEOUT, watcher -> {
                // connectLatch 如果连接上zk 可以释放
                Event.KeeperState state = watcher.getState();
                if (state == Event.KeeperState.SaslAuthenticated) {
                    connectLatch.countDown();
                }
                // waitLatch 需要释放
                Event.EventType type = watcher.getType();
                String path = watcher.getPath();
                if (type == Event.EventType.NodeDeleted && path.equals(waitPath)) {
                    waitLatch.countDown();
                }
            });
            // 等待zk正常连接后再继续执行
            connectLatch.await();
            // 2. 判断根节点/locks是否存在
            String isExist = ZKUtils.exist(zkClient, "/locks", false);
            if (isExist.equals(ZKConstant.NOT_EXIST)) {
                // 如果节点不存在就创建根节点
                ZKUtils.creatChildrenNode(zkClient, "/locks", "locks", ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (Exception e) {
            log.error("初始化异常,错误信息为:{}", e.getMessage());
        }
    }

    /**
     * 上锁
     */
    public void zkLock() {
        // 1. 创建对应的临时带序号节点
        try {
            currentMode = ZKUtils.creatChildrenNode(zkClient, "/locks/" + "seq-", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            // 2. 判断创建的节点是否是最小的序号节点，如果是获取到锁，如果不是，监听他序号前一个节点
            List<String> children = zkClient.getChildren("/locks", false);
            if (children.size() == 1) {
                // 如果children只有一个值，那就直接获取锁
                return;
            } else {
                // 如果有多个节点，那就需要判断谁最小
                Collections.sort(children);
                // 获取对应的节点名称
                String thisNode = currentMode.substring("/locks".length());
                // 通过节点名称获取该节点在children的位置
                int index = children.indexOf(thisNode);
                if (-1 == index) {
                    log.error("数据异常");
                } else if (0 == index) {
                    // 就是第一个节点，可以获取到锁
                    return;
                } else {
                    // 需要监听前一个节点变化
                    waitPath = "/locks/" + children.get(index - 1);
                    zkClient.getData(waitPath, true, null);
                    // 等待监听
                    waitLatch.await();
                    return;
                }
            }
        } catch (Exception e) {
            log.error("上锁异常,异常信息为:{}", e.getMessage());
        }
    }

    /**
     * 解锁
     */
    public void zkUnLock() {
        // 删除节点
        try {
            ZKUtils.deleteChildrenNode(zkClient, currentMode, -1);
        } catch (Exception e) {
            log.error("删除节点保存,错误为:{}", e.getMessage());
        }
    }

}
