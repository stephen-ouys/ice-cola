package cn.stephen12.icecola.component.oplog.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 操作日志注解
 * @author ouyangsheng
 * @since 2022-08-11
 **/
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface OpLog {
    String value();

    String label();

    String description();

    String idEL();
}
