package cn.stephen12.icecola.component.utils.autoconfig;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 工具组件自动装配
 *
 * @author ouyangsheng
 * @date 2022-04-29
 **/
@Configuration
@ComponentScan(basePackages = {
        "cn.stephen12.icecola.component.utils"
})
public class UtilsAutoConfiguration {
}
