package cn.stephen12.icecola.framework.core.infr.id;

/**
 * Id 或者 No生成器（ 基于Tag)
 *
 * @author ouyangsheng
 * @date 2022-05-29
 * @see cn.stephen12.icecola.framework.core.domain.annotation.BizTag
 **/
public interface IdNoGenerator {

    /**
     * 生成Id
     *
     * @param tag
     * @return
     */
    Long generateId(String tag);

    /**
     * 生成 no
     *
     * @param tag
     * @return
     */
    String generateNo(String tag);

}
