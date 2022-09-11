package cn.stephen12.icecola.component.dictionary.hutool;

import cn.hutool.core.convert.AbstractConverter;
import cn.hutool.core.convert.ConverterRegistry;
import cn.stephen12.icecola.component.dictionary.DictionaryManager;
import cn.stephen12.icecola.component.dictionary.autoconfig.DictionaryScannedHandlers;
import cn.stephen12.icecola.component.dictionary.model.Dictionary;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

/**
 * HuTool Converter注册
 *
 * @author ouyangsheng
 * @date 2022-05-31
 **/
@ConditionalOnClass(ConverterRegistry.class)
@Component
public class ConverterSettingHelper implements DictionaryScannedHandlers.Handler {

    @Override
    public void handle(DictionaryManager dictionaryManager) {
        dictionaryManager.getAllEnums().stream().forEach(this::setting);
    }

    private void setting(Class<?> clazz) {
        ConverterRegistry registry = ConverterRegistry.getInstance();
        registry.putCustom(clazz, new DictionaryConverter(clazz));
    }


    /**
     * 字典转换器
     */
    static class DictionaryConverter extends AbstractConverter<Object> {
        /**
         * 枚举类
         */
        private final Class<?> enumClass;

        /**
         * 构造
         *
         * @param enumClass 转换成的目标Enum类
         */
        public DictionaryConverter(Class enumClass) {
            this.enumClass = enumClass;
        }

        /**
         * 基础类型 或 String 转化为枚举
         *
         * @param value
         * @return
         */
        @Override
        protected Object convertInternal(Object value) {
            return Dictionary.getByCode((Class) enumClass, value).orElse(null);
        }

        @Override
        public Class getTargetType() {
            return this.enumClass;
        }
    }
}