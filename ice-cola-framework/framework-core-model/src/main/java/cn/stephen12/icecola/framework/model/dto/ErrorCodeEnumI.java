package cn.stephen12.icecola.framework.model.dto;



/**
 * 错误码枚举接口
 * @author ouyangsheng
 * @date 2022-05-26
 **/
public interface ErrorCodeEnumI<E extends Enum<E>>  {
    /**
     * 编码
     * @return
     */
    Integer getCode();
    /**
     * 错误消息
     * @return
     */
    String getMessage();

    /**
     * 名称默认为消息
     * @return
     */
    default String getName(){
        return this.getMessage();
    }

}
