package cn.stephen12.icecola.framework.core.domain;

import com.alibaba.cola.exception.SysException;
import cn.stephen12.icecola.framework.core.domain.model.BaseAggregateRoot;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 持久化接口
 *
 * @author ouyangsheng
 * @date 2022-03-22
 *
 * @see BaseGatewayI
 **/
public interface PersistenceI<D extends BaseAggregateRoot<D>> {

    /**
     * 查找一个聚合根
     * @param id
     * @return
     */
    Optional<D> findDomainById(Long id);

    /**
     * 批量按ID获取聚合根
     * @param ids
     * @return
     */
    List<D> findAllDomainById(Collection<Long> ids);

    /**
     * 获取值对象
     * @param id
     * @param clazz
     * @param <V> 值对象类型
     * @return
     */
    <V> Optional<V> findValObjById(Long id, Class<V> clazz);

    /**
     * 批量按ID获取值对象
     * @param ids
     * @param clazz
     * @param <V>
     * @return
     */
    <V> List<V> findAllValObjById(Collection<Long> ids, Class<V> clazz);

    /**
     * 持久化聚合根
     * @param domainE
     * @return savedDomain
     */
    D save(D domainE);

    /**
     * 保存全部
     * @param domainEs
     * @return
     */
    default Iterator<D> saveAll(Collection<D> domainEs){
        throw new SysException("Must override the method");
    }

    /**
     * 移除
     * @param domainEs
     * @return
     */
    default void removeAll(Collection<D> domainEs){
        Set<Long> ids = domainEs.stream().map(D::getId).collect(Collectors.toSet());
        removeAllByIds(ids);
    }

    /**
     * 通过Id移除
     * @param ids
     */
    default void removeAllByIds(Collection<Long> ids){
        for (Long id: ids) {
            removeById(id);
        }
    }

    /**
     * 移除
     * @param domain
     * @return
     */
    default void remove(D domain){
        removeById(domain.getId());
    }

    /**
     * 通过Id移除
     * @param id
     */
    default void removeById(Long id){
        throw new SysException("Must override the method: removeById(Long id)");
    }
}
