package cn.stephen12.icecola.framework.core.infr.autoconfig;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Core-Infr组件自动配置
 *
 * @author ouyangsheng
 * @date 2022-06-01
 **/
@EnableCaching
@Configuration
@ComponentScan(basePackages = {
        "cn.stephen12.icecola.framework.core.infr"
})
public class CoreInfrAutoConfiguration {
}
