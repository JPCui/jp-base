package cn.cjp.core.cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import cn.cjp.core.cache.memory.MemoryCache;
import cn.cjp.utils.Logger;
import cn.cjp.utils.StringUtil;

public class CacheManager {

    static final Logger LOGGER = Logger.getLogger(CacheManager.class);

    static final String GROUP_FORMAT = "G:%s";

    Cache cache;

    public CacheManager() {
        cache = new MemoryCache();
    }

    public CacheManager(Cache cache) {
        this.cache = cache;
    }

    public void saveCacheGroup(EvaluationContext ctx, Cacheable cacheable, String cacheKey) {
        String[] groups = cacheable.group();
        for (String group : groups) {
            group = String.format(GROUP_FORMAT, group);
            String groupCacheKey = this.getCacheKey(ctx, group, cacheable.args());
            this.cache.sadd(groupCacheKey, cacheKey);
            LOGGER.debug(String.format("save cache -> group : %s, members : %s", groupCacheKey, cacheKey));
        }
    }

    public void delByCacheGroup(EvaluationContext ctx, CacheEvict cacheEvict) {
        String[] groups = cacheEvict.group();
        for (String group : groups) {
            group = String.format(GROUP_FORMAT, group);
            String groupCacheKey = this.getCacheKey(ctx, group, cacheEvict.args());
            this.delByCacheGroup(ctx, groupCacheKey);
        }
    }

    public void delByCacheGroup(EvaluationContext ctx, String groupCacheKey) {

        this.cache.execute(new CacheCallback<Object>() {

            @Override
            public Long doIntern(Cache cache) {
                List<String> keys = cache.smembers(String.class, cache.getDomainKey(groupCacheKey));
                if (!keys.isEmpty()) {
                    List<String> vs2 = new ArrayList<>();
                    keys.forEach(v -> {
                        vs2.add(cache.getDomainKey(v));
                    });
                    LOGGER.debug(String.format("del cache : %s", vs2.toString()));
                    return cache.delete(vs2);
                }
                return 0L;
            }
        });

        this.cache.execute(new CacheCallback<Long>() {

            @Override
            public Long doIntern(Cache cache) {
                List<String> vs = cache.smembers(String.class, groupCacheKey);
                if (!vs.isEmpty()) {
                    List<String> vs2 = new ArrayList<>();
                    vs.forEach(v -> {
                        vs2.add(v);
                    });
                    LOGGER.debug(String.format("del cache : %s", vs2.toString()));
                    return cache.delete(vs2);
                }
                return 0L;
            }
        });

        LOGGER.debug(String.format("del cache group : %s", groupCacheKey));
    }

    public void saveCache(String key, Object value, long expireTime) {
        LOGGER.debug(String.format("save cache : %s", key));
        this.cache.set(key, value, expireTime);
    }

    public <T> T getCache(Class<T> returnType, String key) {
        T value = this.cache.get(returnType, key);
        return value;
    }

    public void clearCache(EvaluationContext ctx, CacheEvict cacheEvict) {

        // 按key删除
        String[] keys = cacheEvict.key();
        for (String key : keys) {
            if (!StringUtil.isEmpty(key)) {
                key = this.getCacheKey(ctx, key, cacheEvict.args());
                this.cache.delete(key);
                LOGGER.debug(String.format("del cache : %s", key));
            }
        }

        // 按keyPrefix删除
        Arrays.asList(cacheEvict.keyPrefix()).forEach(keyPrefix -> {
            String k = this.getCacheKey(ctx, keyPrefix, new String[0]);
            this.cache.execute(new CacheCallback<Object>() {

                @Override
                public Object doIntern(Cache cache) {
                    Set<String> keys = cache.keys(k);
                    if (!keys.isEmpty()) {
                        cache.delete(keys);
                    }
                    return null;
                }
            });
            LOGGER.debug(String.format("del cache : %s", k));
        });

        // 按group删除
        this.delByCacheGroup(ctx, cacheEvict);
    }

    /**
     * 
     * @param joinPoint
     * @param keyEl
     *            key 的EL表达式
     * @return
     */
    public String getCacheKey(EvaluationContext ctx, String keyEl, String[] args) {

        Object[] argValues = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            ExpressionParser parser = new SpelExpressionParser();
            Object v = parser.parseExpression(arg).getValue(ctx);
            String argValue = null;
            if (v != null) {
                argValue = v.toString();
            }
            argValues[i] = argValue;
        }
        String cacheKey = String.format(keyEl, argValues);
        return cacheKey;
    }

    public EvaluationContext getArgValues(ProceedingJoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        String[] paramNames = methodSignature.getParameterNames();
        Object[] paramValues = joinPoint.getArgs();
        return this.getArgValues(paramNames, paramValues);
    }

    public EvaluationContext getArgValues(String[] paramNames, Object[] paramValues) {
        // 使用直接量表达式
        EvaluationContext ctx = new StandardEvaluationContext();
        for (int i = 0; i < paramNames.length; i++) {
            String paramName = paramNames[i];
            Object paramValue = paramValues[i];

            ctx.setVariable(paramName, paramValue);
        }
        return ctx;
    }

}
