package cn.stephen12.icecola.component.dictionary.apidoc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 枚举类型
 *
 * @author ouyangsheng
 * @date 2022-03-15
 **/
@Target({ElementType.PARAMETER,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiDictionaryType {
    String parameterType() default "path";
}
