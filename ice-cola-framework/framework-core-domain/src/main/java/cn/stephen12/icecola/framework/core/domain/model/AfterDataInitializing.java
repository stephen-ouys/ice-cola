package cn.stephen12.icecola.framework.core.domain.model;

/**
 * 充血完成后调用的接口
 * <p>
 *     //TODO 暂时没有具体实现，各业务系统自行实现
 * </p>
 * @author ouyangsheng
 * @date 2022-06-11
 **/
public interface AfterDataInitializing {

    /**
     * 充血完成后调用
     */
    default void afterDataInitializing(){}
}
