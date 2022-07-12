package com.lucky845.zookeeper.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;

/**
 * @author lucky845
 * @description
 * @date 2022/07/12
 */
@Slf4j
public class CuratorUtils {

    /**
     * 获取curator客户端
     */
    public static CuratorFramework getCuratorFramework(String connectString, Integer sessionTimeout, RetryPolicy retryPolicy) {
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(connectString)
                .connectionTimeoutMs(sessionTimeout)
                .retryPolicy(retryPolicy)
                .build();
        // 启动客户端
        client.start();
        log.info("zookeeper 启动成功");
        return client;
    }

}
