package cn.cjp.core.stopWatch;

import java.util.Collection;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import cn.cjp.utils.Stopwatchs;
import cn.cjp.utils.Stopwatchs.Stopwatch;
import cn.cjp.utils.Stopwatchs.Task;

/**
 * 实体调用检测拦截器. <br>
 * 这里只用作演示，开发者可自行更改
 * 
 * @usage
 * 
 *        1. used in {@link Configuration}
 * 
 *        <pre>
 *        &#64;Bean
 *        public BeanWatcher BeanWatcher() {
 *            BeanWatcher w = new BeanWatcher();
 *            return w;
 *        }
 *        </pre>
 * 
 *        2. 或者添加 @Component 注解
 * 
 * @author JinPeng Cui
 * @see EnableAspectJAutoProxy 如何在SpringBoot中使用 aspect
 */
@Aspect
public class BeanWatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeanWatcher.class);

    public BeanWatcher() {
        LOGGER.debug("init");
    }

    @Pointcut(value = "execution(public * cn.cjp.core.service..*.*(..))")
    public void core() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.info("core");
        }
    }

    /**
     * @use @Pointcut(value= "execution(* your_package.*.sayAdvisorBefore(..))
     *      && args(param)", argNames = "param")
     * @param point
     */
    @Before(value = "core()")
    public void before(JoinPoint point) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.info("Watch before: " + point.getSignature().toString());
        }
        BeanInspectorModel model = initBeanInspectModel(point);
        Task task = new Task(model.toFullName());
        task.setObj(model);
        Stopwatchs.start(task);
    }

    @AfterReturning(value = "core()", returning = "returnValue")
    public void after(JoinPoint point, Object returnValue) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.info("Wacth after: " + point.getSignature().toString());
        }
        Stopwatch stopwatch = Stopwatchs.getCurrent();
        Stopwatchs.end();
        fill(stopwatch, point, returnValue);

        Stopwatch root = Stopwatchs.getRoot();
        if (root.equals(stopwatch)) {
            if (root != null) {
                LOGGER.info("\r\n" + Stopwatchs.getTimingStat());
                Stopwatchs.release();
            }
        }
    }

    private void fill(Stopwatch stopwatch, JoinPoint point, Object returnValue) {

        int returnLineNum = 1;
        if (returnValue != null) {
            if (returnValue.getClass().isArray()) {
                returnLineNum = ((Object[]) returnValue).length;
            } else if ((returnValue instanceof Collection<?>)) {
                returnLineNum = ((Collection<?>) returnValue).size();
            } else if ((returnValue instanceof Page)) {
                returnLineNum = ((Page) returnValue).getResultList().size();
            }
        }

        Task task = stopwatch.getTask();
        BeanInspectorModel inspectorModel = (BeanInspectorModel) task.getObj();
        // 当前model: calledTime+1, period++, returnLineNum++
        inspectorModel.mergeValue(stopwatch.getElapsedTime(), returnLineNum);
    }

    /**
     * 建立一个初始化model
     * 
     * @param invocation
     * @return
     */
    private BeanInspectorModel initBeanInspectModel(JoinPoint point) {
        Signature sign = point.getSignature();
        String className = sign.getDeclaringTypeName();
        String method = sign.toString().substring(sign.toString().lastIndexOf(".") + 1);

        BeanInspectorModel model = BeanInspectorModel.newInstance(className, method, "");
        return model;
    }

}
