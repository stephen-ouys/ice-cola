package cn.stephen12.icecola.framework.model.unpacker;

import cn.stephen12.icecola.framework.model.dto.AsCollection;
import cn.stephen12.icecola.framework.model.vo.RestResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 默认拆包器
 *
 * @author ouyangsheng
 * @date 2022-03-31
 **/
public class DefaultUnpacker implements Unpacker{

    @Override
    public List<Object> unpack(Object returnObj) {
        if(returnObj instanceof List){
            return (List<Object>) returnObj;
        }else if(returnObj instanceof Collection){
            return new ArrayList<>((Collection<?>) returnObj);
        } else if(returnObj instanceof AsCollection) {
            return new ArrayList<>(((AsCollection)returnObj).toCollection());
        } else if(returnObj instanceof RestResult){
            Object data = ((RestResult) returnObj).getData();
            return unpack(data);
        }
        return Collections.emptyList();
    }
}
