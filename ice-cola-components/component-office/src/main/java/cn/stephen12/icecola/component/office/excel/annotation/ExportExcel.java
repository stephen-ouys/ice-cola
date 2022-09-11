package cn.stephen12.icecola.component.office.excel.annotation;


import cn.stephen12.icecola.framework.model.unpacker.DefaultUnpacker;
import cn.stephen12.icecola.framework.model.unpacker.Unpacker;

import java.lang.annotation.*;

/**
 * 导出Excel注解
 *
 * @author ouyangsheng
 * @since  2021-03-31
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ExportExcel {

    /**
     * 导出文件名
     *
     * @param
     */
    String fileName() default "excel1";

    /**
     * 导出Shell名
     *
     * @param
     * @return {@link String} 导出Shell名
     */
    String shellName() default "shell1";

    /**
     * 转换器方法
     * @return
     */
    Class<? extends Unpacker> unpacker() default DefaultUnpacker.class;

}
