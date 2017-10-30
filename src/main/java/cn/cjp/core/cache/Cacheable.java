package cn.cjp.cache;

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
public @interface Cacheable {

    String[] group() default {};

    /**
     * second
     * @return
     */
    long expireTime() default 3600;

    /**
     * 使用 {@link #args()} 传参，通过 {@link String#format(String, Object...)} 进行格式化
     * key
     * 
     * @return
     */
    String key() default "";

    /**
     * 
     * 使用EL表达式取值
     * 
     * @return
     */
    String[] args() default {};

}
