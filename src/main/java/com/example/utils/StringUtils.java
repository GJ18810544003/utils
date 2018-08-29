package com.example.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: guojun
 * @Date: 2018/6/6
 */
public class StringUtils {

    /**
     * 版本号比较，如1.0.20与2.1.1比较大小
     * 分隔符中的字符是否为合法数字需要自己处理
     * @param version1
     * @param version2
     * @return
     */
    public int versionCompare(String version1, String version2) {
        System.out.println();
        String[] v1Array = version1.split("\\.");
        String[] v2Array = version2.split("\\.");
        int i = -1;
        for (String v1 : v1Array) {
            i++;
            if (i < v2Array.length) {
                String v2 = v2Array[i];
                if (!v1.equals(v2)) {
                    return Integer.parseInt(v1) - Integer.parseInt(v2);
                }
            }else {// i超出v2Array的范围并且没有返回，说明version1比version2长且version2是version1的子串,则version1比version2大
                return 1;
            }
        }
        // 遍历v1Array结束后，若没有返回且i < v2Array.length，则说明version1比version2短且version1是version2的子串，则version1比version2小
        if (i < v2Array.length) {
            return -1;
        }

        return 0;
    }

}
