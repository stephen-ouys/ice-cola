package cn.stephen12.icecola.component.dictionary.apidoc;

import cn.stephen12.icecola.component.dictionary.DictionaryManager;
import cn.stephen12.icecola.component.dictionary.model.DictWrapper;
import io.swagger.annotations.ApiModel;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.stream.Collectors;

/**
 * 字典参数描述器
 *
 * @author ouyangsheng
 * @date 2022-03-17
 **/
public class DictionaryParamDescriptor {

    /**
     * dictionaryType 参数描述信息
     *
     * @return
     */
    public static String buildDescription(DictionaryManager dictionaryManager) {
        return dictionaryManager.getAllDictionaries().entrySet().stream().map(entry ->
        {
            if (entry.getValue() != null) {
                String type = entry.getKey();
                String description = "";
                DictWrapper dictWrapper = entry.getValue();

                //先从包装器中获取
                ApiModel apiModel = AnnotationUtils.getAnnotation(dictWrapper.getClass(), ApiModel.class);
                if (apiModel != null) {
                    description = apiModel.description();
                } else {
                    //如果没有，再从原型类上获取
                    apiModel = AnnotationUtils.getAnnotation(dictWrapper.getPrototypeClass(), ApiModel.class);
                    if (apiModel != null) {
                        description = apiModel.description();
                    }
                }
                return type + ": " + description;
            }
            return null;
        }).collect(Collectors.joining("；", "可选项：", ""));
    }
}
