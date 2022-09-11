package cn.stephen12.icecola.component.utils.period;

import io.swagger.annotations.ApiModelProperty;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 把 json 序列化为 Period 字段
 * @author ouyangsheng
 * @date 2022-04-28
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(JsonAsPeriod.List.class)
public @interface JsonAsPeriod {

    /**
     * 序列化为Json字段的Key
     * @return
     */
    String periodKey() default "";

    /**
     * 开始时间字段
     * @return
     */
    String startField() default "";

    /**
     * 结束字段
     * @return
     */
    String endField() default "";

    /**
     * Api注解
     * @return
     */
    ApiModelProperty apiProperty() default @ApiModelProperty();

    @Target({ ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface List {
        JsonAsPeriod[] value();
    }
}
