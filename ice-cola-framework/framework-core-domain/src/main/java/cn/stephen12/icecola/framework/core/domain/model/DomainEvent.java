package cn.stephen12.icecola.framework.core.domain.model;

/**
 * 领域事件接口
 * @author ouyangsheng
 * @date 2022-05-13
 *
 * @see DomainEventListener
 **/
public interface DomainEvent<D extends BaseAggregateRoot> {
    /**
     * 获取聚合根ID
     * @return
     */
    Long getAggregateId();

}
