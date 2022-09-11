package cn.stephen12.icecola.framework.model.unpacker;

import java.util.List;

/**
 * 拆包器
 *
 * @author ouyangsheng
 * @date 2022-03-31
 **/
public interface Unpacker {

    /**
     * 返回结果拆包器
     * @param returnObj
     * @return
     */
    List<Object> unpack(Object returnObj);
}
