package cn.stephen12.icecola.component.dictionary.autoconfig;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 字典配置项
 *
 * @author ouyangsheng
 * @date 2022-03-16
 **/
@Data
@ConfigurationProperties(prefix = "ice-cola.component.dictionary")
public class DictionaryProperties {

    /**
     * 扫描路径，默认""
     */
    private String typeEnumsPackage = "";
}
