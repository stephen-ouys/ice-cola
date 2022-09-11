package cn.stephen12.icecola.component.dictionary.model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 枚举包装器
 *
 * @author ouyangsheng
 * @date 2022-04-24
 **/
@Slf4j
public class DefaultDictWrapper implements DictWrapper {
    @Setter
    @Getter
    private Class prototypeClass;

    /**
     * 包装值
     *
     * @return
     */
    @Override
    public List<DictionaryVO> getDictionaries() {
        if (prototypeClass.isEnum()) {
            return Arrays.stream(prototypeClass.getEnumConstants()).map(e -> {
                //如果枚举有实现 ToDictionaryVO
                if (ToDictionaryVO.class.isAssignableFrom(prototypeClass)) {
                    return ((ToDictionaryVO) e).toDictionaryVO();
                }
                return new DictionaryVO((Dictionary) e);
            }).collect(Collectors.toList());
        }
        log.error("Error: the prototype class is not a enum.");
        return Collections.EMPTY_LIST;
    }
}
