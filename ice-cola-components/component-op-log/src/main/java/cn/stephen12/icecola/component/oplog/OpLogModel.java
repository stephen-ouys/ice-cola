package cn.stephen12.icecola.component.oplog;

import lombok.Data;

/**
 * 业务日志对象
 * @author ouyangsheng
 * @since 2022-08-11
 **/
@Data
public class OpLogModel {

    /**
     * 操作标识
     */
    private String label;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 所操作数据的ID
     */
    private Object bizId;
    /**
     * 先前状态
     */
    private String previousState;

    /**
     * 之后状态
     */
    private String postState;

    /**
     * 参数
     */
    private String param;

    /**
     * 响应信息
     */
    private String feedback;
}
