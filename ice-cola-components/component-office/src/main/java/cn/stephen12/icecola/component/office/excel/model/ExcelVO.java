package cn.stephen12.icecola.component.office.excel.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Excel 输出结果
 * <p>
 *     如果使用此VO输出，则会优先使用此VO中的文件名和sheetName
 * </p>
 *
 * @author ouyangsheng
 * @date 2022-06-06
 **/
@Data
public class ExcelVO<T> {
    /**
     * 文件名
     */
    private String fileName;

    /**
     * sheet名
     */
    private String sheetName;
    /**
     * 记录
     */
    private List<T> rows;

    /**
     * 构造实例
     * @param rows
     * @param fileName
     * @param sheetName
     * @param <T>
     * @return
     */
    public static <T> ExcelVO<T> instance(Collection<T> rows, String fileName, String sheetName) {
        ExcelVO<T> instance = new ExcelVO<>();
        instance.setFileName(fileName);
        instance.setSheetName(sheetName);
        instance.setRows(new ArrayList<>(rows));
        return instance;
    }
}
