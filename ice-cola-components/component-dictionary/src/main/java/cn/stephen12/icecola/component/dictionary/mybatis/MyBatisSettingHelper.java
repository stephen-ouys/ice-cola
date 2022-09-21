package cn.stephen12.icecola.component.dictionary.mybatis;

import cn.stephen12.icecola.component.dictionary.DictionaryManager;
import cn.stephen12.icecola.component.dictionary.autoconfig.DictionaryScannedHandlers;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Component;

/**
 * config MyBatis EnumTypeHandler
 *
 * @author ouyangsheng
 * @date 2022-09-17
 **/
@Slf4j
@ConditionalOnClass(SqlSessionFactory.class)
@Component
public class MyBatisSettingHelper extends ApplicationObjectSupport implements DictionaryScannedHandlers.Handler {
    /**
     * MyBatis TypeHandlerRegistry
     */
    private TypeHandlerRegistry typeHandlerRegistry;

    @Override
    public void handle(DictionaryManager dictionaryManager) {
        SqlSessionFactory sqlSessionFactory = this.getApplicationContext().getBean(SqlSessionFactory.class);
        if (sqlSessionFactory == null) {
            log.warn("Miss bean SqlSessionFactory");
            return;
        }
        this.typeHandlerRegistry = sqlSessionFactory.getConfiguration().getTypeHandlerRegistry();
        dictionaryManager.getAllEnums().stream().forEach(this::setting);
    }

    private void setting(Class<?> clazz) {
        typeHandlerRegistry.register(clazz, MybatisEnumTypeHandler.class);
    }

}
