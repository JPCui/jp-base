package cn.cjp.core.stopWatch;

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
@ContextConfiguration(classes = { StopWatchApp.class }, initializers = ConfigFileApplicationContextInitializer.class)
@WebAppConfiguration
public class StopWatchTest extends BaseTest {

    @Autowired
    UserService userService;

    @Test
    public void test() {
        userService.func1();
    }

}
