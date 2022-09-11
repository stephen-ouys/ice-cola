package cn.stephen12.icecola.component.dictionary.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 * <p>
 * 通用字典接口
 * </p>
 *
 * @author ouyangsheng
 * @date 2022-03-08
 **/
public interface Dictionary<C extends Serializable, N extends Serializable, E extends Enum<E>> extends Dict<C, N, E> {

    /**
     * 获取枚举字段
     *
     * @param clazz
     * @param code
     * @param <C>
     * @param <N>
     * @param <E>
     * @return
     */
    static <C extends Serializable, N extends Serializable, E extends Enum<E> & Dictionary<C, N, E>> Optional<E> getByCode(Class<E> clazz, Object code) {
        E[] enumList = clazz.getEnumConstants();
        for (E e : enumList) {
            if (Objects.equals(code, e.getCode())) {
                return Optional.of(e);
            }
        }
        return Optional.empty();
    }

    /**
     * 枚举Code ToString 方法，会用于判断两个枚举是否相等
     *
     * @return
     */
    default String codeToStr() {
        return String.valueOf(getCode());
    }

}
