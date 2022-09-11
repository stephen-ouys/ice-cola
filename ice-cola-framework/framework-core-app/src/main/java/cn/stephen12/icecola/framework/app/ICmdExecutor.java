package cn.stephen12.icecola.framework.app;

import javax.validation.Valid;

/**
 * Cmd 执行器
 *
 * @author ouyangsheng
 * @date 2022-05-17
 * @see ICmdProcessor 两者只能实现一个
 **/
public interface ICmdExecutor<CMD> {
    /**
     * 执行，无返回结果
     * <p>
     * 配合注解 CmdExecutor 默认会在调用其进行参数校验
     * </p>
     * <p>
     *    //TODO NOTE 有个值得注意的点，java validation 设计必须在接口上加 @Valid 而非实现是基于里氏替换的原则: <br/>
     *    https://beanvalidation.org/1.1/spec/#constraintdeclarationvalidationprocess-methodlevelconstraints-inheritance
     * </p>
     *
     * @param cmd
     * @see cn.stephen12.icecola.framework.app.annotation.CmdExecutor
     */
    void execute(@Valid CMD cmd);
}
