package cn.stephen12.icecola.component.dictionary;


import cn.stephen12.icecola.component.dictionary.model.DictWrapper;
import cn.stephen12.icecola.component.dictionary.model.DictionaryVO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 字典管理器
 *
 * @author ouyangsheng
 * @since 2022-03-14
 */
public interface DictionaryManager {

    /**
     * 通用枚举类型获取枚举
     *
     * @param type 枚举类型
     * @return
     */
    List<DictionaryVO> listDictByType(String type);

    /**
     * 通用枚举类型获取枚举
     *
     * @param types 枚举类型
     * @return
     */
    Map<String, List<DictionaryVO>> getDictionariesByTypes(Collection<String> types);

    /**
     * 注册字典
     *
     * @param dictClazz
     */
    void register(Class<?> dictClazz);

    /**
     * 注册字典
     *
     * @param type
     * @param dictClazz
     */
    void register(String type, Class<?> dictClazz);


    /**
     * 获取所有枚举
     *
     * @return type, enumType
     */
    Set<Class<?>> getAllEnums();

    /**
     * 获取所有字典
     *
     * @return
     */
    Map<String, DictWrapper> getAllDictionaries();
}
