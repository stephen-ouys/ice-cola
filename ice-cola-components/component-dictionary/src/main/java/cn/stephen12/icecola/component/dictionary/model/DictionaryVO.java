package cn.stephen12.icecola.component.dictionary.model;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 字典VO
 *
 * @author ouyangsheng
 * @date 2022-03-15
 **/
@ApiModel(value = "DictionaryVO", description = "字典VO")
@AllArgsConstructor
@Data
public class DictionaryVO {
    @ApiModelProperty("编码")
    @JSONField(name = "value")
    private Object code;

    @ApiModelProperty("名称")
    @JSONField(name = "label")
    private String name;

    public DictionaryVO(Dictionary dictionary) {
        this.code = dictionary.getCode();
        this.name = String.valueOf(dictionary.getName());
    }

    public DictionaryVO(Dictionary dictionary, String name) {
        this.code = dictionary.getCode();
        this.name = name;
    }
}
