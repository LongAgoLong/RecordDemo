package com.example.leo.recorddemo.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.example.leo.recorddemo.R;

import java.io.File;
import java.util.ArrayList;

import io.reactivex.Observable;

/**
 * Create by LEO
 * on 2018/7/5
 * at 14:06
 * in MoeLove Company
 */

public final class VideoRecordUtil {
    public static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 99;

    /*
     * 开始录制
     * */
    @Deprecated //小米大哥有问题
    public static String startRecord(Context context) {
        if (AppStackManager.getInstance().checkPermissions(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO)) {
            if (SDcardUtil.isVideo_PATH(context)) {
                try {
                    String videoFilePath = context.getString(R.string.text_video_path, DateUtil.format(String.valueOf(System.currentTimeMillis() / 1000), DateUtil.DATA_YYYY_MM_DD_HH_MM_SS2));
                    File mediaFile = new File(SDcardUtil.VIDEO_PATH, videoFilePath);
                    String nyatoRecordPath = mediaFile.getPath();
                    //判断当前手机版本
                    Uri fileUri;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        fileUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", mediaFile);
                    } else {
                        fileUri = Uri.fromFile(mediaFile);
                    }
                    Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);  // set the image file name
//                    intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 500);
//                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); // set the video image quality to high
                    // start the Video Capture Intent
                    ((Activity) context).startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
                    return nyatoRecordPath;
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtil.show(context, R.string.text_no_camera_app);
                }
            } else {
                ToastUtil.show(context, R.string.no_SD_card);
            }
        } else {
            ToastUtil.show(context, context.getResources().getString(R.string.text_permission_youRefuse, "相关"));
        }
        return null;
    }

    /*
     * onActivityResult 需要做判断，miui部分机型拿不到uri
     * */
    public static boolean startRecordNoParams(Context context) {
        if (AppStackManager.getInstance().checkPermissions(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO)) {
            if (SDcardUtil.isVideo_PATH(context)) {
                try {
                    Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    ((Activity) context).startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtil.show(context, R.string.text_no_camera_app);
                }
            } else {
                ToastUtil.show(context, R.string.no_SD_card);
            }
        } else {
            ToastUtil.show(context, context.getResources().getString(R.string.text_permission_youRefuse, "相关"));
        }
        return false;
    }


    /**
     * 创建视频缩略图
     *
     * @param file         视频文件
     * @param shouleVerify 是否需要校验
     *                     false-不需要检测本地是否已存在截图直接创建
     *                     true-需要检测本地是否已存在截图，存在则不需要创建
     */
    public static Observable<ArrayList<String>> rxCreateVideoThumbFile(File file, boolean shouleVerify) {
        return Observable.create(e -> {
            ArrayList<String> videoThumbList = new ArrayList<>();
            String videoPath = file.getPath();
            String videoFileName = videoPath.substring(videoPath.lastIndexOf("/") + 1, videoPath.lastIndexOf("."));
            String videoName01 = videoFileName + "_thumb01";
            String videoName02 = videoFileName + "_thumb02";
            String thumbImgName01 = Md5Util.MD5Encode(videoName01) + ".jpg";
            String thumbImgName02 = Md5Util.MD5Encode(videoName02) + ".jpg";
            //第一张缩略图
            if (!shouleVerify || !new File(SDcardUtil.VIDEO_PATH, thumbImgName01).exists()) {
                Bitmap frameAtTimeBitmap = BitmapUtil.getVideoThumbnail(videoPath, -1);
                if (null == frameAtTimeBitmap) {
                    e.onError(new Throwable("视频文件已损坏"));
                    return;
                }
                byte[] bytes = BitmapUtil.toByte(frameAtTimeBitmap);
                SDcardUtil.saveToSDCard(thumbImgName01, SDcardUtil.VIDEO_PATH, bytes, true);
                if (!frameAtTimeBitmap.isRecycled()) {
                    frameAtTimeBitmap.recycle();
                }
            }
            videoThumbList.add(SDcardUtil.VIDEO_PATH + thumbImgName01);
            //第二张缩略图
            if (!shouleVerify || !new File(SDcardUtil.VIDEO_PATH, thumbImgName02).exists()) {
                MediaPlayer player = new MediaPlayer();
                player.setDataSource(videoPath);
                player.prepare();
                double duration = player.getDuration();
                player.release();//记得释放资源
                int time;
                if (duration > 1000) {
                    time = 1000000;
                } else {
                    time = -1;
                }
                Bitmap frameAtTimeBitmap = BitmapUtil.getVideoThumbnail(videoPath, time);//1秒
                if (null == frameAtTimeBitmap) {
                    e.onError(new Throwable("视频文件已损坏"));
                    return;
                }
                byte[] bytes = BitmapUtil.toByte(frameAtTimeBitmap);
                SDcardUtil.saveToSDCard(thumbImgName02, SDcardUtil.VIDEO_PATH, bytes, true);
                if (!frameAtTimeBitmap.isRecycled()) {
                    frameAtTimeBitmap.recycle();
                }
            }
            videoThumbList.add(SDcardUtil.VIDEO_PATH + thumbImgName02);
            e.onNext(videoThumbList);
            e.onComplete();
        });
    }
}
