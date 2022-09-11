package cn.stephen12.icecola.framework.app;

import cn.stephen12.icecola.framework.core.domain.model.DomainEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.PayloadApplicationEvent;

/**
 * 领域事件监听器
 * @author ouyangsheng
 * @date 2022-05-13
 *
 * @see DomainEvent
 * @see org.springframework.context.ApplicationEventPublisher
 * @see ApplicationListener
 **/
public interface DomainEventListener<DE extends DomainEvent> extends ApplicationListener<PayloadApplicationEvent<DE>> {

    /**
     * 监听Spring 事件
     * @param event
     */
    @Override
    default void onApplicationEvent(PayloadApplicationEvent<DE> event){
        DE payload = event.getPayload();
        onDomainEvent(payload);
    }

    /**
     * 领域事件监听
     * @param event
     */
    void onDomainEvent(DE event);
}
