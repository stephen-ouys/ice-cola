package cn.stephen12.icecola.component.dictionary.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 通用字典接口
 *
 * @author ouyangsheng
 * @date 2022-03-08
 **/
@ApiModel(value ="CommonDictionary",description = "通用枚举")
public interface CommonDictionary<E extends Enum<E>> extends Dictionary<String,String, E>{

    /**
     * 字典Key
     *
     * @return
     */
    @Override
    @ApiModelProperty("编码")
    String getCode();

    /**
     * 字典名称（中文）
     *
     * @return
     */
    @Override
    @ApiModelProperty("名称")
    String getName();

}
