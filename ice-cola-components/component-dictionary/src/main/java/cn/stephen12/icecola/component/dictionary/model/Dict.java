package cn.stephen12.icecola.component.dictionary.model;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * 顶层字典接口
 *
 * @author ouyangsheng
 * @date 2022-03-18
 **/
public interface Dict<C extends Serializable, N extends Serializable, E extends Enum<E>> {
    /**
     * 字典Key
     *
     * @return
     */
    C getCode();

    /**
     * 字典名称（中文）
     *
     * @return
     */
    N getName();

    /**
     * 获取Code()方法的类型
     * @param clazz
     * @return
     */
    static Type getCodeType(Class<? extends Dict> clazz){
        try {
            Method getCodeMethod = clazz.getMethod("getCode");
           return getCodeMethod.getReturnType();
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("The class["+clazz.getName()+"] must implement interface Dict",e);
        }
    }

}
