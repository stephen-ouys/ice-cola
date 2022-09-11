package cn.stephen12.icecola.framework.core.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 基本值对象基类
 *
 * @author ouyangsheng
 * @since  2022-05-13
 */
@ToString
@NoArgsConstructor
@SuperBuilder
@Data
public abstract class BaseV<E extends BaseV<E>> {

    /**
     * id
     */
    protected Long id;

}
