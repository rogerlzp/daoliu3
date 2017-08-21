package com.wash.daoliu.utility;

import android.util.Log;

/**
 * Created by jiajia on 2016/1/18.
 */
public class LogUtils {
    private static String LOG_NAME = LogUtils.class.getSimpleName();
    public static boolean isDebug = false;
    public static void e(String tag){
        if(isDebug){
            Log.e(LOG_NAME,tag);
        }
    }
    public static void i(String tag){
        if(isDebug){
            Log.i(LOG_NAME,tag);
        }
    }
    public static void w(String tag){
        if(isDebug){
            Log.w(LOG_NAME,tag);
        }
    }
    public static void d(String tag){
        if(isDebug){
            Log.d(LOG_NAME,tag);
        }
    }
    public static void v(String tag){
        if(isDebug){
            Log.v(LOG_NAME,tag);
        }
    }
}
