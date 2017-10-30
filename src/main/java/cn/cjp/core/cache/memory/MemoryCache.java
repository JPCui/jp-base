package cn.cjp.core.cache.memory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import cn.cjp.core.cache.Cache;
import cn.cjp.core.cache.CacheCallback;
import cn.cjp.utils.Logger;

public class MemoryCache implements Cache, Runnable {

    static final Logger LOGGER = Logger.getLogger(MemoryCache.class);

    final String DEFAULT_DOMAIN_KEY = "";

    String domainKey;

    ConcurrentHashMap<String, Object> cacheMap = new ConcurrentHashMap<>();

    ConcurrentHashMap<String, Long> cacheExpireMap = new ConcurrentHashMap<>();

    Executor executor;

    public MemoryCache() {
        this.startClearCacheThread();
    }

    public Executor getExecutor() {
        if (executor == null) {
            executor = Executors.newSingleThreadExecutor();
        }
        return executor;
    }

    public String getDomainKey() {
        if (this.domainKey == null) {
            this.domainKey = DEFAULT_DOMAIN_KEY;
        }
        return this.domainKey;
    }

    public void setDomainKey(String domainKey) {
        this.domainKey = domainKey;
    }

    /**
     * 
     */
    private void startClearCacheThread() {
        this.getExecutor().execute(this);
    }

    @Override
    public void set(String key, Object value, long expireTime) {
        this.cacheMap.put(key, value);
        this.cacheExpireMap.put(key, expireTime);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(Class<T> c, String key) {
        return (T) this.cacheMap.get(key);
    }

    @Override
    public Long delete(String key) {
        this.cacheMap.remove(key);
        this.cacheExpireMap.remove(key);
        return 1L;
    }

    @Override
    public Long delete(Collection<String> key) {
        key.forEach(k -> {
            this.cacheMap.remove(k);
            this.cacheExpireMap.remove(k);
        });
        return 1L;
    }

    @Override
    public Long sadd(String key, Object... values) {
        @SuppressWarnings({ "unchecked" })
        Collection<Object> list = (Collection<Object>) this.cacheMap.get(key);
        if (list == null) {
            list = this.createEmptySet();
            this.cacheMap.put(key, list);
        }
        list.addAll(Arrays.asList(values));
        return (long) values.length;
    }

    private Collection<Object> createEmptySet() {
        Collection<Object> list = new ConcurrentSkipListSet<>();
        return list;
    }

    @Override
    public <T> List<T> smembers(Class<T> c, String key) {
        @SuppressWarnings("unchecked")
        Collection<T> list = (Collection<T>) this.cacheMap.get(key);
        if (list == null) {
            return Collections.emptyList();
        }

        List<T> newList = new ArrayList<>();
        newList.addAll(list);
        return Collections.unmodifiableList(newList);
    }

    @Override
    public <T> T execute(CacheCallback<T> callback) {
        return callback.doIntern(this);
    }

    @Override
    public String getDomainKey(String key) {
        return domainKey + ":" + key;
    }

    @Override
    public Set<String> keys(String keyPrefix) {
        return this.cacheMap.keySet().stream().filter(k -> {
            if (k.startsWith(keyPrefix)) {
                return true;
            }
            return false;
        }).collect(Collectors.toSet());
    }

    @Override
    public void run() {
        boolean interrupt = false;
        while (!interrupt) {
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                interrupt = true;
            }

            List<String> cacheKeySet = new ArrayList<>(cacheExpireMap.keySet());
            for (String cacheKey : cacheKeySet) {
                Long expire = cacheExpireMap.get(cacheKey);
                if (expire != null && expire > System.currentTimeMillis()) {
                    cacheExpireMap.remove(cacheKey);
                    cacheMap.remove(cacheKey);
                    LOGGER.debug(String.format("Clear Cache(%s)", cacheKey));
                }
            }
        }
    }
}
