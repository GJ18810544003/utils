package com.example.utils.excel;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.*;

/**
 * @Description:
 * @Author: guojun
 * @Date: 2019/1/3
 */
public class Test {

    public static void main(String[] args) throws Exception {
        File file = new File("D://2017.xlsx");

        XSSFWorkbook workbook = new XSSFWorkbook(file);
        String[] fieldNames = new String[]{"fengTou", "qiye"};
        List<ReadVo> readVoList = ExcelUtil.readExcel(workbook, ReadVo.class, fieldNames, 0, true);
        Map<String, Integer> indexMap = new TreeMap<>();
        Map<String, List<String>> dataMap = new HashMap<>();
        List<String> fengtouList = new ArrayList<>();
        int startIndex = 0;
        for (ReadVo readVo : readVoList) {

            if (!indexMap.containsKey(readVo.getFengTou())) {
                indexMap.put(readVo.getFengTou(), startIndex);
                startIndex++;
                fengtouList.add(readVo.getFengTou());
            }


            if (dataMap.containsKey(readVo.getFengTou())) {
                List<String> tmpList = dataMap.get(readVo.getFengTou());
                tmpList.add(readVo.getQiye());
                dataMap.put(readVo.getFengTou(), tmpList);
            }else {
                List<String> tmpList = new LinkedList<>();
                tmpList.add(readVo.getQiye());
                dataMap.put(readVo.getFengTou(), tmpList);
            }

        }

        int[][] result = new int[fengtouList.size()][fengtouList.size()];
        for (int i = 0; i < fengtouList.size(); i++) {
            for (int j = 0; j < fengtouList.size(); j++) {
                String fentouI = fengtouList.get(i);
                String fentouJ = fengtouList.get(j);
                int num = 0;
                if (fentouI.equals(fentouJ)) {
                    result[i][j] = 0;
                }else {
                    List<String> qiyeIList = dataMap.get(fentouI);
                    List<String> qiyeJList = dataMap.get(fentouJ);
                    for (String qiye : qiyeIList) {
                        if (qiyeJList.contains(qiye)) {
                            qiyeJList.remove(qiye);
                            num++;
                        }
                    }
                    result[i][j] = num;

                }
            }
        }

/*        StringBuilder sb = new StringBuilder();
        sb.append("           ");
        for (String fengtou : fengtouList) {
            sb.append(" ").append(fengtou);
        }
        sb.append("\r\n");

        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                if (j == 0) {
                    sb.append(fengtouList.get(i));
                    sb.append(" ").append(result[i][j]);
                }else {
                    if (i != 0) {
                        for (int k = 0; k < fengtouList.get(j - 1).length(); k++) {
                            sb.append(" ");
                        }
                    }

                    sb.append(result[i][j]);
                }

            }
            sb.append("\r\n");
        }

        File file1 = new File("wahaha.txt");
        if (!file1.exists()) {
            file1.createNewFile();
        }

        FileWriter fileWriter = new FileWriter(file1.getName(), true);
        fileWriter.write(sb.toString());*/

        ExcelUtil.writeExcel(result, fengtouList, "1");

       /* FileOutputStream fileOutputStream = new FileOutputStream("D://新建文件夹/2016.xlsx");

        workbook1.write(fileOutputStream);*/



    }
}
