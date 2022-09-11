package cn.stephen12.icecola.component.office.excel.handler;

import cn.afterturn.easypoi.handler.impl.ExcelDataHandlerDefaultImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * Excel数据处理分发器
 *
 * @author ouyangsheng
 * @since  2022-06-06
 */
@Component
public class ExcelDataHandleDispatcher extends ExcelDataHandlerDefaultImpl<Object> {

    @Autowired
    private List<DataHandler> dataHandlers;

    @Override
    public Object exportHandler(Object obj, String name, Object value) {
        for (DataHandler dataHandler : dataHandlers) {
            if (dataHandler.supported(obj, name, value)) {
                return dataHandler.handle(obj, name, value);
            }
        }
        return value;
    }

    /**
     * Excel 数据处理器
     */
    public interface DataHandler {

        /**
         * 支持什么类型的数据
         *
         * @param obj
         * @param name
         * @param value
         * @return
         */
        boolean supported(Object obj, String name, Object value);

        /**
         * 实际处理
         *
         * @param obj
         * @param name
         * @param value
         * @return new Value
         */
        Object handle(Object obj, String name, Object value);
    }
}
