package cn.stephen12.icecola.component.dictionary.apidoc;

import cn.stephen12.icecola.component.dictionary.DictionaryManager;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.bean.validators.plugins.Validators;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterContext;

import java.util.Optional;

/**
 * dictionaryType 参数文档
 *
 * @author ouyangsheng
 * @date 2022-03-15
 **/
@Component
@NoArgsConstructor
@Order
@Slf4j
public class DictionaryTypeParamPlugin implements ParameterBuilderPlugin {
    @Autowired
    private DictionaryManager dictionaryManager;

    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }

    @Override
    public void apply(ParameterContext context) {
        Optional<ApiDictionaryType> apiDictionaryType = Validators.annotationFromParameter(context, ApiDictionaryType.class);
        if (apiDictionaryType.isPresent()) {
            log.debug("@ApiDictionaryType present");
            context.parameterBuilder()
                    .parameterType(apiDictionaryType.get().parameterType())
                    .description(DictionaryParamDescriptor.buildDescription(dictionaryManager));
        }
    }

}
