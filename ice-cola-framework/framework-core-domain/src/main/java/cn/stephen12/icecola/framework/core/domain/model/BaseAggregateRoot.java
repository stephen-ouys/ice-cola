package cn.stephen12.icecola.framework.core.domain.model;

import com.alibaba.cola.domain.ApplicationContextHelper;
import com.alibaba.cola.domain.Entity;
import cn.stephen12.icecola.framework.core.domain.BaseGatewayI;
import cn.stephen12.icecola.framework.core.domain.GatewayManager;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

/**
 * 基本模型基类
 *
 * @author ouyangsheng
 * @since 2022-05-13
 */
@ToString
@NoArgsConstructor
@SuperBuilder
@Data
@Entity
public abstract class BaseAggregateRoot<E extends BaseAggregateRoot<E>> extends BaseE<E> {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired(required = false)
    @Setter(AccessLevel.MODULE)
    protected BaseGatewayI<E> basicGateway;

    /**
     * 分公司ID
     */
    protected Long storeId;

    /**
     * 持久化
     */
    public void save() {
        basicGateway.save((E) this);
    }

    /**
     * 推送事件
     *
     * @param eventObj
     */
    public <DE extends DomainEvent> void publishEvent(DE eventObj) {
        basicGateway.publishEvent(eventObj);
    }


    /**
     * 获取工厂Bean
     *
     * @param fClass
     * @param <T>
     * @return
     */
    protected static <T> T getFactoryBean(Class<T> fClass) {
        return ApplicationContextHelper.getBean(fClass);
    }

    /**
     * 通过ID获取
     *
     * @param domainClass
     * @param id
     * @param <T>
     * @return
     */
    protected static <T extends BaseAggregateRoot<T>> Optional<T> findDomainById(Class<T> domainClass, Long id) {
        return getBasicGateway(domainClass).flatMap(gateway -> gateway.findDomainById(id));
    }

    /**
     * 获取值对象
     *
     * @param domainClass
     * @param id
     * @param vajObjClass
     * @param <D>
     * @param <V>
     * @return
     */
    protected static <D extends BaseAggregateRoot<D>, V> Optional<V> findValObjById(Class<D> domainClass, Long id, Class<V> vajObjClass) {
        return getBasicGateway(domainClass).flatMap(gateway -> gateway.findValObjById(id, vajObjClass));
    }

    /**
     * 获取Gateway
     *
     * @param clazz
     * @param <D>
     * @return
     */
    public static <D extends BaseAggregateRoot<D>> Optional<BaseGatewayI> getBasicGateway(Class<D> clazz) {
        return GatewayManager.findGatewayByDomainClass(clazz);
    }
}
