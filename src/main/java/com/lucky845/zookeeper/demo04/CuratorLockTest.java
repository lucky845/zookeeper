package com.lucky845.zookeeper.demo04;

import com.lucky845.zookeeper.constant.ZKConstant;
import com.lucky845.zookeeper.utils.CuratorUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

/**
 * @author lucky845
 * @description 测试使用curator作为分布式锁
 * @date 2022/07/12
 */
@Slf4j
public class CuratorLockTest {

    public static void main(String[] args) {

        // 创建分布式锁1
        InterProcessMutex lock1 = new InterProcessMutex(
                CuratorUtils.getCuratorFramework(ZKConstant.CONNECT_STRING, ZKConstant.SESSION_TIMEOUT, ZKConstant.POLICY),
                "/locks");

        // 创建分布式锁2
        InterProcessMutex lock2 = new InterProcessMutex(
                CuratorUtils.getCuratorFramework(ZKConstant.CONNECT_STRING, ZKConstant.SESSION_TIMEOUT, ZKConstant.POLICY),
                "/locks");

        getLock(lock1, "线程1");
        getLock(lock2, "线程2");

    }

    private static void getLock(InterProcessMutex lock, String threadName) {
        new Thread(() -> {
            try {
                lock.acquire();
                log.info(Thread.currentThread().getName() + "启动，获取到锁");
                lock.acquire();
                log.info(Thread.currentThread().getName() + "再次获取到锁");
                lock.release();
                log.info(Thread.currentThread().getName() + "释放锁");
                lock.release();
                log.info(Thread.currentThread().getName() + "再次释放锁");
                Thread.sleep(5 * 1000);
            } catch (Exception e) {
                log.error("线程sleep异常，异常信息为:{}", e.getMessage());
            }
        }, threadName);
    }

}
