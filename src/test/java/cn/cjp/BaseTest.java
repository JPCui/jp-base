package cn.cjp;

import org.junit.After;
import org.junit.Before;

public class BaseTest {

    @Before
    public void before() {
        System.out.println("==========================");
        System.out.println("Before ==========================");
        System.out.println("==========================");
    }

    @After
    public void after() {
        System.out.println("==========================");
        System.out.println("After ==========================");
        System.out.println("==========================");
    }

}
