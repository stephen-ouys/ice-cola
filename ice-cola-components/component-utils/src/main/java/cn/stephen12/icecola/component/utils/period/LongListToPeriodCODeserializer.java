package cn.stephen12.icecola.component.utils.period;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 将时间戳列表反序列化为PeriodCo
 *
 * @author xiongliuyang
 * @date 2022/03/21
 */
public class LongListToPeriodCODeserializer implements ObjectDeserializer {
    public static final LongListToPeriodCODeserializer INSTANCE = new LongListToPeriodCODeserializer();

    @Override
    public PeriodCO deserialze(DefaultJSONParser defaultJSONParser, Type type, Object o) {
        // null 直接返回
        if(defaultJSONParser.getLexer().token() == JSONToken.NULL){
            return null;
        }
        List<Long> timestampBetween = defaultJSONParser.parseArray(Long.class);
        return convertToPeriodCO(timestampBetween);
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }

    private PeriodCO convertToPeriodCO(List<Long> timestampBetween){
        if(CollectionUtil.isEmpty(timestampBetween)){
            return null;
        }
        return new PeriodCO(timestampBetween);
    }
}
