package cn.stephen12.icecola.component.utils.period;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.classmate.TypeResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ModelBuilder;
import springfox.documentation.builders.ModelPropertyBuilder;
import springfox.documentation.schema.ModelProperty;
import springfox.documentation.schema.ResolvedTypes;
import springfox.documentation.schema.TypeNameExtractor;
import springfox.documentation.schema.TypeNameIndexingAdapter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.EnumTypeDeterminer;
import springfox.documentation.spi.schema.ModelBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelContext;

import java.util.Map;

/**
 * @author ouyangsheng
 * @date 2022-05-05
 **/
@Component
public class PeriodModelBuilderPlugin implements ModelBuilderPlugin {

    private final TypeResolver typeResolver;
    private final TypeNameExtractor typeNameExtractor;
    private final EnumTypeDeterminer enumTypeDeterminer;

    @Autowired
    public PeriodModelBuilderPlugin(
            TypeResolver typeResolver,
            TypeNameExtractor typeNameExtractor,
            EnumTypeDeterminer enumTypeDeterminer) {
        this.typeResolver = typeResolver;
        this.typeNameExtractor = typeNameExtractor;
        this.enumTypeDeterminer = enumTypeDeterminer;
    }

    @Override
    public void apply(ModelContext context) {
        JsonAsPeriod annotation = AnnotationUtil.getAnnotation(context.getType().getErasedType(), JsonAsPeriod.class);
        if (annotation != null) {
            ModelBuilder builder = context.getBuilder();
            Map<String, ModelProperty> properties = BeanUtil.getProperty(builder, "properties");
            ModelProperty periodProperty = new ModelPropertyBuilder()
                    .name(annotation.periodKey())
                    .type(typeResolver.arrayType(Long.class))
                    .required(annotation.apiProperty().required())
                    .description(annotation.apiProperty().name())
                    .build()
                    .updateModelRef(
                            ResolvedTypes.modelRefFactory(context, enumTypeDeterminer, typeNameExtractor, new TypeNameIndexingAdapter().getNames()));
            properties.put(annotation.periodKey(), periodProperty);
            properties.remove(annotation.startField());
            properties.remove(annotation.endField());
        }
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }

}
