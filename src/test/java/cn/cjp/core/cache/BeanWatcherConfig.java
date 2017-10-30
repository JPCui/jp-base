package cn.cjp.core.cache;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import cn.cjp.core.stopWatch.BeanWatcher;

@Configuration
@EnableAspectJAutoProxy
public class BeanWatcherConfig {

    @Bean
    public BeanWatcher beanWatcher() {
        BeanWatcher beanWatcher = new BeanWatcher();
        return beanWatcher;
    }

}
