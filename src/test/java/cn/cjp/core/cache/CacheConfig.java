package cn.cjp.core.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.cjp.core.cache.CacheInterceptor;
import cn.cjp.core.cache.CacheManager;

@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CacheManager cacheManager = new CacheManager();
        return cacheManager;
    }

    @Bean
    public CacheInterceptor cacheInterceptor(@Autowired CacheManager cacheManager) {
        return new CacheInterceptor(cacheManager);
    }

}
