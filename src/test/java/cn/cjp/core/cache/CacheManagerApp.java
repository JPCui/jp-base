package cn.cjp.core.cache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import cn.cjp.utils.Logger;

@ComponentScan(basePackages = { "cn.cjp.core.cache", "cn.cjp.core.service" })
@SpringBootApplication
public class CacheManagerApp extends WebMvcConfigurationSupport {

    static final Logger LOGGER = Logger.getLogger(CacheManagerApp.class);

    public static void main(String[] args) {
        SpringApplication.run(CacheManagerApp.class, args);
    }

}
