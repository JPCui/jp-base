package cn.cjp.core.stopWatch;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy(exposeProxy = true)
@Aspect
public class JPBeanWatcher extends BeanWatcher {

    /**
     * 定义切面
     * 
     * @return
     */
    @Bean
    public BeanWatcher beanWatcher() {
        JPBeanWatcher config = new JPBeanWatcher();
        return config;
    }

    @Override
    @Pointcut(value = "execution(public * cn.cjp.core.service..*.*(..))")
    public void core() {
    }

}
