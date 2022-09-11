package cn.stephen12.icecola.framework.app;

import cn.stephen12.icecola.framework.app.annotation.CmdExecutor;
import cn.stephen12.icecola.framework.core.domain.model.BaseAggregateRoot;

/**
 * 基础的执行器基类
 * @author ouyangsheng
 * @date 2022-05-23
 **/
@CmdExecutor
public abstract class BaseCmdExecutorImpl<D extends BaseAggregateRoot<D>> extends AppServiceSupport<D>{
}
