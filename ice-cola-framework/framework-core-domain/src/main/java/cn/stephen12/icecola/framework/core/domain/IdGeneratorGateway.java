package cn.stephen12.icecola.framework.core.domain;

/**
 * Id 编号、生成器
 *
 * @author ouyangsheng
 * @date 2022-03-22
 **/
public interface IdGeneratorGateway {

    /**
     * 生成ID
     * @param domainClass 需要生成ID的类，实现者可自学觉得生成规则
     * @return
     */
    Long generateId(Class<?> domainClass);

    /**
     * 生成全局编号
     *
     * @param domainClass
     * @return
     */
    String generateNo(Class<?> domainClass);

}
