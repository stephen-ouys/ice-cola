package cn.stephen12.icecola.framework.core.infr.repository;

import cn.stephen12.icecola.framework.core.infr.model.IJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA 仓库
 *
 * @author ouyangsheng
 * @date 2022-03-23
 *
 **/
public interface BaseJpaRepository<E extends IJpaEntity> extends JpaRepository<E, Long> {
}
