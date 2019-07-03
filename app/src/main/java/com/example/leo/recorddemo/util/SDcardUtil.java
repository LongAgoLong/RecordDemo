package com.example.leo.recorddemo.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * @author Ari
 */
public class SDcardUtil {
    //本地保存图片目录
    public static final String DCIM_PATH = Environment.getExternalStorageDirectory() + File.separator + "Nyato_DCIM" + File.separator;
    public static final String SHAREPIC_PATH = Environment.getExternalStorageDirectory() + File.separator + "Nyato_DCIM" + File.separator + ".Nyato_SHAREPIC" + File.separator;
    public static final String VIDEO_PATH = Environment.getExternalStorageDirectory() + File.separator + "Nyato_DCIM" + File.separator + "Nyato_VIDEO" + File.separator;

    /**
     * SD卡是否存在
     */
    public static boolean sdcardExists() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * SD卡根路径
     */
    public static File getRoot() {
        return Environment.getExternalStorageDirectory();
    }


    /**
     * SD卡剩余存储空间
     */
    public static long getFreeSpace() {
        File root = getRoot();
        StatFs stat = new StatFs(root.getPath());
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks;
    }

    public static boolean isDCIMFolderExist(Context context) {
        if (!sdcardExists()) {
            return false;
        } else {
            File dirFirstFile = new File(DCIM_PATH);
            if (dirFirstFile.exists() && !dirFirstFile.isDirectory()) {
                dirFirstFile.delete();
            }
            if (!dirFirstFile.exists()) {
                dirFirstFile.mkdir();
            }
            return true;
        }
    }

    public static boolean isSHAREPIC_PATH(Context context) {
        if (!sdcardExists()) {
            return false;
        } else {
            File dirFirstFile = new File(DCIM_PATH);
            if (dirFirstFile.exists() && !dirFirstFile.isDirectory()) {
                dirFirstFile.delete();
            }
            if (!dirFirstFile.exists()) {
                dirFirstFile.mkdir();
            }

            File dirSecondFile = new File(SHAREPIC_PATH);
            if (dirSecondFile.exists() && !dirSecondFile.isDirectory()) {
                dirSecondFile.delete();
            }
            if (!dirSecondFile.exists()) {
                dirSecondFile.mkdir();
            }
            return true;
        }
    }

    public static boolean isVideo_PATH(Context context) {
        if (!sdcardExists()) {
            return false;
        } else {
            File dirFirstFile = new File(DCIM_PATH);
            if (dirFirstFile.exists() && !dirFirstFile.isDirectory()) {
                dirFirstFile.delete();
            }
            if (!dirFirstFile.exists()) {
                dirFirstFile.mkdir();
            }

            File dirSecondFile = new File(VIDEO_PATH);
            if (dirSecondFile.exists() && !dirSecondFile.isDirectory()) {
                dirSecondFile.delete();
            }
            if (!dirSecondFile.exists()) {
                dirSecondFile.mkdir();
            }
            return true;
        }
    }

    /**
     * 保存到SD卡
     *
     * @param filename
     * @param filePath
     * @param filecontent
     * @param isPICSendBroadcast
     * @return
     */
    public static boolean saveToSDCard(Context context, String filename, String filePath, String filecontent, boolean isPICSendBroadcast) {
        return saveToSDCard(context, filename, filePath, filecontent.getBytes(), isPICSendBroadcast);
    }

