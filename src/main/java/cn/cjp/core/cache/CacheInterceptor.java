package cn.cjp.cache;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.EvaluationContext;
import org.springframework.stereotype.Component;

import cn.cjp.utils.Logger;

/**
 * Redis 管理缓存，仿 <a href=
 * "https://www.ibm.com/developerworks/cn/opensource/os-cn-spring-cache/">SpringCache</a><br>
 * 
 * 区别：因为SpringCache是全加载到内存中做缓存，只有APP启动和关闭时，才会与Redis打交道，所以自定义<br>
 * 
 * <ol>
 * NOTICE
 * <li>同样，使用 this.xxx 调用的方法不起作用
 * 
 * @author sucre
 */
@Aspect
@Component
public class RedisCacheInterceptor {

    static final Logger LOGGER = Logger.getLogger(RedisCacheInterceptor.class);

    @Autowired
    RedisTemplateService redisTemplateService;

    @Autowired
    RedisCacheManager redisCacheManager;

    @Around("@annotation(cacheable)")
    public Object aroundCacheable(ProceedingJoinPoint joinPoint, Cacheable cacheable) throws Throwable {
        EvaluationContext ctx = redisCacheManager.getArgValues(joinPoint);

        String key = cacheable.key();
        long expireTime = cacheable.expireTime();

        String cacheKey = redisCacheManager.getCacheKey(ctx, key, cacheable.args());

        Object cacheObj = redisCacheManager.getCache(cacheKey);
        if (cacheObj == null) {
            cacheObj = joinPoint.proceed();
            redisCacheManager.saveCache(cacheKey, cacheObj, expireTime);
            redisCacheManager.saveCacheGroup(ctx, cacheable, cacheKey);
        }
        return cacheObj;
    }

    @Around("@annotation(cacheEvict)")
    public Object aroundCacheEvict(ProceedingJoinPoint joinPoint, CacheEvict cacheEvict) throws Throwable {
        EvaluationContext ctx = redisCacheManager.getArgValues(joinPoint);

        Object ret = null;
        boolean beforeInvocation = cacheEvict.beforeInvocation();
        if (beforeInvocation) {
            redisCacheManager.clearCache(ctx, cacheEvict);
            ret = joinPoint.proceed();
        } else {
            ret = joinPoint.proceed();
            redisCacheManager.clearCache(ctx, cacheEvict);
        }

        return ret;
    }

}
