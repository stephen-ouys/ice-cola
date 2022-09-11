package cn.stephen12.icecola.framework.app;

import cn.stephen12.icecola.framework.core.domain.model.BaseAggregateRoot;

/**
 * App Service 基类
 *
 * @see ICmdExecutor
 * @see IAppService
 *
 * @author ouyangsheng
 * @date 2022-03-20
 **/
public abstract class BaseAppServiceImpl<D extends BaseAggregateRoot<D>> extends AppServiceSupport<D> {
}
