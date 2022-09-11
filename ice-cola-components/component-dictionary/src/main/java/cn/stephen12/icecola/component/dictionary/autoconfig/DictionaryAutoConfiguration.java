package cn.stephen12.icecola.component.dictionary.autoconfig;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 字典组件自动装配
 *
 * @author ouyangsheng
 * @date 2022-03-15
 **/
@Configuration
@ComponentScan(basePackages = {
        "cn.stephen12.icecola.component.dictionary"
})
@EnableConfigurationProperties(DictionaryProperties.class)
public class DictionaryAutoConfiguration {
}
