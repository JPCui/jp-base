package cn.cjp.core.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import cn.cjp.BaseTest;
import cn.cjp.core.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CacheManagerApp.class }, initializers = ConfigFileApplicationContextInitializer.class)
@WebAppConfiguration
public class CacheTest extends BaseTest {

    @Autowired
    UserService service;

    @Test
    public void test() {
        System.out.println(service.get(1));
        System.out.println(service.get(1));
        System.out.println(service.get(2));
        service.removeAll();
        System.out.println(service.get(1));
        System.out.println(service.get(2));

        System.out.println("update 1");
        service.update(1);
        System.out.println(service.get(1));

        service.update(1);
        System.out.println(service.get(2));

    }

}
