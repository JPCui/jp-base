package cn.cjp.core.lock;

import cn.cjp.core.redis.IRedisDao;

public class DistributeLock {

    IRedisDao redis;

    /**
     * 默认锁的超时时间，30s
     */
    static final int DEFAULT_KEY_TIMEOUT = 30;

    /**
     * 尝试加锁，会在一段时间内重复加锁，直到加锁成功 或 超时
     * 
     * @param key
     * @param millis
     *            尝试加锁时长，单位：秒
     * @return 加锁失败返回false，or true
     * @throws InterruptedException
     */
    public boolean tryLock(String key, long millis) throws InterruptedException {
        long deadline = (System.currentTimeMillis() + millis);
        String deadlineStr = deadline + "";
        if (redis.setnx(key, deadlineStr) == 1) {
            redis.expire(key, DEFAULT_KEY_TIMEOUT);
            return true;
        }

        String oldDeadlineStr = redis.get(key);
        long oldDeadline = Long.parseLong(oldDeadlineStr);

        if (oldDeadline < deadline) {
            // 之前的锁已超时
            String oldDeadlineStr2 = redis.getset(key, deadlineStr);
            if (oldDeadlineStr.equals(oldDeadlineStr2)) {
                // 成功加锁
                return true;
            }
        }

        return false;
    }

    public void unlock(String key) {
        long currentTime = System.currentTimeMillis();
        String oldDeadlineStr = redis.get(key);
        long oldDeadline = Long.parseLong(oldDeadlineStr);

        if (currentTime < oldDeadline) {
            // 未超时，则可以进行删除
            redis.del(key);
        }
    }

}