package cn.stephen12.icecola.component.utils;


import cn.hutool.core.lang.Assert;
import org.springframework.core.GenericTypeResolver;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

/**
 * 反射工具类，提供反射相关的快捷操作
 *
 * @author Caratacus
 * @author hcl
 * @since 2016-09-22
 */
public class ClassUtil extends cn.hutool.core.util.ClassUtil {

    private static final char PACKAGE_SEPARATOR = '.';

    /**
     * 代理 class 的名称
     */
    private static final List<String> PROXY_CLASS_NAMES = Arrays.asList("net.sf.cglib.proxy.Factory"
            // cglib
            , "org.springframework.cglib.proxy.Factory"
            , "javassist.util.proxy.ProxyObject"
            // javassist
            , "org.apache.ibatis.javassist.util.proxy.ProxyObject");

    /**
     * <p>
     * 反射对象获取泛型
     * </p>
     *
     * @param clazz      对象
     * @param genericIfc 所属泛型父类
     * @param index      泛型所在位置
     * @return Class
     */
    public static Class<?> getSuperClassGenericType(final Class<?> clazz, final Class<?> genericIfc, final int index) {
        Class<?>[] typeArguments = GenericTypeResolver.resolveTypeArguments(getUserClass(clazz), genericIfc);
        return null == typeArguments ? null : typeArguments[index];
    }

    /**
     * 判断是否为代理对象
     *
     * @param clazz 传入 class 对象
     * @return 如果对象class是代理 class，返回 true
     */
    public static boolean isProxy(Class<?> clazz) {
        if (clazz != null) {
            for (Class<?> cls : clazz.getInterfaces()) {
                if (PROXY_CLASS_NAMES.contains(cls.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * <p>
     * 获取当前对象的 class
     * </p>
     *
     * @param clazz 传入
     * @return 如果是代理的class，返回父 class，否则返回自身
     */
    public static Class<?> getUserClass(Class<?> clazz) {
        Assert.notNull(clazz, "Class must not be null");
        return isProxy(clazz) ? clazz.getSuperclass() : clazz;
    }

    /**
     * <p>
     * 获取当前对象的class
     * </p>
     *
     * @param object 对象
     * @return 返回对象的 user class
     */
    public static Class<?> getUserClass(Object object) {
        Assert.notNull(object, "Instance must not be null");
        return getUserClass(object.getClass());
    }


    /**
     * <p>
     * 排序重置父类属性
     * </p>
     *
     * @param fields         子类属性
     * @param superFieldList 父类属性
     */
    public static Map<String, Field> excludeOverrideSuperField(Field[] fields, List<Field> superFieldList) {
        // 子类属性
        Map<String, Field> fieldMap = Stream.of(fields).collect(toMap(Field::getName, identity(),
                (u, v) -> {
                    throw new IllegalStateException(String.format("Duplicate key %s", u));
                },
                LinkedHashMap::new));
        superFieldList.stream().filter(field -> !fieldMap.containsKey(field.getName()))
                .forEach(f -> fieldMap.put(f.getName(), f));
        return fieldMap;
    }


}
