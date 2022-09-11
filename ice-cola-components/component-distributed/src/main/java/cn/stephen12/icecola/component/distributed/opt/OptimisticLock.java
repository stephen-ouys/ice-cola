package cn.stephen12.icecola.component.distributed.opt;

import java.util.function.Consumer;

/**
 * 乐观锁顶层接口
 * @author ouyangsheng
 * @since 2022-09-01
 **/
public interface OptimisticLock<T> {

    /**
     * 如果目标资源（可能是版本或者某个value) 等于 当前值 v 则执行 action
     * @param v
     * @param action
     * @return
     */
    boolean computeIfMatch(T v, Consumer<T> action);
}
