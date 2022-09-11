package cn.stephen12.icecola.framework.core.infr.datafiller;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.stephen12.icecola.framework.core.domain.model.BaseV;
import cn.stephen12.icecola.framework.core.infr.annotation.FindV;
import cn.stephen12.icecola.framework.core.infr.model.IdV;
import cn.stephen12.icecola.framework.core.infr.repository.RepositoryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 外部数据充血器
 * <p>
 * //TODO NOTE: 警告，本类仅设计用于Gateway充血使用，其他场景未经测试，不要乱用
 * </p>
 *
 * @author ouyangsheng
 * @date 2022-05-25
 **/
@Component
public class DataFiller {

    /**
     * 值对象对于的 RepositoryWrapper
     */
    private static final Map<Class, RepositoryWrapper> vTypeToDataWrapperMap = new ConcurrentHashMap<>(16);
    /**
     * 默认策略
     */
    private static DataFillStrategy dataFillStrategy;

    /**
     * 绑定值对象类型与 RepositoryWrapper的映射关系
     *
     * @param wrappers
     */
    @Autowired(required = false)
    public void setRepositoryWrappers(List<RepositoryWrapper> wrappers) {
        for (RepositoryWrapper wrapper : wrappers) {
            FindV findV = AnnotationUtil.getAnnotation(wrapper.getClass(), FindV.class);

            if (findV != null && findV.value() != null) {
                for (Class vType : findV.value()) {
                    vTypeToDataWrapperMap.put(vType, wrapper);
                }
            }
        }
    }

    @Autowired
    public void setDataFillStrategy(DataFillStrategy dataFillStrategy) {
        DataFiller.dataFillStrategy = dataFillStrategy;
    }

    /**
     * 获取值对象
     *
     * @param id
     * @param targetVClass
     * @param <V>
     * @return
     */
    public static <V extends BaseV> V findVById(IdV id, Class<V> targetVClass) {
        return findVById(id.obtainId(), targetVClass);
    }

    /**
     * 获取值对象
     *
     * @param id
     * @param targetVClass
     * @param <V>
     * @return
     */
    public static <V extends BaseV> V findVById(Long id, Class<V> targetVClass) {
        return dataFillStrategy.findById(id, targetVClass);
    }

    /**
     * 获取值类型对于的仓库获取器
     *
     * @param targetVClass
     * @param <V>
     * @return
     */
    static <V extends BaseV> RepositoryWrapper getRepository(Class<V> targetVClass) {
        return vTypeToDataWrapperMap.get(targetVClass);
    }
}
