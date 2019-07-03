package com.example.leo.recorddemo.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.leo.recorddemo.util.AppStackManager;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import javax.inject.Inject;

public class BaseActivity extends RxAppCompatActivity {
    private final String TAG = BaseActivity.class.getSimpleName();
    protected Context context;
    public RxPermissions rxPermissions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppStackManager.getInstance().addActivity(this);
        context = this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppStackManager.getInstance().killSingleActivity(this);
    }

    public RxPermissions getRxPermission() {
        if (null == rxPermissions) {
            synchronized (TAG) {
                if (null == rxPermissions) {
                    rxPermissions = new RxPermissions(this);
                }
            }
        }
        return rxPermissions;
    }
}
