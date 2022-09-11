package cn.stephen12.icecola.framework.core.domain.model;

/**
 * 领域事件基类
 * @author ouyangsheng
 * @since 2022-05-13
 **/
public abstract class BaseDomainEvent<D extends BaseAggregateRoot> implements DomainEvent<D>{
    /**
     * 领域对象
     * 通常应该设计成不能被其他领域访问
     */
    private D domain;

    public BaseDomainEvent(D domain){
        this.domain = domain;
    }

    @Override
    public Long getAggregateId() {
        return domain.getId();
    }
}
