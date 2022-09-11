package cn.stephen12.icecola.framework.core.domain.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

/**
 * 基本实体基类
 *
 * @author yuchenggong
 * @version 1.0
 * @date 2021/9/1
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@NoArgsConstructor
@SuperBuilder
@Data
public abstract class BaseE<E extends BaseE<E>> {

    /**
     * id
     */
    protected Long id;

    /**
     * 负载信息，由基础设施自由维护
     */
    protected Object payload;

}
