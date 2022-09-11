package cn.stephen12.icecola.framework.app;

import cn.stephen12.icecola.framework.core.domain.BaseGatewayI;
import cn.stephen12.icecola.framework.core.domain.model.BaseAggregateRoot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * AppService 能力支持
 *
 * @author ouyangsheng
 * @date 2022-03-20
 * @see IAppService
 **/
public abstract class AppServiceSupport<D extends BaseAggregateRoot<D>> implements IAppService<D> {

    @Autowired
    protected BaseGatewayI<D> baseGatewayI;

    @Autowired
    protected ApplicationContext applicationContext;


    /**
     * 执行修改并保存
     *
     * @param id
     */
    protected D getDomain(Long id) {
        return baseGatewayI.findDomainById(id).orElseThrow(() -> new IllegalArgumentException("数据不存在"));
    }

    /**
     * 获取命令执行器
     *
     * @param clazz
     * @param <T>
     * @return
     */
    protected <T> T getCmdExecutor(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }
}
