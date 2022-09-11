package cn.stephen12.icecola.framework.core.infr;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.ReflectUtil;
import cn.stephen12.icecola.framework.core.domain.model.BaseAggregateRoot;
import cn.stephen12.icecola.framework.core.domain.model.BaseE;
import cn.stephen12.icecola.framework.core.domain.model.BaseV;
import cn.stephen12.icecola.framework.core.infr.datafiller.DataFiller;
import cn.stephen12.icecola.framework.core.infr.model.IJpaEntity;
import cn.stephen12.icecola.framework.core.infr.model.IdV;
import org.mapstruct.MappingTarget;
import org.mapstruct.ObjectFactory;
import org.mapstruct.TargetType;

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
     * 带负载信息的转换器
     *
     * @param <D>
     */
    interface WithEntityPayload<D extends BaseAggregateRoot<D>, E extends IJpaEntity> extends IEntityConverter<D, E>{
        /**
         * 从JpaEntity 转换为BaseE的时候，传递负载信息
         *
         * @param jpaEntity
         * @param domainEType
         * @param <BE>
         * @param <JE>
         * @return
         */
        @ObjectFactory
        default <BE extends BaseE, JE extends IJpaEntity> BE generateDomainE(JE jpaEntity, @TargetType Class<BE> domainEType) {
            BE domainE = ReflectUtil.newInstance(domainEType);

            final IJpaEntity payload =  ReflectUtil.newInstance(jpaEntity.getClass());
            BeanUtil.copyProperties(jpaEntity, payload, CopyOptions.create().ignoreNullValue().ignoreError());

            domainE.setPayload(payload);
            return domainE;
        }

        /**
         * 从JpaEntity 转换为BaseE的时候，传递负载信息
         *
         * @param domainE
         * @param jpaEntityType
         * @param <BE>          BaseE子类
         * @param <JE>
         * @return
         */
        @ObjectFactory
        default <JE extends IJpaEntity, BE extends BaseE> JE generateJpaEntity(BE domainE, @TargetType Class<JE> jpaEntityType) {
            JE jpaEntity = ReflectUtil.newInstance(jpaEntityType);
            Object payload =  domainE.getPayload();
            BeanUtil.copyProperties(payload, jpaEntity, CopyOptions.create().ignoreError().ignoreNullValue());
            return jpaEntity;
        }

    }
}
