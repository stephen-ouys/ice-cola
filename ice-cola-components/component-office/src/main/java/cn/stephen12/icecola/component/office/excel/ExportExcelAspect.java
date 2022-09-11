package cn.stephen12.icecola.component.office.excel;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.stephen12.icecola.component.office.excel.annotation.ExportExcel;
import cn.stephen12.icecola.component.office.excel.handler.ExcelDataHandleDispatcher;
import cn.stephen12.icecola.component.office.excel.model.ExcelVO;
import cn.stephen12.icecola.framework.model.unpacker.Unpacker;
import com.alibaba.cola.domain.ApplicationContextHelper;
import com.alibaba.cola.exception.SysException;
import org.apache.poi.ss.usermodel.Workbook;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;

/**
 * 导出Excel注解
 *
 * @author ouyangsheng
 * @date 2022-03-31
 **/
@Aspect
@Component
public class ExportExcelAspect {

    @Autowired
    private HttpServletResponse response;
    @Autowired
    private ExcelDataHandleDispatcher excelDataHandleDispatcher;

    @AfterReturning(returning = "resultObj", pointcut = "@annotation(exportExcel)")
    public void exportExcel(ExportExcel exportExcel, Object resultObj) {
        String fileName = getFileName(exportExcel, resultObj);
        String sheetName = getSheetName(exportExcel, resultObj);

        if (StrUtil.isBlank(fileName) || StrUtil.isBlank(sheetName)) {
            throw new SysException("Please correctly set the annotation @ExportExcel.");
        }

        //拆包
        Unpacker unpacker = getUnpacker(exportExcel.unpacker());
        List data = unpacker.unpack(resultObj);

        //TODO 按理说可以把这里去掉，为空的情况，直接返回空excel
        Assert.state(!CollectionUtils.isEmpty(data), "没有数据可导出");

        Class<?> modelType = data.get(0).getClass();
        try {
            response.setHeader("Access-Control-Expose-Headers", "content-disposition");
            response.addHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xls");

            ExportParams exportParams = new ExportParams();
            exportParams.setDataHandler(this.excelDataHandleDispatcher);
            exportParams.setExclusions(getExclusions());

            Workbook workbook = ExcelExportUtil.exportExcel(exportParams, modelType, data);
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (Exception e) {
            throw new SysException("Error occurred when export data to excel.", e);
        } finally {
            //清理上下文
            ExcelContext.clear();
        }
    }


    /**
     * 拆包器
     *
     * @param unpackerClass
     * @return
     */
    private Unpacker getUnpacker(Class<? extends Unpacker> unpackerClass) {
        //尝试从容器中获取实例
        Unpacker unpacker = ApplicationContextHelper.getBean(unpackerClass);
        if (unpacker == null) {
            try {
                unpacker = unpackerClass.getConstructor().newInstance();
            } catch (Exception e) {
                throw new SysException("Instancing " + unpackerClass.getName() + " fail. Please ensure your unpacker class is instantiatable", e);
            }
        }

        return unpacker;
    }

    /**
     * 获取文件名
     *
     * @param exportExcel
     * @param resultObj
     * @return
     */
    private String getFileName(ExportExcel exportExcel, Object resultObj) {
        if (resultObj != null && resultObj instanceof ExcelVO) {
            return ((ExcelVO) resultObj).getFileName();
        }
        return exportExcel.fileName();
    }

    /**
     * 获取Sheet 名称
     *
     * @param exportExcel
     * @param resultObj
     * @return
     */
    private String getSheetName(ExportExcel exportExcel, Object resultObj) {
        if (resultObj != null && resultObj instanceof ExcelVO) {
            return ((ExcelVO) resultObj).getSheetName();
        }
        return exportExcel.shellName();
    }

    /**
     * 获取需要排除的列
     *
     * @return
     */
    private String[] getExclusions() {
        int exclusionNum = ExcelContext.getExcludedFields().size();
        String[] exclusions = new String[exclusionNum];
        return ExcelContext.getExcludedFields().toArray(exclusions);
    }


}