    /**
     * 保存到SD卡
     *
     * @param filename
     * @param filePath
     * @param filecontent
     * @param isPICSendBroadcast
     * @return
     */
    public static boolean saveToSDCard(Context context, String filename, String filePath, byte[] filecontent, boolean isPICSendBroadcast) {
        try {
            final File file = new File(filePath, filename);
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream outStream = new FileOutputStream(file);
            outStream.write(filecontent);
            outStream.close();
            if (isPICSendBroadcast) {
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);//发送更新图片信息广播
                Uri uri = Uri.fromFile(file);
                intent.setData(uri);
                context.getApplicationContext().sendBroadcast(intent);
            }
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 保存到SD卡
     *
     * @param filename
     * @param filePath
     * @param filecontent
     * @param isPICSendBroadcast
     * @return
     */
    public static boolean saveToSDCard(Context context, String filename, String filePath, File filecontent, boolean isPICSendBroadcast) {
        try {
            int bytesum = 0;
            int byteread;
            final File file = new File(filePath, filename);
            if (file.exists())
                file.delete();
            InputStream inStream = new FileInputStream(filecontent); //读入原文件
            FileOutputStream outStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                outStream.write(buffer, 0, byteread);
            }
            inStream.close();
            outStream.close();
            if (isPICSendBroadcast) {
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);//发送更新图片信息广播
                Uri uri = Uri.fromFile(file);
                intent.setData(uri);
                context.getApplicationContext().sendBroadcast(intent);
            }
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
     * 保存电子票数据
     * */
    public static synchronized void saveETickets(Context context, String s) {
        if (isSHAREPIC_PATH(context)) {
            File file = new File(SHAREPIC_PATH + "EleTickets.txt");
            byte[] encode = Base64.encode(s.getBytes(), Base64.NO_WRAP);
            String s1 = new String(encode);
            //Write the file to disk
            OutputStreamWriter writer = null;
            try {
                OutputStream out = new FileOutputStream(file);
                writer = new OutputStreamWriter(out, "UTF-8");
                writer.write(s1);
                writer.flush();
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
//            try {
//                Field field;
//                field = ContextWrapper.class.getDeclaredField("mBase");// 获取ContextWrapper对象中的mBase变量。该变量保存了ContextImpl对象
//                field.setAccessible(true);
//                Object obj = field.get(context);// 获取mBase变量
//                field = obj.getClass().getDeclaredField("mPreferencesDir");// 获取ContextImpl。mPreferencesDir变量，该变量保存了数据文件的保存路径
//                field.setAccessible(true);
//                File file = new File(SHAREPIC_PATH);// 创建自定义路径
//                field.set(obj, file);// 修改mPreferencesDir变量的值
//                SharedPreferences mySharedPreferences = context.getSharedPreferences("ETickets", Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = mySharedPreferences.edit();
//                if (TextUtils.isEmpty(s)) {
//                    editor.putString("eTickets", "");
//                } else {
//                    byte[] encode = Base64Util.encode(s.getBytes());
//                    editor.putString("eTickets", new String(encode));
//                }
//                editor.commit();
//            } catch (NoSuchFieldException e) {
//                e.printStackTrace();
//            } catch (IllegalArgumentException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
        }
    }

    /*
     * 获取电子票数据
     * */
    public static String getETickets(Context context) {
        StringBuilder jsonString = new StringBuilder("");
        if (isSHAREPIC_PATH(context)) {
            File file = new File(SHAREPIC_PATH + "EleTickets.txt");
            if (file.exists()) {
                try {
                    InputStreamReader input = new InputStreamReader(new FileInputStream(file), "UTF-8");
                    BufferedReader reader = new BufferedReader(input);
                    String empString = null;
                    while ((empString = reader.readLine()) != null) {
                        jsonString.append(empString);
                    }
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
//            try {
//                Field field;
//                field = ContextWrapper.class.getDeclaredField("mBase");//获取ContextWrapper对象中的mBase变量。该变量保存了ContextImpl对象
//                field.setAccessible(true);
//                Object obj = field.get(context);//获取mBase变量
//                field = obj.getClass().getDeclaredField("mPreferencesDir");//获取ContextImpl。mPreferencesDir变量，该变量保存了数据文件的保存路径
//                field.setAccessible(true);
//                File file = new File(SHAREPIC_PATH);//创建自定义路径
//                field.set(obj, file);//修改mPreferencesDir变量的值
//                SharedPreferences mySharedPreferences = context.getSharedPreferences("ETickets", Context.MODE_PRIVATE);
//                String eTickets = mySharedPreferences.getString("eTickets", "");
//                if (TextUtils.isEmpty(eTickets)) {
//                    return "";
//                } else {
//                    byte[] decode = Base64Util.decode(eTickets);
//                    return new String(decode);
//                }
//            } catch (NoSuchFieldException e) {
//                e.printStackTrace();
//            } catch (IllegalArgumentException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
        String s = jsonString.toString();
        byte[] decode = Base64.decode(s, Base64.NO_WRAP);
        if (null == decode || decode.length == 0) {
            return "";
        } else {
            return new String(decode);
        }
    }
}
