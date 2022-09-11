package cn.stephen12.icecola.component.dictionary.apidoc;

import com.google.common.base.Optional;
import cn.stephen12.icecola.component.dictionary.DictionaryFiled;
import cn.stephen12.icecola.component.dictionary.DictionaryManager;
import cn.stephen12.icecola.component.dictionary.DictionaryRegistry;
import cn.stephen12.icecola.component.dictionary.model.DictionaryVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import springfox.documentation.schema.Annotations;
import springfox.documentation.service.AllowableListValues;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;
import springfox.documentation.swagger.schema.ApiModelProperties;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * Swagger 插件，对枚举属性的支持
 * </p>
 *
 * @author ouyangsheng
 * @date 2021-03-25
 */
@Slf4j
@Component
public class EnumModelPropertyPlugin implements ModelPropertyBuilderPlugin {
    @Autowired
    private DictionaryManager dictionaryManager;

    @Override
    public void apply(ModelPropertyContext context) {
        buildDictionaryTypeProperty(context);
        buildEnumModelProperty(context);
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return true;
    }


    /**
     * 枚举类型的属性文档
     *
     * @param context
     */
    private void buildEnumModelProperty(ModelPropertyContext context) {
        Optional<ApiModelProperty> annotation = findApiModelProperty(context);
        final Class<?> rawPrimaryType = context.getBeanPropertyDefinition().get().getRawPrimaryType();
        //过滤得到目标类型 实现Dictionary 的枚举
        if (annotation.isPresent() && DictionaryRegistry.isDictionary(rawPrimaryType)) {

            //获取指定包装器
            Optional<DictionaryFiled> dictionaryFiled = findAnnotation(context, DictionaryFiled.class);

            //获取字典Key
            String dictKey = dictionaryFiled.isPresent() ? DictionaryRegistry.getDictionaryKey(dictionaryFiled.get().wrapperClass())
                    : DictionaryRegistry.getDictionaryKey(rawPrimaryType);

            //获取Enum的code值
            Collection<DictionaryVO> dictionaryVos = dictionaryManager.listDictByType(dictKey);
            final List<String> displayValues = dictionaryVos.stream().map(dict -> " " + dict.getCode() + " - " + dict.getName()).collect(Collectors.toList());
            final AllowableListValues allowableListValues = new AllowableListValues(displayValues, rawPrimaryType.getTypeName());

            context.getBuilder()
                    .description(annotation.get().value() + " [枚举 key: " + dictKey + "] ")
                    .allowableValues(allowableListValues)
                    .type(context.getResolver().resolve(String.class));
        }
    }

    private void buildDictionaryTypeProperty(ModelPropertyContext context) {
        Optional<ApiDictionaryType> apiDictionaryType = findAnnotation(context, ApiDictionaryType.class);
        if (apiDictionaryType.isPresent()) {
            log.debug("@ApiDictionaryType present");
            context.getBuilder().description(DictionaryParamDescriptor.buildDescription(dictionaryManager));
        }
    }

    private Optional<ApiModelProperty> findApiModelProperty(ModelPropertyContext context) {
        Optional<ApiModelProperty> annotation = Optional.absent();

        if (context.getAnnotatedElement().isPresent()) {
            annotation = annotation.or(Optional.fromJavaUtil(ApiModelProperties.findApiModePropertyAnnotation(context.getAnnotatedElement().get())));
        }
        if (context.getBeanPropertyDefinition().isPresent()) {
            annotation = annotation.or(findAnnotation(context, ApiModelProperty.class));
        }
        return annotation;
    }

    private <T extends Annotation> Optional<T> findAnnotation(ModelPropertyContext context, Class<T> clazz) {
        if (context.getBeanPropertyDefinition().isPresent()) {
            return Optional.fromJavaUtil(Annotations.findPropertyAnnotation(
                    context.getBeanPropertyDefinition().get(),
                    clazz));
        }
        return Optional.absent();
    }
}