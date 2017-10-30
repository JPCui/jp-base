package cn.cjp.core.cache;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CacheEvict {

    String[] group() default {};

    /**
     * 
     * @return
     * 
     * @see String#format(String, Object...)
     */
    String[] key() default {};

    /**
     * 
     * 根据前缀删除缓存
     * 
     * 末尾的*号，自己加， o(￣ヘ￣o#)
     * 
     * @return
     */
    String[] keyPrefix() default {};

    /**
     * 是否在调用前清除缓存
     * 
     * @return
     */
    boolean beforeInvocation() default true;

    String[] args() default {};

}
