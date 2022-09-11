package cn.stephen12.icecola.framework.core.infr.datafiller;

import cn.stephen12.icecola.framework.core.domain.model.BaseV;

/**
 * 数据填充策略
 *
 * @see MergedDataFillStrategy
 *
 * @author ouyangsheng
 * @date 2022-05-25
 **/
public interface DataFillStrategy {

    /**
     * 数据填充策略
     *
     * @param id
     * @param targetVType
     * @param <V>
     * @return
     */
    <V extends BaseV> V findById(Long id, Class<V> targetVType);



}
