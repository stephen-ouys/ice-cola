package cn.stephen12.icecola.framework.core.infr.model;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 基本模型基类
 * @author yuchenggong
 * @version 1.0
 * @date 2021/9/1
 */
@EntityListeners({AuditingEntityListener.class})
@MappedSuperclass
@Data
@DynamicUpdate
@DynamicInsert
public abstract class BaseJpaEntity implements IJpaEntity {
    /**
     * id
     */
    @Id
    protected Long id;

    /**
     * 创建人工号
     */
    @CreatedBy
    @Column(name = "c_u",updatable = false)
    protected String createUserNo;

    /**
     * 创建人名称
     */
    @CreatedBy
    @Column(name = "c_name",updatable = false)
    protected String createUserName;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "c_t",updatable = false)
    protected LocalDateTime createTime;

    /**
     * 最后修改人工号
     */
    @Column(name = "u_u")
    @LastModifiedBy
    protected String updateUserNo;

    /**
     * 最后修改人名称
     */
    @Column(name = "u_name")
    @LastModifiedBy
    protected String updateUserName;

    /**
     * 最后修改时间
     */
    @Column(name = "u_t")
    @LastModifiedDate
    protected LocalDateTime updateTime;

    /**
     * 获取分布式ID标识
     * @return
     */
    public static <E extends IJpaEntity> String getLeafTag(Class<E> clazz){
        Table annotation = AnnotationUtils.findAnnotation(clazz, Table.class);
        return annotation.schema() + "_" + annotation.name();
    }
}
