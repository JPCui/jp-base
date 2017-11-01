package cn.cjp.utils;

import org.junit.Test;

import cn.cjp.core.stopWatch.Stopwatchs;
import cn.cjp.core.stopWatch.Stopwatchs.Task;

public class StopwatchsTest {

    @Test
    public void testTask() {
        Task aTask = new Task("a");

        Task bTask = new Task("a");

        Stopwatchs.start(aTask);
        Stopwatchs.start(bTask);

        Stopwatchs.end();
        Stopwatchs.end();

        System.out.println(Stopwatchs.getTimingStat());

        Stopwatchs.release();

    }

}
