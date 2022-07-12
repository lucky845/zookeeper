package com.lucky845.zookeeper.demo03;

import lombok.extern.slf4j.Slf4j;

/**
 * @author lucky845
 * @description 测试zk分布式锁
 * @date 2022/07/12
 */
@Slf4j
public class DistributedLockTest {

    public static void main(String[] args) {

        final DistributedLock lock1 = new DistributedLock();
        final DistributedLock lock2 = new DistributedLock();

        getLock(lock1, "线程1 ");
        getLock(lock2, "线程2 ");

    }

    private static void getLock(DistributedLock lock, String threadName) {
        new Thread(() -> {
            lock.zkLock();
            log.info(Thread.currentThread().getName() + "启动，获取到锁");
            try {
                Thread.sleep(5 * 1000);
            } catch (InterruptedException e) {
                log.error("线程sleep异常，异常信息为:{}", e.getMessage());
            }
        }, threadName);
    }

}
