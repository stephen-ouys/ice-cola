package cn.stephen12.icecola.framework.core.infr;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.ReflectUtil;
import cn.stephen12.icecola.framework.core.domain.model.AfterDataInitializing;
import cn.stephen12.icecola.framework.core.domain.model.BaseAggregateRoot;
import cn.stephen12.icecola.framework.core.domain.model.BaseE;
import cn.stephen12.icecola.framework.core.domain.model.BaseV;
import cn.stephen12.icecola.framework.core.infr.datafiller.DataFiller;
import cn.stephen12.icecola.framework.core.infr.model.IJpaEntity;
import cn.stephen12.icecola.framework.core.infr.model.IdV;
import com.alibaba.cola.domain.ApplicationContextHelper;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ObjectFactory;
import org.mapstruct.TargetType;

import javax.persistence.EntityManager;

/**
 * 基础 Convertor
 *
 * @author ouyangsheng
 * @date 2022-04-01
 **/
public interface IEntityConverter<D extends BaseAggregateRoot<D>, E extends IJpaEntity> {
    /**
     * 转换 Domain为 Entity
     *
     * @param domain
     * @return
     */
    E convertDomainToEntity(D domain);

    /**
     * 复制领域模型到实体
     *
     * @param domain
     * @param entity
     */
    void copyFromDomainToEntity(D domain, @MappingTarget E entity);

    /**
     * 转换 Entity 为 Domain
     *
     * @param entity
     * @return
     */
    D convertEntityToDomain(E entity);

    /**
     * 复制实体到领域模型
     *
     * @param domain
     * @param entity
     */
    void copyFromEntityToDomain(E entity, @MappingTarget D domain);

    /**
     * 多态生成聚合对象
     *
     * @param idv
     * @param vType
     * @return
     */
    @ObjectFactory
    default <V extends BaseV, IDV extends IdV> V generateV(IDV idv, @TargetType Class<V> vType) {
        return DataFiller.findVById(idv, vType);
    }

    /**
     * 调用目标对象方法
     * @param target
     */
    @AfterMapping
    default void defaultAfterMapping(@MappingTarget AfterDataInitializing target){
        target.afterDataInitializing();
    }

    /**
     * 从EntityManager中获取Entity，而非直接new
     *
     * @param <D>
     */
    interface WithEm<D extends BaseAggregateRoot<D>, E extends IJpaEntity> extends IEntityConverter<D, E> {

        /**
         * 从BaseE 转换为JpaEntity的时候，从EntityManager 获取 JpaEntity
         *
         * @param domainE
         * @param jpaEntityType
         * @param <BE>          BaseE子类
         * @param <JE>
         * @return
         */
        @ObjectFactory
        default <JE extends IJpaEntity, BE extends BaseE> JE generateJpaEntity(BE domainE, @TargetType Class<JE> jpaEntityType) {
            EntityManager em = ApplicationContextHelper.getBean(EntityManager.class);
            JE jpaEntity = em.find(jpaEntityType, domainE.getId());
            if (jpaEntity != null) {
                // 不能传递原引用，因为Copy过程中Collection类型无法保证引用不变
                return BeanUtil.toBean(jpaEntity,jpaEntityType);
            }

            return ReflectUtil.newInstance(jpaEntityType);
        }

        /**
         * 从BaseV 转换为JpaEntity的时候，从EntityManager 获取
         *
         * @param valueObj
         * @param jpaEntityType
         * @param <BV>          BaseV子类
         * @param <JE>
         * @return
         */
        @ObjectFactory
        default <JE extends IJpaEntity, BV extends BaseV> JE generateJpaEntity(BV valueObj, @TargetType Class<JE> jpaEntityType) {
            EntityManager em = ApplicationContextHelper.getBean(EntityManager.class);
            JE jpaEntity = em.find(jpaEntityType, valueObj.getId());
            if (jpaEntity != null) {
                // 不能传递原引用，因为Copy过程中Collection类型无法保证引用不变
                return BeanUtil.toBean(jpaEntity,jpaEntityType);
            }

            return ReflectUtil.newInstance(jpaEntityType);
        }

    }
}
