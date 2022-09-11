package cn.stephen12.icecola.framework.core.domain;

import cn.stephen12.icecola.framework.core.domain.model.BaseAggregateRoot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * 聚合根工厂
 *
 * @author ouyangsheng
 * @date 2022-03-23
 **/
public class BaseFactoryImpl<D extends BaseAggregateRoot<D>> {
    @Autowired(required = false)
    protected BaseGatewayI<D> baseGateway;

    @Autowired
    protected ApplicationContext applicationContext;

}
