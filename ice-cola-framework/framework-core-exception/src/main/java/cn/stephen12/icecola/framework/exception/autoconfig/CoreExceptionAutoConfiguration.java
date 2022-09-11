package cn.stephen12.icecola.framework.exception.autoconfig;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Core-exception 组件自动配置
 *
 * @author ouyangsheng
 * @date 2022-06-01
 **/
@Configuration
@ComponentScan(basePackages = {
        "cn.stephen12.icecola.framework.exception"
})
public class CoreExceptionAutoConfiguration {
}
