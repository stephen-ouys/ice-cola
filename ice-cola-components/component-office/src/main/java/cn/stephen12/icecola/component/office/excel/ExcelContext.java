package cn.stephen12.icecola.component.office.excel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Excel 导入导出上下文
 *
 * @author ouyangsheng
 * @date 2022-06-06
 **/
public class ExcelContext {

    private static final ThreadLocal<Set<String>> excludedFieldsThreadLocal = new ThreadLocal<>();

    /**
     * 排除某些列
     *
     * @param fields
     */
    public static void excludeFields(Collection<String> fields) {
        if (excludedFieldsThreadLocal.get() == null) {
            excludedFieldsThreadLocal.set(new HashSet<>(16));
        }

        excludedFieldsThreadLocal.get().addAll(fields);
    }

    /**
     * 获取所有需要导出的列
     *
     * @return
     */
    public static List<String> getExcludedFields() {

        return new ArrayList<>();
    }

    /**
     * 清理
     */
    public static void clear() {
        excludedFieldsThreadLocal.remove();
    }
}
