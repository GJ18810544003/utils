package com.example.utils.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CellStyleUtil {

    public static XSSFCellStyle getTitleStyle(XSSFWorkbook workbook) {
        XSSFCellStyle cellStyle = getCellStyle(workbook);

        //设置字体
        XSSFFont titleFont = workbook.createFont();
        titleFont.setFontName("微软雅黑");
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 10);
        cellStyle.setFont(titleFont);

        //背景颜色设置
        cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        return cellStyle;
    }

    /**
     * 获取单元格普通格式
     */
    public static XSSFCellStyle getCellStyle(XSSFWorkbook workbook) {
        XSSFCellStyle cellStyle = workbook.createCellStyle();

        //边框设置
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);

        //设置居中
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        //设置字体
        XSSFFont font = workbook.createFont();
        font.setFontName("微软雅黑");
        font.setFontHeightInPoints((short) 10);
        cellStyle.setFont(font);

        return cellStyle;

    }
}
