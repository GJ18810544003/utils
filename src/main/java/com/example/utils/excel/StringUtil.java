package com.example.utils.excel;

import com.douyu.wsd.parrot.common.constant.BizCode;
import com.douyu.wsd.parrot.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description:
 * @Author: guojun
 * @Date: 2018/8/8
 */
@Slf4j
public class StringUtil {

 /*   public static String printRoomInfoList(List<RoomInfo> roomInfos) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        roomInfos.forEach(roomInfo -> {
            stringBuilder.append(roomInfo.getRoomId()).append(",");
        });
        stringBuilder.append("}");
        return stringBuilder.toString();
    }*/

    /**
     * String转Long
     */
    public static Long stringToLong(String str) {
        try {
            return Long.parseLong(str);
        }catch (NumberFormatException e) {
            log.error("字符串[{}]转Long类型异常：{}", str, e.getMessage());
            throw new BizException(BizCode.INNER_DATA_INVALID, String.format("字符串[%s]转Long类型异常：[%s]", str, e.getMessage()));
        }
    }

    /**
     * String转Integer
     */
    public static Integer stringToInteger(String str) {
        try {
            return Integer.parseInt(str);
        }catch (NumberFormatException e) {
            log.error("字符串[{}]转Integer类型异常：{}", str, e.getMessage());
            throw new BizException(BizCode.INNER_DATA_INVALID, String.format("字符串[%s]转Integer类型异常：[%s]", str, e.getMessage()));
        }
    }

    /**
     * 暂不知道有没有用
     * 将指定对象转换成字符串
     *
     * @param obj 指定对象
     * @return 转换后的字符串
     */
    public static String toString(Object obj) {
        StringBuffer buffer = new StringBuffer();
        if (obj != null) {
            buffer.append(obj);
        }
        return buffer.toString();
    }

    /**
     * 将指定字符串首字母转换成大写字母
     *
     * @param str 指定字符串
     * @return 返回首字母大写的字符串
     */
    public static String firstCharUpperCase(String str) {
        StringBuffer buffer = new StringBuffer(str);
        if (buffer.length() > 0) {
            char c = buffer.charAt(0);
            buffer.setCharAt(0, Character.toUpperCase(c));
        }
        return buffer.toString();
    }
}
