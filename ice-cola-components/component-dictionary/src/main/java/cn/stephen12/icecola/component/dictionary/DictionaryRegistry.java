package cn.stephen12.icecola.component.dictionary;

import cn.hutool.core.util.StrUtil;
import cn.stephen12.icecola.component.dictionary.model.DefaultDictWrapper;
import cn.stephen12.icecola.component.dictionary.model.DictWrapper;
import cn.stephen12.icecola.component.dictionary.model.Dictionary;
import com.alibaba.cola.exception.SysException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.util.StringUtils.hasLength;

/**
 * 枚举字典字典注册中心
 *
 * @author ouyangsheng
 * @date 2022-03-16
 **/
@Slf4j
public class DictionaryRegistry {
    /**
     * 原始包装类型、不能被注册的
     */
    private final static List<Class<?>> ORIGIN_DICT_WRAPPER_CLASSES = Arrays.asList(DictWrapper.class, DefaultDictWrapper.class);
    private final static String DEFAULT_SUFFIX = "Enum";

    static final Map<String, DictWrapper> dictionaryMap = new ConcurrentHashMap(16);
    static final Map<DictWrapper, Class<?>> wrapperToPrototypeMap = new ConcurrentHashMap(16);

    public static void registerDictWrapper(Class<?> wrapperClazz) {
        String key = getDictionaryKey(wrapperClazz);
        registerDictWrapper(key, wrapperClazz);
    }

    public static void registerDictWrapper(String key, Class<?> wrapperClazz) {
        if (isDictWrapper(wrapperClazz)) {
            try {
                Constructor<?> constructor = wrapperClazz.getConstructor();
                DictWrapper wrapper = (DictWrapper) constructor.newInstance();

                register0(key, wrapper);
            } catch (Exception e) {
                throw new SysException("Error occurred when register dictWrapper[" + wrapperClazz + "]: ", e);
            }
        } else {
            throw new SysException("wrapperClazz(" + wrapperClazz + ") must implement DictWrapper");
        }
    }

    public static void registerDict(Class<?> enumClazz) {
        String key = getDictionaryKey(enumClazz);
        registerDict(key, enumClazz);
    }

    public static void registerDict(String key, Class<?> enumClazz) {
        if (isDictionary(enumClazz)) {
            DefaultDictWrapper dictWrapper = new DefaultDictWrapper();
            dictWrapper.setPrototypeClass(enumClazz);

            register0(key, dictWrapper);
        } else {
            throw new SysException("enumClazz(" + enumClazz + ") must implement Dictionary and be a enum");
        }
    }


    public static boolean isDictionary(Class<?> clazz) {
        return Dictionary.class.isAssignableFrom(clazz) && clazz.isEnum();
    }

    public static boolean isDictWrapper(Class<?> clazz) {
        if (ORIGIN_DICT_WRAPPER_CLASSES.contains(clazz)) {
            return false;
        }
        return DictWrapper.class.isAssignableFrom(clazz);
    }

    public static String getDictionaryKey(Class<?> cls) {
        String key;
        DictionaryKey annotation = AnnotationUtils.findAnnotation(cls, DictionaryKey.class);
        if (annotation != null && hasLength(annotation.value())) {
            key = annotation.value();
        } else {
            key = StringUtils.uncapitalize(cls.getSimpleName());

            //如果有 Enum后缀，则移除Enum
            int indexOfSuffix = key.indexOf(DEFAULT_SUFFIX);
            if (indexOfSuffix > 0) {
                key = key.substring(0, indexOfSuffix);
            }
        }

        // 前端要求字典key 统一下划线格式
        return StrUtil.toUnderlineCase(key);
    }

    private static void register0(String key, DictWrapper wrapper) {
        dictionaryMap.put(key, wrapper);
        wrapperToPrototypeMap.put(wrapper, wrapper.getPrototypeClass());
    }
}
