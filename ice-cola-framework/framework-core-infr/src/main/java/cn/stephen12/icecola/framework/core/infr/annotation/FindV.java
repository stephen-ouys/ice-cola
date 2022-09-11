package cn.stephen12.icecola.framework.core.infr.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 值对象查找绑定注解
 * @author ouyangsheng
 * @date 2022-05-25
 **/
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface FindV {
    /**
     * 能支持查找的值对象
     * @return
     */
    Class[] value() default {};
}
