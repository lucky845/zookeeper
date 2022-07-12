package com.lucky845.zookeeper.constant;

import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author lucky845
 * @description zk常量类
 * @date 2022/07/12
 */
public class ZKConstant {

    /**
     * zk服务地址
     */
    public static final String CONNECT_STRING = "localhost:2181";

    /**
     * 连接超时时间
     */
    public static final Integer SESSION_TIMEOUT = 2000;

    /**
     * 存在
     */
    public static final String EXIST = "exist";

    /**
     * 不存在
     */
    public static final String NOT_EXIST = "not exist";

    /**
     * 重试
     */
    public static final ExponentialBackoffRetry POLICY = new ExponentialBackoffRetry(3000, 3);

}
