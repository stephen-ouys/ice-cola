package cn.stephen12.icecola.framework.core.infr.api;

/**
 * 充血一个聚合跟之后的通知
 * @author ouyangsheng
 * @date 2022-05-25
 **/
public interface FindAggregateRootInterceptor {

    /**
     * 聚合跟充血后处理
     * @param aggregateRoot
     */
    void postHandle(Object aggregateRoot);


}
