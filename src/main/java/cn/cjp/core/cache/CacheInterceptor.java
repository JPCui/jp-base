package cn.cjp.core.cache;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.EvaluationContext;

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
public class CacheInterceptor {

    static final Logger LOGGER = Logger.getLogger(CacheInterceptor.class);

    CacheManager cacheManager;

    public CacheInterceptor() {
        this.cacheManager = new CacheManager();
    }

    public CacheInterceptor(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    /**
     * 
     * @param joinPoint
     * @param cacheable
     * @return
     * @throws Throwable
     * @see cn.cjp.core.cache.Cacheable
     */
    @Around("@annotation(cacheable)")
    public Object aroundCacheable(ProceedingJoinPoint joinPoint, Cacheable cacheable) throws Throwable {
        EvaluationContext ctx = cacheManager.getArgValues(joinPoint);

        String key = cacheable.key();
        long expireTime = cacheable.expireTime();

        String cacheKey = cacheManager.getCacheKey(ctx, key, cacheable.args());

        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Class<?> returnType = methodSignature.getReturnType();

        Object cacheObj = cacheManager.getCache(returnType, cacheKey);
        if (cacheObj == null) {
            LOGGER.debug(String.format("miss cache: %s", cacheKey));
            cacheObj = joinPoint.proceed();
            cacheManager.saveCache(cacheKey, cacheObj, expireTime);
            cacheManager.saveCacheGroup(ctx, cacheable, cacheKey);
        } else {
            LOGGER.debug(String.format("get cache: %s, value: %s", cacheKey, cacheObj));
        }

        return cacheObj;
    }

    @Around("@annotation(cacheEvict)")
    public Object aroundCacheEvict(ProceedingJoinPoint joinPoint, CacheEvict cacheEvict) throws Throwable {
        EvaluationContext ctx = cacheManager.getArgValues(joinPoint);

        Object ret = null;
        boolean beforeInvocation = cacheEvict.beforeInvocation();
        if (beforeInvocation) {
            cacheManager.clearCache(ctx, cacheEvict);
            ret = joinPoint.proceed();
        } else {
            ret = joinPoint.proceed();
            cacheManager.clearCache(ctx, cacheEvict);
        }

        return ret;
    }

}
