package cn.stephen12.icecola.component.dictionary;

import cn.stephen12.icecola.component.dictionary.model.DefaultDictWrapper;
import cn.stephen12.icecola.component.dictionary.model.DictWrapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 声明枚举字段
 *
 * @author ouyangsheng
 * @date 2022-04-24
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DictionaryFiled {
    Class<? extends DictWrapper> wrapperClass() default DefaultDictWrapper.class;
}
