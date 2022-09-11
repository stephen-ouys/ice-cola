package cn.stephen12.icecola.component.mock;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * @author ouys
 */
@Configuration(proxyBeanMethods = false)
@EnableAspectJAutoProxy
@ConditionalOnProperty(prefix = "ice-cola.component.mock", name = "enabled", havingValue = "true")
public class MockAutoConfiguration {

    @Bean
    public PodamFactory podamFactory() {
        return new PodamFactoryImpl();
    }

    @Bean
    public MockResponseAspect mockResponseAspect() {
        return new MockResponseAspect();
    }

}
