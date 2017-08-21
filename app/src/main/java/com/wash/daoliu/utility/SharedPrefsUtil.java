package com.wash.daoliu.utility;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Base64;

import java.io.*;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: bobo
 * Date: 13-1-29
 * Time: 下午2:38
 * To change this template use File | Settings | File Templates.
 */
public class SharedPrefsUtil {

    static SharedPreferences sp = null;

    public static SharedPreferences getSp(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences(Constant.Share.SHARE_NAME, 0);
        }
        return sp;
    }

    /**
     * 判断是否存在key
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean isExist(Context context, String key) {
        return getSp(context).contains(key);
    }

    /**
     * 查询缓存值
     *
     * @param context
     * @param key
     * @param id
     */
    public static void putInt(Context context, String key, int id) {
        SharedPreferences.Editor spEdit = getSp(context).edit();
        spEdit.putInt(key, id);
        spEdit.commit();
    }

    /**
     * 查询缓存值
     *
     * @param context
     * @param key
     * @return
     */
    public static int getInt(Context context, String key) {
        return getSp(context).getInt(key, -1);
    }

    /**
     * 查询缓存值
     *
     * @param context
     * @param key
     * @return
     */
    public static int getInt(Context context, String key, int defaultValue) {
        return getSp(context).getInt(key, defaultValue);
    }

    /**
     * 查询缓存值
     *
     * @param context
     * @param key
     * @param s
     */
    public static void putString(Context context, String key, String s) {
        SharedPreferences.Editor spEdit = getSp(context).edit();
        spEdit.putString(key, s);
        spEdit.commit();
    }

    public static String getString(Context context, String key) {
        return getSp(context).getString(key, null);
    }

    public static float getFloat(Context context, String key) {
        return getSp(context).getFloat(key, -1F);
    }

    public static void putLong(Context context, String key, long l) {
        SharedPreferences.Editor spEdit = getSp(context).edit();
        spEdit.putLong(key, l);
        spEdit.commit();
    }

    public static long getLong(Context context, String key) {
        return getSp(context).getLong(key, -1L);
    }

    public static long getLong(Context context, String key, long defaultValue) {
        return getSp(context).getLong(key, defaultValue);
    }

    /**
     * 查询缓存值
     *
     * @param context
     * @param key
     * @param b
     */
    public static void putBoolean(Context context, String key, boolean b) {
        SharedPreferences.Editor spEdit = getSp(context).edit();
        spEdit.putBoolean(key, b);
        spEdit.commit();
    }

    /**
     * 查询缓存值
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean getBoolean(Context context, String key) {
        return getSp(context).getBoolean(key, false);
    }

    /**
     * 查询缓存值
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        return getSp(context).getBoolean(key, defaultValue);
    }

    /**
     * 缓存Set集合值
     *
     * @param context
     * @param key
     * @param ids
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void putSet(Context context, String key, Set<String> ids) {
        SharedPreferences.Editor spEdit = getSp(context).edit();
        spEdit.putStringSet(key, ids);
        spEdit.commit();
    }

    /**
     * 获取缓存Set集合值
     *
     * @param context
     * @param key
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static Set<String> getSet(Context context, String key) {
        return getSp(context).getStringSet(key, null);
    }

    public static void remove(Context context, String key) {
        SharedPreferences.Editor spEdit = getSp(context).edit();
        spEdit.remove(key);
        spEdit.commit();
    }

    public static void putObjectStream(Context context, String key, Object obj) {
        ByteArrayOutputStream baos;
        try {
            baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            //Save user preferences. use Editor object to make changes.
            SharedPreferences.Editor spEdit = getSp(context).edit();
            String strObj = new String(Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT));
            spEdit.putString(key, strObj);
            spEdit.commit();
        } catch (IOException e) {
           // Log.e("IOException", e.toString());
        }
    }

    /**
     * 从SharedPreferences文件流中获取对象
     *
     * @param context
     * @param key
     * @return
     */
    public static Object getObjectStream(Context context, String key) {
        try {
            String paramBase64 = getSp(context).getString(key, null);
            byte[] base64Bytes = Base64.decode(paramBase64.getBytes(), Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
           // Log.d("Exception", "" + e.toString());
        }
        return null;
    }

    /**
     * 清空用户数据
     *
     * @param context
     */
    public static void clearUserInfo(Context context) {
        remove(context, Constant.Share.SHARE_CACHE_USERINFO);
        // clear cache username,password
        remove(context, Constant.Share.SHARE_LOGIN_PASSWORD);
        remove(context, Constant.Share.SHARE_USERNAME);
        remove(context, Constant.Share.SHARE_SESSIONID);
        remove(context, Constant.Share.SHARE_TOKENKEY);
        remove(context, Constant.Share.SHARE_USERCODE);
        remove(context, Constant.Share.SHARE_USER_PORTRAITS);
    }

    /**
     * 获取市场渠道号
     * @param context
     */
    public static void putAssetsConf(Context context) {
        try {
            InputStream in = context.getResources().getAssets().open("conf.db");
            byte[] b = new byte[in.available()];
            int len = in.read(b);
            if (len > 0 && b.length > 0) {
                String text = new String(b);
                String[] lines = text.split("\n");
                if (lines.length > 0) {
                    String[] arr = lines[0].split("=");
                    if (arr.length > 1) {
                        SharedPrefsUtil.putInt(context, Constant.Share.SHARE_CHANNEL_NO, Integer.valueOf(arr[0].trim()));
                        SharedPrefsUtil.putString(context, Constant.Share.SHARE_CHANNEL_NAME, arr[1].trim());
                    }
                }
            }
        } catch (IOException e) {
            //e.printStackTrace();
        }

    }
}
