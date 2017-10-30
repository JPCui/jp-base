package cn.cjp.base.cache.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.scheduling.annotation.Scheduled;

import cn.cjp.utils.Logger;
import cn.cjp.utils.StringUtil;

/**
 * 利用map实现的缓存工具类
 * 
 * @author sucre
 *
 */
public class Cache {

    private static final Logger LOGGER = Logger.getLogger(Cache.class);

    /**
     * 缓存map
     */
    private static final Map<String, Object> cacheMap = new ConcurrentHashMap<>();

    /**
     * 缓存时长
     */
    private static final Map<String, Long> cacheExpire = new ConcurrentHashMap<>();

    /**
     * second for 1 hour
     */
    public static final int ONE_HOUR = 3600;

    /**
     * 超时清除
     */
    @Scheduled(fixedDelay = 60_000)
    public void clearTask() {
        List<String> cacheKeySet = new ArrayList<>(cacheExpire.keySet());
        for (String cacheKey : cacheKeySet) {
            Long expire = cacheExpire.get(cacheKey);
            if (expire != null && expire > System.currentTimeMillis()) {
                cacheExpire.remove(cacheKey);
                cacheMap.remove(cacheKey);
                LOGGER.debug(String.format("Clear Cache(%s)", cacheKey));
            }
        }
    }

    /**
     * 
     * @param key
     * @param value
     * @param time
     *            超时时长，0 永不超时
     */
    public void set(String key, Object value, int second) {
        if (!StringUtil.isEmpty(key)) {
            cacheMap.put(key, value);
            if (second != 0) {
                long currentTime = System.currentTimeMillis();
                cacheExpire.put(key, currentTime + second * 1000);
            }
        }
    }

    public Object get(String key) {
        Long expire = cacheExpire.get(key);
        if (expire != null) {
            Long currentTime = System.currentTimeMillis();
            if (expire < currentTime) {
                // 过期
                cacheExpire.remove(key);
                cacheMap.remove(key);
                return null;
            }
        }
        return cacheMap.get(key);
    }

}
