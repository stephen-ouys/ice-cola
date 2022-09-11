package cn.stephen12.icecola.framework.core.domain;

import cn.stephen12.icecola.framework.core.domain.model.BaseAggregateRoot;
import cn.stephen12.icecola.framework.core.domain.model.DomainEvent;

/**
 * 基础的gateway接口
 *
 * @author ouyangsheng
 * @date 2022-03-22
 **/
public interface BaseGatewayI<E extends BaseAggregateRoot<E>> extends PersistenceI<E> {

    /**
     * 推送事件
     *
     * @param eventObj
     */
    <DE extends DomainEvent> void publishEvent(DE eventObj);

    /**
     * 推送事件并且不关注结果，不受事件异常引响
     *
     * @param eventObj
     */
    <DE extends DomainEvent> void publishAndIgnoreEvent(DE eventObj);

    /**
     * 生成ID
     * @return
     */
    Long generateId();

    /**
     * 生成编号
     * @return
     */
    String generateNo();


    /**
     * 生成ID
     * @param domainClass
     * @return
     */
    Long generateId(Class<?> domainClass);

    /**
     * 生成全局编号
     * @param domainClass
     * @return
     */
    String generateNo(Class<?> domainClass);
}
