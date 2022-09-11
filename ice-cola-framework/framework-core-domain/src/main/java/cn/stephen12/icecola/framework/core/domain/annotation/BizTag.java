package cn.stephen12.icecola.framework.core.domain.annotation;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 实体的Id标识
 * @author ouyangsheng
 * @date 2022-05-25
 **/
@Inherited
@Target({TYPE})
@Retention(RUNTIME)
public @interface BizTag {

    /**
     * Id标识
     * @return
     */
    String value() default "";
}
