package cn.stephen12.icecola.component.dictionary.fastjson;

import cn.stephen12.icecola.component.dictionary.DictionaryManager;
import cn.stephen12.icecola.component.dictionary.autoconfig.DictionaryScannedHandlers;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

/**
 * 序列化、反序列化器注册
 *
 * @author ouyangsheng
 * @date 2022-03-17
 **/
@ConditionalOnClass(ParserConfig.class)
@Component
public class FastJsonSettingHelper implements DictionaryScannedHandlers.Handler {

    @Override
    public void handle(DictionaryManager dictionaryManager) {
        dictionaryManager.getAllEnums().stream().forEach(this::setting);
    }

    private void setting(Class<?> clazz) {
        ParserConfig parserConfig = ParserConfig.getGlobalInstance();
        SerializeConfig serializeConfig = SerializeConfig.getGlobalInstance();

        serializeConfig.put(clazz, DictionaryCodeSerializer.INSTANCE);
        parserConfig.putDeserializer(clazz, DictionaryDeserializer.INSTANCE);
    }

}
