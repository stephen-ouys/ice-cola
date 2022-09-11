package cn.stephen12.icecola.framework.core.infr.id;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.lang.Assert;
import cn.stephen12.icecola.framework.core.domain.IdGeneratorGateway;
import cn.stephen12.icecola.framework.core.domain.annotation.BizTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认Id生成Gateway
 * @author ouyangsheng
 * @since 2022-04-22
 * @see IdNoGenerator
 * @see BizTag
 **/
@Primary
@Component
public class IdGeneratorGatewayImpl implements IdGeneratorGateway {
    /**
     * Jvm 缓存，避免重复反射获取注解
     */
    private final static Map<Class, String> domainTypeToTagMap = new ConcurrentHashMap<>(16);

    @Autowired
    protected IdNoGenerator idNoGenerator;

    @Override
    public Long generateId(Class<?> domainClass) {
        String tag = getBizTag(domainClass);
        return idNoGenerator.generateId(tag);
    }

    @Override
    public String generateNo(Class<?> entityClass) {
        String tag = getBizTag(entityClass);
        return idNoGenerator.generateNo(tag);
    }

    /**
     * 获取业务标识，用于获取ID
     * @param domainClass
     * @return
     */
    protected String getBizTag(Class<?> domainClass){
        //尝试从缓存中获取，如果没有，则从注解中获取
        String tag = domainTypeToTagMap.get(domainClass);
        if (tag == null) {
            BizTag bizTag = AnnotationUtil.getAnnotation(domainClass, BizTag.class);
            Assert.state(bizTag != null, "domainClass({})  must contain annotation @BizTag", domainClass.toGenericString());

            //重新写入缓存
            tag = bizTag.value();
            domainTypeToTagMap.put(domainClass,tag);
        }
        return tag;
    }


}

