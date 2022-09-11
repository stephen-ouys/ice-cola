package cn.stephen12.icecola.component.dictionary.autoconfig;

import cn.stephen12.icecola.component.dictionary.DictionaryRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static org.springframework.util.StringUtils.hasLength;
import static org.springframework.util.StringUtils.tokenizeToStringArray;

/**
 * 字典扫描器
 *
 * @author ouyangsheng
 * @date 2022-03-16
 **/
@Slf4j
@Component
public class DictionaryScanner implements InitializingBean {
    private static final String STAR = "*";
    private static final String SEMICOLON = ";";
    public static final String COMMA = ",";


    private static final ResourcePatternResolver RESOURCE_PATTERN_RESOLVER = new PathMatchingResourcePatternResolver();
    private static final MetadataReaderFactory METADATA_READER_FACTORY = new CachingMetadataReaderFactory();

    private String typeEnumsPackage;

    public DictionaryScanner(DictionaryProperties properties){
        this.typeEnumsPackage = properties.getTypeEnumsPackage();
    }

    public void registerDictionaries() throws IOException {
        // TODO 自定义枚举类扫描处理
        if (hasLength(this.typeEnumsPackage)) {
            Set<Class<?>> classes;
            if (typeEnumsPackage.contains(STAR) && !typeEnumsPackage.contains(COMMA)
                    && !typeEnumsPackage.contains(SEMICOLON)) {
                classes = scanClasses(typeEnumsPackage, null);
                if (classes.isEmpty()) {
                    log.warn("Can't find class in '[" + typeEnumsPackage + "]' package. Please check your configuration.");
                }
            } else {
                classes = new HashSet<>();
                String[] typeEnumsPackageArray = tokenizeToStringArray(this.typeEnumsPackage,
                        ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
                Assert.notNull(typeEnumsPackageArray, "not find typeEnumsPackage:" + typeEnumsPackage);
                Stream.of(typeEnumsPackageArray).forEach(typePackage -> {
                    try {
                        Set<Class<?>> scanTypePackage = scanClasses(typePackage, null);
                        if (scanTypePackage.isEmpty()) {
                            log.warn("Can't find class in '[" + typePackage + "]' package. Please check your configuration.");
                        } else {
                            classes.addAll(scanTypePackage);
                        }
                    } catch (IOException e) {
                        throw new IllegalStateException("Cannot scan class in '[" + typePackage + "]' package", e);
                    }
                });
            }

            // 注册枚举
            classes.stream()
                    .filter(DictionaryRegistry::isDictionary)
                    .forEach(DictionaryRegistry::registerDict);


            // 注册枚举包装器
            classes.stream()
                    .filter(DictionaryRegistry::isDictWrapper)
                    .forEach(DictionaryRegistry::registerDictWrapper);
        }
    }

    private Set<Class<?>> scanClasses(String packagePatterns, Class<?> assignableType) throws IOException {
        Set<Class<?>> classes = new HashSet();
        String[] packagePatternArray = StringUtils.tokenizeToStringArray(packagePatterns, ",; \t\n");
        String[] var5 = packagePatternArray;
        int var6 = packagePatternArray.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            String packagePattern = var5[var7];
            Resource[] resources = RESOURCE_PATTERN_RESOLVER.getResources("classpath*:" + ClassUtils.convertClassNameToResourcePath(packagePattern) + "/**/*.class");
            Resource[] var10 = resources;
            int var11 = resources.length;

            for(int var12 = 0; var12 < var11; ++var12) {
                Resource resource = var10[var12];

                try {
                    ClassMetadata classMetadata = METADATA_READER_FACTORY.getMetadataReader(resource).getClassMetadata();
                    Class<?> clazz = Class.forName(classMetadata.getClassName());
                    if (assignableType == null || assignableType.isAssignableFrom(clazz)) {
                        classes.add(clazz);
                    }
                } catch (Throwable var16) {
                    log.warn("Cannot load the '" + resource + "'. Cause by " + var16.toString());
                }
            }
        }

        return classes;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.registerDictionaries();
    }
}
