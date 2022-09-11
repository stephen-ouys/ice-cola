package cn.stephen12.icecola.framework.model.dto;

import java.util.Collection;

/**
 * 能转换为集合的
 * @author ouyangsheng
 * @date 2022-03-31
 **/
public interface AsCollection<E> {
    /**
     * 将数据转换为集合
     * @return
     */
    Collection<E> toCollection();


}
