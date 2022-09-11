package cn.stephen12.icecola.component.dictionary;

import cn.stephen12.icecola.component.dictionary.model.DictWrapper;
import cn.stephen12.icecola.component.dictionary.model.DictionaryVO;
import com.alibaba.cola.exception.SysException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 字典管理器
 *
 * @author ouyangsheng
 * @since 2022-03-14
 */
@Component
@Slf4j
public class DictionaryManagerImpl implements DictionaryManager {

    @Override
    public List<DictionaryVO> listDictByType(String type) {
        DictWrapper wrapper = DictionaryRegistry.dictionaryMap.get(type);
        if (wrapper == null) {
            return Collections.emptyList();
        }
        return new ArrayList<>(wrapper.getDictionaries());
    }

    @Override
    public Map<String, List<DictionaryVO>> getDictionariesByTypes(Collection<String> types) {
        if (types == null) {
            return Collections.EMPTY_MAP;
        }
        return types.stream().collect(Collectors.toMap(Function.identity(), this::listDictByType, (pre, curr) -> curr));
    }

    @Override
    public void register(Class<?> dictClazz) {
        if (DictionaryRegistry.isDictionary(dictClazz)) {
            DictionaryRegistry.registerDict(dictClazz);
        } else if (DictionaryRegistry.isDictWrapper(dictClazz)) {
            DictionaryRegistry.registerDictWrapper(dictClazz);
        }
        throw new SysException("The dictClass must implement Dictionary or DictWrapper.");
    }

    @Override
    public void register(String key, Class<?> dictClazz) {
        if (DictionaryRegistry.isDictionary(dictClazz)) {
            DictionaryRegistry.registerDict(key, dictClazz);
        } else if (DictionaryRegistry.isDictWrapper(dictClazz)) {
            DictionaryRegistry.registerDictWrapper(key, dictClazz);
        }
        throw new SysException("The dictClass must implement Dictionary or DictWrapper.");
    }

    @Override
    public Set<Class<?>> getAllEnums() {
        return new HashSet<>(DictionaryRegistry.wrapperToPrototypeMap.values());
    }

    @Override
    public Map<String, DictWrapper> getAllDictionaries() {
        return DictionaryRegistry.dictionaryMap;
    }


}
