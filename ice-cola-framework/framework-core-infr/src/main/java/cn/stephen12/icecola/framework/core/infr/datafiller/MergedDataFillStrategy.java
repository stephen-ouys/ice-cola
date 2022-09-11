package cn.stephen12.icecola.framework.core.infr.datafiller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.StopWatch;
import cn.stephen12.icecola.framework.core.infr.api.FindAggregateRootInterceptor;
import cn.stephen12.icecola.framework.core.domain.model.BaseV;
import cn.stephen12.icecola.framework.core.infr.context.RequestContext;
import cn.stephen12.icecola.framework.core.infr.model.IdV;
import cn.stephen12.icecola.framework.core.infr.repository.RepositoryWrapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 合并数据填充策略
 * <br/>
 * 通过收集所有同类型值对象的ID，调用批量查询的接口，减少I/O
 *
 * @author ouyangsheng
 * @date 2022-05-25
 **/
@Primary
@Component
public class MergedDataFillStrategy implements DataFillStrategy, FindAggregateRootInterceptor {

    /**
     * 缓存Key
     */
    private final static String MERGE_CACHE_KEY = "MERGE_CACHE";

    /**
     * 数据填充策略
     *
     * @param id
     * @param targetVType
     * @param <V>
     * @return
     */
    @Override
    public <V extends BaseV> V findById(Long id, Class<V> targetVType) {
        //从上下文中获取缓存对象
        MergeCache mergeCache = getMergeCache();

        //新建对象、并缓存
        return mergeCache.newAndCache(targetVType, id);
    }

    /**
     * 虽然一个RequestContext 范围内，会有多个聚合跟 充血的过程，但只需要保证每次充血后把数据清空，即可保证数据正确性。
     *
     * @param aggregateRoot
     */
    @Override
    public void postHandle(Object aggregateRoot) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("Start filling outer data!");

        //从上下文中获取缓存对象
        MergeCache mergeCache = getMergeCache();

        //拿到所有被缓存的类型，统一调接口
        Set<Class> cachedVTypes = mergeCache.getCachedVTypes();
        for (Class vType : cachedVTypes) {
            //拿到所有Id取掉 findByIds 方法
            Map<Long, Object> idToInstanceMap = mergeCache.getIdToInstanceMap(vType);
            RepositoryWrapper repository = DataFiller.getRepository(vType);
            List<? extends BaseV> results = repository.findByIds(idToInstanceMap.keySet());

            //将结果反Copy 回实例
            for (Object obj : results) {
                Object target = idToInstanceMap.get(getId(obj));
                BeanUtil.copyProperties(obj, target, CopyOptions.create().ignoreNullValue().ignoreError());
            }
        }

        //清理MergeCache 避免其他同线程聚合根数据错乱
        mergeCache.clear();

        //Finished filling outer data
        stopWatch.stop();
        stopWatch.prettyPrint();
    }

    /**
     * 获取任意Bean的Id
     *
     * @param obj
     * @return
     */
    private Long getId(Object obj) {
        if (obj instanceof IdV) {
            return ((IdV) obj).obtainId();
        } else if (obj instanceof BaseV) {
            return ((BaseV) obj).getId();
        } else {
            return BeanUtil.getProperty(obj, "id");
        }
    }

    private MergeCache getMergeCache() {
        //获取该类型的缓存
        return RequestContext.computeIfAbsent(MERGE_CACHE_KEY, key -> new MergeCache());
    }


    /**
     * 服务于请求合并，每一种值对象，对应一个 MergeCache
     */
    class MergeCache {
        /**
         * 一对多的Map
         */
        MultiValueMap<Class, Object> vTypeToInstancesMap;

        /**
         * 初始化
         */
        MergeCache() {
            this.vTypeToInstancesMap = new LinkedMultiValueMap<>(16);
        }

        /**
         * 往缓存里添加实例
         *
         * @param vType
         * @param id
         * @param <V>
         * @return
         */
        <V extends BaseV> V newAndCache(Class<V> vType, Long id) {
            V vObj = null;
            try {
                vObj = vType.newInstance();
                vObj.setId(id);
            } catch (Exception e) {
                throw new IllegalStateException("targetVType (" + vType.toString() + ") is not implementable");
            }
            this.vTypeToInstancesMap.add(vType, vObj);

            return vObj;
        }

        /**
         * 获取所有被缓存管理的值对象类型
         *
         * @return
         */
        Set<Class> getCachedVTypes() {
            return this.vTypeToInstancesMap.keySet();
        }


        /**
         * 获取指定类型 Id-> Instance 的映射
         *
         * @param vType
         * @return
         */
        Map<Long, Object> getIdToInstanceMap(Class vType) {
            List<Object> instances = this.vTypeToInstancesMap.get(vType);
            return instances.stream().collect(Collectors.toMap(i -> ((BaseV) i).getId(), Function.identity(), (pre, curr) -> pre));
        }


        /**
         * 清理掉全部缓存
         */
        void clear() {
            this.vTypeToInstancesMap.clear();
        }
    }
}
