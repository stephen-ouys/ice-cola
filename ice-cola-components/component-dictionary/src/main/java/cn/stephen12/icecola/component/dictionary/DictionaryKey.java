package cn.stephen12.icecola.component.dictionary;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 枚举KEY
 *
 * @author ouyangsheng
 * @date 2022-03-16
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DictionaryKey {
    String value() default "";
}
