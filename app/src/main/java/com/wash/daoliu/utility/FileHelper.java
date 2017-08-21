package com.wash.daoliu.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;
import android.os.Environment;


/**
 * @author ding
 */

public class FileHelper {

    public static File getFileDirectory(Context context, String dirName) {
        File dirFile = null;
        if (ExistSDCard()) {
            dirFile = new File(Environment.getExternalStorageDirectory(), "lingtouniao/" + dirName);
        } else {
            String installPath = context.getFilesDir().getAbsolutePath() + File.separator;
            dirFile = new File(installPath + dirName);
        }
        if (!dirFile.exists()) {
            if (!dirFile.mkdirs()) {
                String installPath = context.getFilesDir().getAbsolutePath() + File.separator;
                dirFile = new File(installPath + dirName);
                dirFile.mkdirs();
            }
        }
        return dirFile;
    }

    public static boolean isFileExist(Context context, String dirName, String FileName) {
        File file = new File(getFileDirectory(context, dirName), FileName);
        return file.exists();
    }

    public static File getFile(Context context, String dirName, String FileName) {
        File file = new File(getFileDirectory(context, dirName), FileName);
        return file;
    }

    public static File getFile(Context context, String dirName, String FileName, boolean isDelete) {
        File file = new File(getFileDirectory(context, dirName), FileName);
        if (file.exists()) {
            file.delete();
        }
        return file;
    }

    private static boolean ExistSDCard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else
            return false;
    }

    public static void writeObjectToFile(final Context context, final Object object, final String dirName,
                                         final String fileName, final boolean delete) {
        File file = new File(dirName, fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (delete) {
            file.delete();
        }
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            oos.flush();
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Object readObjectFromFile(Context context, String dirName, String fileName) {
        File file = new File(dirName, fileName);
        if (!file.exists()) {
            return null;
        }
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);
            Object object = ois.readObject();
            return object;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static Object readObjectFromFile(Context context, InputStream inputStream) {
        // TODO Auto-generated method stub
        if (inputStream == null) {
            return null;
        }
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(inputStream);
            Object object = ois.readObject();
            return object;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static String readSDFile(String filepath) {

        StringBuffer sb = new StringBuffer();

        File file = new File(filepath);
        try {
            FileInputStream fis = new FileInputStream(file);
            int c;
            while ((c = fis.read()) != -1) {
                sb.append((char) c);
            }

            fis.close();

        } catch (FileNotFoundException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

        return sb.toString();

    }

    public static String getFromAssets(Context context, String fileName) {
        String Result = "";
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result;
    }
}
