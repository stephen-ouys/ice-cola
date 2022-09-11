package cn.stephen12.icecola.component.office.excel.handler;

import cn.stephen12.icecola.component.dictionary.model.Dictionary;
import org.springframework.stereotype.Component;


/**
 * 自定义excel数据处理
 * @author xiongliuyang
 * @version 1.0
 * @date 2022/4/18
 */
@Component
public class DictionaryDataHandler implements ExcelDataHandleDispatcher.DataHandler {

    @Override
    public boolean supported(Object obj, String name, Object value) {
        return Dictionary.class.isAssignableFrom(value.getClass());
    }

    @Override
    public Object handle(Object obj, String name, Object value) {
        Dictionary dictionary = (Dictionary) value;
        return dictionary.getName();
    }
}
