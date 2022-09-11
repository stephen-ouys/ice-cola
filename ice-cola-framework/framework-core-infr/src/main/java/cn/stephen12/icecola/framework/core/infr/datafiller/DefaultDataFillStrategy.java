package cn.stephen12.icecola.framework.core.infr.datafiller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.stephen12.icecola.framework.core.domain.model.BaseV;
import cn.stephen12.icecola.framework.core.infr.repository.RepositoryWrapper;
import org.springframework.stereotype.Component;

/**
 * 默认数据填充策略
 *
 * @author ouyangsheng
 * @date 2022-05-25
 **/
@Component
public class DefaultDataFillStrategy implements DataFillStrategy {

    /**
     * 每次都直接调用 repository 查，很浪费I/O资源
     * <br/>
     * //TODO NOTE: 仅用于测试 或者 数据量少的场景
     *
     * @param id
     * @param targetVType
     * @param <V>
     * @return
     */
    @Override
    public <V extends BaseV> V findById(Long id, Class<V> targetVType) {
        RepositoryWrapper repository = DataFiller.getRepository(targetVType);
        Object result = repository.findById(id);
        return toV(result, targetVType);
    }

    /**
     * 将一个对象转换未 指定值对象
     *
     * @param source
     * @param targetVType
     * @param <V>
     * @return
     */
    private <V extends BaseV> V toV(Object source, Class<V> targetVType) {
        if (source != null) {
            V vObj = null;
            try {
                vObj = targetVType.newInstance();
            } catch (Exception e) {
                throw new IllegalStateException("targetVType (" + targetVType.toString() + ") is not implementable");
            }
            BeanUtil.copyProperties(source, vObj, CopyOptions.create().ignoreError());
            return vObj;
        }
        return null;
    }
}
