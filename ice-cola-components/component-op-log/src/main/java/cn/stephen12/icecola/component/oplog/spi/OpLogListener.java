package cn.stephen12.icecola.component.oplog.spi;

import cn.stephen12.icecola.component.oplog.OpLogModel;

/**
 * 操作日志监听器
 * @author ouyangsheng
 * @since 2022-08-11
 **/
public interface OpLogListener {

    /**
     * 记录业务日志
     * @param opLogModel 业务日志对象
     * @param e
     */
    void onLog(OpLogModel opLogModel,Throwable e);

}
