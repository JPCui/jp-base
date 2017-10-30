package cn.cjp.core.stopWatch;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy(exposeProxy = true)
public class BeanWatcherConfig {

    @Bean
    public BeanWatcher beanWatcher() {
        BeanWatcher beanWatcher = new BeanWatcher();
        return beanWatcher;
    }

}
