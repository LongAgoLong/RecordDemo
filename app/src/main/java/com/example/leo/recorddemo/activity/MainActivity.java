package com.example.leo.recorddemo.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.leo.recorddemo.R;
import com.example.leo.recorddemo.util.AppStackManager;
import com.example.leo.recorddemo.util.DateUtil;
import com.example.leo.recorddemo.util.FileUtils;
import com.example.leo.recorddemo.util.RxRecordDetector;
import com.example.leo.recorddemo.util.VideoRecordUtil;
import com.example.leo.recorddemo.util.SDcardUtil;
import com.example.leo.recorddemo.util.ToastUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private Button recordBtn;
    private TextView recordResultTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        recordBtn = findViewById(R.id.recordBtn);
        recordBtn.setOnClickListener(this);
        recordResultTv = findViewById(R.id.recordResultTv);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.recordBtn:
                getRxPermission()
                        .request(Manifest.permission.CAMERA,
                                Manifest.permission.RECORD_AUDIO,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE)
                        .subscribe(aBoolean -> {
                            if (aBoolean) {
                                VideoRecordUtil.startRecordNoParam(context);
                            } else {
                                ToastUtil.show(context, "获取权限失败");
                            }
                        }, throwable -> {

                        });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (null == data) {
                return;
            }
            switch (requestCode) {
                case VideoRecordUtil.CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE:
                    Uri videoUri = data.getData();
                    String path = FileUtils.getPath(context, videoUri);
                    recordResultTv.append(path);
                    recordResultTv.append("\n");
                    if (TextUtils.isEmpty(path)) {
                        return;
                    }
                    File file = new File(path);
                    if (!file.exists()) {
                        return;
                    }
                    if (!SDcardUtil.isVideo_PATH(context)) {
                        return;
                    }
                    String videoFilePath = SDcardUtil.VIDEO_PATH + context.getString(R.string.text_video_path, DateUtil.format(String.valueOf(System.currentTimeMillis() / 1000), DateUtil.DATA_YYYY_MM_DD_HH_MM_SS2));
                    File newFile = new File(videoFilePath);
                    if (newFile.exists()) {
                        newFile.delete();
                    }
                    file.renameTo(newFile);
                    recordResultTv.append(videoFilePath);
                    recordResultTv.append("\n");
                    file.delete();
                    break;
            }
        }
    }

    /*
     * 开始录制
     * */
    private Disposable disposable;

    private void startRecord() {
        if (!VideoRecordUtil.startRecordNoParams(context)) {
            finish();
        } else if (RomUtil.rom() == Target.MIUI) {
            disposable = RxRecordDetector.start(this)
                    .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(path -> {
                        if (null != disposable && !disposable.isDisposed()) {
                            disposable.dispose();
                        }
                        File file = new File(path);
                        if (!file.exists()) {
                            ToastUtil.show(context, R.string.text_mp4_no_exists);
                            finish();
                            return;
                        }
                        if (!file.canRead()) {
                            ToastUtil.show(context, R.string.text_videofile_cant_read);
                            finish();
                            return;
                        }
                        AppStackManager.getInstance().notifyMediaScan(file);
                        long fileSize = FileUtils.getFileSize(file);
                        String formetFileSize = FileUtils.formetFileSize(fileSize);
                        mFileSizeTv.setText(formetFileSize);
                    }, throwable -> {
                        ToastUtil.show(context, R.string.text_parse_videofile_fail);
                        finish();
                    });
        }
    }
}
