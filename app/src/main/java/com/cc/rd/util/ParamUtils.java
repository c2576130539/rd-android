package com.cc.rd.util;

/**
 * @program: ParamUtils
 * @description: 校验数据
 * @author: cchen
 * @create: 2019-05-06 21:44
 */

public class ParamUtils {

    /**
     * 字符串判不为空
     * @param string
     * @return
     */
    public static Boolean isNotNull(String string) {
        return string != null && string.length() > 0;
    }

    /**
     * 字符串判为空
     * @param string
     * @return
     */
    public static boolean isEmpty(CharSequence string) {
        return string == null || string.length() == 0;
    }
}
