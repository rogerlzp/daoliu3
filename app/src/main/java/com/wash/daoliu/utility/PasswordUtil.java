package com.wash.daoliu.utility;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by rogerlzp on 15/12/28.
 */
public class PasswordUtil {

    public static String getMD5(TreeMap map) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(sortMap(map).getBytes("UTF-8"));
            byte[] encryption = md5.digest();

            StringBuffer strBuf = new StringBuffer();
            for (int i = 0; i < encryption.length; i++) {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                    strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                } else {
                    strBuf.append(Integer.toHexString(0xff & encryption[i]));
                }
            }

            return strBuf.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }


    private final static String HEX = "0123456789ABCDEF";


    public static String private_key = "private_key";
    public static String private_value = "ltn$%^qpdhTH18";


    public static String sortMap(TreeMap map) {
        StringBuffer sb = new StringBuffer();
        Map<String, String> resultMap = sortMapByKey(map);    //按Key进行排序
        for (Map.Entry<String, String> entry : resultMap.entrySet()) {
            sb.append(entry.getKey());
            sb.append(entry.getValue());
        }
        return sb.toString();
    }


    public static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, String> sortMap = new TreeMap<String, String>(
                new MapKeyComparator() {
                });
        sortMap.putAll(map);
        return sortMap;
    }

    static class MapKeyComparator implements Comparator<String> {
        @Override
        public int compare(String str1, String str2) {

            return str1.compareTo(str2);
        }
    }


}
