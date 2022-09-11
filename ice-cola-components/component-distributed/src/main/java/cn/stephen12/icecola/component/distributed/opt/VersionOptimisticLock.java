package cn.stephen12.icecola.component.distributed.opt;

import java.util.function.Consumer;

/**
 * 版本乐观锁
 * @author ouyangsheng
 * @since 2022-09-01
 **/
public interface VersionOptimisticLock extends OptimisticLock<Long>{

    /**
     * 如果目标版本 小于等于 当前值 version 则执行 action
     * @param version
     * @param action
     * @return
     */
    @Override
    boolean computeIfMatch(Long version, Consumer<Long> action);
}
