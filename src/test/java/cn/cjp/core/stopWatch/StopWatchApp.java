package cn.cjp.core.stopWatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import cn.cjp.utils.Logger;

@ComponentScan(basePackages = { "cn.cjp.core.stopWatch", "cn.cjp.core.service" })
@SpringBootApplication
public class StopWatchApp extends WebMvcConfigurationSupport {

    static final Logger LOGGER = Logger.getLogger(StopWatchApp.class);

    public static void main(String[] args) {
        SpringApplication.run(StopWatchApp.class, args);
    }

}
