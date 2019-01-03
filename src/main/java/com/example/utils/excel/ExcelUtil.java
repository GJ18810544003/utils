package com.example.utils.excel;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Wang Siqi
 * @date 2018/11/13
 */
public class ExcelUtil {

    /**
     * 对象序列化版本号名称
     */
    public static final String UID = "serialVersionUID";

    public static <T> XSSFWorkbook writeExcel(Class<T> clazz, List<T> dataModels,
        String[] fieldNames, String[] titles) throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 创建工作表
        String sheetName = "sheet1";
        XSSFSheet sheet = workbook.createSheet(sheetName);
        XSSFRow headRow = sheet.createRow(0);
        // 添加表格标题
        for (int i = 0; i < titles.length; i++) {
            XSSFCell cell = headRow.createCell(i);
            cell.setCellValue(titles[i]);
            // 设置单元格格式，title格式
            XSSFCellStyle cellStyle = CellStyleUtil.getTitleStyle(workbook);
            // 设置自动换行
            cellStyle.setWrapText(true);
            cell.setCellStyle(cellStyle);
            // 设置单元格宽度
            sheet.setColumnWidth(i, 10000);
        }
        // 添加表格内容
        for (int i = 0; i < dataModels.size(); i++) {
            T target = dataModels.get(i);
            XSSFRow row;
            if (titles.length == 0) {
                row = sheet.createRow(i);
            }else {
                row = sheet.createRow(i + 1);
            }
            // 遍历属性列表
            for (int j = 0; j < fieldNames.length; j++) {
                // 通过反射获取属性的值域
                String fieldName = fieldNames[j];
                if (fieldName == null || UID.equals(fieldName)) {
                    continue; // 过滤serialVersionUID属性
                }
                Object result = invokeGetter(target, fieldName);
                XSSFCell cell = row.createCell(j);
                XSSFCellStyle cellStyle = CellStyleUtil.getCellStyle(workbook);
                //cellStyle.setWrapText(true);  //自动换行
                cell.setCellStyle(cellStyle);
                cell.setCellValue(String.valueOf(result));
            }
        }
        return workbook;
    }

    /**
     * 反射调用指定对象属性的getter方法
     *
     * @param <T> 泛型
     * @param target 指定对象
     * @param fieldName 属性名
     * @return 返回调用后的值
     */
    public static <T> Object invokeGetter(T target, String fieldName)
        throws NoSuchMethodException, SecurityException,
        IllegalAccessException, IllegalArgumentException,
        InvocationTargetException {
        // 如果属性名为xxx，则方法名为getXxx
        String methodName = "get" + StringUtil.firstCharUpperCase(fieldName);
        Method method = target.getClass().getMethod(methodName);
        return method.invoke(target);
    }

    /**
     * 仅限读取的对象属性均为string类型，未做其他类型转换
     * @param workbook
     * @param clazz
     * @param fieldNames
     * @param sheetNo
     * @param hasTitle
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> readExcel(XSSFWorkbook workbook, Class<T> clazz, String[] fieldNames,
                                 int sheetNo, boolean hasTitle) throws Exception {
        List<T> dataModels = new ArrayList<T>();
        // 获取excel工作簿
        XSSFSheet sheet = workbook.getSheetAt(sheetNo);
        int start = sheet.getFirstRowNum() + (hasTitle ? 1 : 0);
        // 如果有标题则从第二行开始
        for (int i = start; i <= sheet.getLastRowNum(); i++) {
            XSSFRow row = sheet.getRow(i);
            if (row == null) {
                break;
            }
            // 生成实例并通过反射调用setter方法
            T target = clazz.newInstance();
            for (int j = 0; j < fieldNames.length; j++) {
                String fieldName = fieldNames[j];
                if (fieldName == null || UID.equals(fieldName)) {
                    continue; // 过滤serialVersionUID属性
                }
                // 获取excel单元格的内容
                XSSFCell cell = row.getCell(j);
                if (cell == null) {
                    continue;
                }
                try {
                    String content = getCellContent(cell);
                    invokeSetter(target, fieldName, content);
                }catch (Exception e) {
                    continue;
                }

            }
            dataModels.add(target);
        }
        return dataModels;
    }

    /**
     * 根据cell类型读取cell数据
     * @param xssfCell
     * @return
     */
    public static String getCellContent(XSSFCell xssfCell) {
        if (null == xssfCell) {
            return "";
        }
        CellType cellType = xssfCell.getCellTypeEnum();
        switch (cellType) {
            case NUMERIC:
                Double num = xssfCell.getNumericCellValue();
                if (num - num.intValue() == 0) {
                    return String.valueOf(num.intValue()).trim();
                }else {
                    return String.valueOf(xssfCell.getNumericCellValue()).trim();
                }
            case FORMULA:
                try {
                    return  xssfCell.getStringCellValue().trim();
                }catch (Exception e) {
                    return String.valueOf(xssfCell.getNumericCellValue()).trim();
                }
            case STRING:
                return xssfCell.getStringCellValue().trim();
            case BLANK:
                return xssfCell.getStringCellValue().trim();
            case BOOLEAN:
                return String.valueOf(xssfCell.getBooleanCellValue()).trim();
            case _NONE:
                return xssfCell.toString().trim();
            case ERROR:
                return "";
            default :
                return "";
        }
    }

    /**
     * 反射调用指定对象属性的setter方法
     *
     * @param <T>
     *            泛型
     * @param target
     *            指定对象
     * @param fieldName
     *            属性名
     * @param args
     *            参数列表
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     *
     */
    public static <T> void invokeSetter(T target, String fieldName, Object args)
            throws NoSuchFieldException, SecurityException,
            NoSuchMethodException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        // 如果属性名为xxx，则方法名为setXxx
        String methodName = "set" + StringUtil.firstCharUpperCase(fieldName);
        Class<?> clazz = target.getClass();
        Field field = clazz.getDeclaredField(fieldName);
        Method method = clazz.getMethod(methodName, field.getType());
        method.invoke(target, args);
    }
}
