package cn.stephen12.icecola.component.dictionary.autoconfig;

import cn.stephen12.icecola.component.dictionary.DictionaryManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * 字典扫描完成后处理器
 *
 * @author ouyangsheng
 * @date 2022-06-06
 **/
@Slf4j
@Component
public class DictionaryScannedHandlers implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private DictionaryManager dictionaryManager;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private List<Handler> handlers;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //注意，Feign等组件会构建自己的容器（ApplicationContext),这里判断主容器加载完后，注册序列化器
        if (event.getApplicationContext().equals(this.applicationContext)) {
            //调用具体处理器
            for (Handler handler : handlers) {
                handler.handle(dictionaryManager);
            }
        }
    }

    /**
     * 扫描完成后要处理的事项交给本处理器
     */
    public interface Handler {

        /**
         * 完成扫描后处理
         *
         * @param dictionaryManager
         */
        void handle(DictionaryManager dictionaryManager);
    }

    /**
     * 打印扫描结果
     */
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Component
    class Printer implements Handler{

        @Override
        public void handle(DictionaryManager dictionaryManager) {
            if (log.isDebugEnabled()) {
                Collection<Class<?>> enumClasses = dictionaryManager.getAllEnums();
                log.debug("Have found these dictionary classes: {}", enumClasses);
            }
        }
    }
}
