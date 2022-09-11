package cn.stephen12.icecola.component.dictionary.model;





import cn.stephen12.icecola.component.utils.ClassUtil;

import java.util.Collection;

/**
 * 枚举包装器
 *
 * @author ouyangsheng
 * @date 2022-04-24
 **/
public interface DictWrapper<P> {

    /**
     * 设置包装原型类
     * 当实现类未指定具体泛型参数时，需要覆盖此方法
     *
     * @param prototypeClass
     */
    default void setPrototypeClass(Class<P> prototypeClass) {
    }

    /**
     * 获取包装原型类
     * 当实现类未指定具体泛型参数时，需要覆盖此方法
     *
     * @return
     */
    default Class<P> getPrototypeClass(){
        return (Class<P>) ClassUtil.getSuperClassGenericType(this.getClass(), DictWrapper.class, 0);
    }

    /**
     * 包装值
     *
     * @return
     */
    Collection<DictionaryVO> getDictionaries();
}
