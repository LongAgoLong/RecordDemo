package com.example.leo.recorddemo.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;

import com.example.leo.recorddemo.App;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Ari
 */
public class SDcardUtil {
    //本地保存图片目录
    public static final String DCIM_PATH = Environment.getExternalStorageDirectory() + File.separator + "LEO_DCIM" + File.separator;
    public static final String SHAREPIC_PATH = Environment.getExternalStorageDirectory() + File.separator + "LEO_DCIM" + File.separator + ".LEO_SHAREPIC" + File.separator;
    public static final String VIDEO_PATH = Environment.getExternalStorageDirectory() + File.separator + "LEO_DCIM" + File.separator + "LEO_VIDEO" + File.separator;

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
     * @param filename           文件名称(带扩展名)
     * @param filePath           存储目录路径
     * @param filecontent        存储内容
     * @param isPICSendBroadcast 是否需要发送文件更新广播
     */
    public static boolean saveToSDCard(String filename, String filePath, String filecontent, boolean isPICSendBroadcast) {
        return saveToSDCard(filename, filePath, filecontent.getBytes(), isPICSendBroadcast);
    }

    /**
     * 保存到SD卡
     *
     * @param filename           文件名称(带扩展名)
     * @param filePath           存储目录路径
     * @param filecontent        存储内容
     * @param isPICSendBroadcast 是否需要发送文件更新广播
     */
    public static boolean saveToSDCard(String filename, String filePath, byte[] filecontent, boolean isPICSendBroadcast) {
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
                App.getContext().sendBroadcast(intent);
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
     * @param filename           文件名称(带扩展名)
     * @param filePath           存储目录路径
     * @param filecontent        存储内容
     * @param isPICSendBroadcast 是否需要发送文件更新广播
     */
    public static boolean saveToSDCard(String filename, String filePath, File filecontent, boolean isPICSendBroadcast) {
        try {
            int bytesum = 0;
            int byteread;
            final File file = new File(filePath, filename);
            if (file.exists()) {
                file.delete();
            }
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
                App.getContext().sendBroadcast(intent);
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
}
