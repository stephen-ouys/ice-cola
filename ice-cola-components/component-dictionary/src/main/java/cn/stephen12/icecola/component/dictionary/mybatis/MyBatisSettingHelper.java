package cn.stephen12.icecola.component.dictionary.mybatis;

import cn.stephen12.icecola.component.dictionary.DictionaryManager;
import cn.stephen12.icecola.component.dictionary.autoconfig.DictionaryScannedHandlers;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

/**
 * 序列化、反序列化器注册
 *
 * @author ouyangsheng
 * @date 2022-03-17
 **/
@ConditionalOnBean(SqlSessionFactory.class)
@Component
public class MyBatisSettingHelper implements DictionaryScannedHandlers.Handler {

    @Autowired
    private SqlSessionFactory sessionFactory;

    @Override
    public void handle(DictionaryManager dictionaryManager) {
        dictionaryManager.getAllEnums().stream().forEach(this::setting);
    }

    private void setting(Class<?> clazz) {
        sessionFactory.getConfiguration().getTypeHandlerRegistry().register(clazz,MybatisEnumTypeHandler.class);
    }

}
