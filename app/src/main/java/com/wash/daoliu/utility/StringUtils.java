package com.wash.daoliu.utility;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: bobo
 * Date: 13-5-13
 * Time: 下午1:38
 */
public class StringUtils {
    //斜杠
    public static final String SPLIT_XG = "/";
    //反斜杠
    public static final String SPLIT_FXG = "\\";
    //分号
    public static final String SPLIT_FH = ";";
    //冒号
    public static final String SPLIT_MH = ":";
    //横杠
    public static final String SPLIT_HG = " -- ";
    //竖杠
    public static final String SPLIT_SG = "\\|";
    //换行符
    public static final String SPLIT_HHF = "\n";
    //逗号
    public static final String SPLIT_COMMA = ",";

    /**
     * 分列字符串
     *
     * @param s
     * @return
     */
    public static String[] split(String s) {
        return s.split(SPLIT_XG);
    }

    /**
     * 分列字符串
     *
     * @param s
     * @return
     */
    public static String[] split(String s, String regular) {
        return s.split(regular);
    }

    /**
     * 字符串为空
     *
     * @param s
     * @return
     */
    public static boolean isEmpty(String s) {
        return !isNotEmpty(s);
    }

    /**
     * 字符串不为空
     *
     * @param s
     * @return
     */
    public static boolean isNotEmpty(String s) {
        return s != null && s.length() > 0;
    }

    /**
     * 判断是否为null或空值
     *
     * @param str String
     * @return true or false
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }


    /**
     * 去除字符中间空格
     *
     * @param s
     * @return
     */
    public static String repalceEmptyStr(String s) {
        return s.replaceAll(" ", "");
    }

    /**
     * 字符串转成List集合
     *
     * @param texts
     * @return
     */
    public static List<String> stringToList(String[] texts) {
        List<String> ss = new ArrayList<String>();
        for (int i = 0; i < texts.length; i++) {
            ss.add(texts[i]);
        }
        return ss;
    }

    /**
     * 在原来的数组中添加第一条字符串
     *
     * @param olds
     * @param s
     * @return
     */
    public static String[] addFirstValue(String[] olds, String s) {
        String[] news = new String[olds.length + 1];
        news[0] = s;
        for (int i = 0; i < olds.length; i++) {
            news[i + 1] = olds[i];
        }
        return news;
    }

    /**
     * 使double不用科学计数法显示
     *
     * @param d
     * @return
     */
    public static String double2String(Double d) {
        if (null == d)
            return "";
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        return decimalFormat.format(d);
    }

    /**
     * 获取UUID值
     *
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    public static boolean isExistForList(List<String> stringList, String str) {
        for (String s : stringList) {
            if (s.equals(str)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unused")
    public static String string2Decimal(String str) {
        String src = str.split("\\.")[0];
        StringBuilder sb = new StringBuilder();
        char[] strChar = src.toCharArray();
        for (int i = strChar.length - 1; i >= 0; i--) {
            sb.append(strChar[i]);
            if ((strChar.length - i) % 3 == 0) {
                sb.append(",");
            }
        }
        return sb.reverse().toString();
    }

    /**
     * 逗号分隔显示数字
     *
     * @param str
     * @return
     */
    public static String split3Number(String str) {
        if (isEmpty(str))
            return "";
        String[] ss = str.split("\\.");
        str = ss[0];
        int i = ss[0].length();
        while (i > 3) {
            str = str.substring(0, i - 3) + "," + str.substring(i - 3);
            i -= 3;
        }
        if (ss.length > 1) {
            str = str + "." + ss[1];
        }
        return str;
    }

    public static String double2Decimal(BigDecimal decimal) {
        NumberFormat nf = new DecimalFormat(",##0.00");
        return nf.format(decimal);
    }
}
