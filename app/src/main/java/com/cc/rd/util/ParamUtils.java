package com.cc.rd.util;

import com.cc.rd.enums.Constant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static boolean isRightTel(String telphone) {
        // 编译正则表达式
        Pattern pattern = Pattern.compile(Constant.DEFAULT_TELPHONE);
        Matcher matcher = pattern.matcher(telphone);
        // 字符串是否与正则表达式相匹配
        if (!matcher.matches()) {
            return false;
        }
        return true;
    }
}
