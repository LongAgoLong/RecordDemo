package com.example.leo.recorddemo.rom;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by LEO
 * on 2017/7/3.
 */
// 自定义一个注解Mode
@IntDef({Target.MIUI,
        Target.EMUI,
        Target.FLYME,
        Target.JPUSH})
@Retention(RetentionPolicy.SOURCE)
public @interface Target {
    int JPUSH = 1;
    int MIUI = 2;
    int EMUI = 3;
    int FLYME = 4;
}
