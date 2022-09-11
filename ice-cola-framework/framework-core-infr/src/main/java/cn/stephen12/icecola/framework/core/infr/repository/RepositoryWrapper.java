package cn.stephen12.icecola.framework.core.infr.repository;

import org.springframework.cache.annotation.Cacheable;

import java.util.Collection;
import java.util.List;

/**
 *  Repository 包装器
 * <p>
 *     把外部（非JPA、非数据库资源）进行包装，如RPC、配置文件、JVM缓存，使其保持与SpringData Repository 相同的特性
 * </p>
 * @param <E> 统一的承载对象
 * @author ouyangsheng
 * @date 2022-05-23
 **/
public interface RepositoryWrapper<E,ID>{
    /**
     *  获取实体的ID
     * @param entity
     * @return
     */
    ID getIdOf(E entity);

    /**
     * 通过ID获取一个对象
     * @param id
     * @return
     */
    E findById(ID id);

    /**
     * 批量获取
     * @param ids
     * @return
     */
    List<E> findByIds(Collection<ID> ids);

}
