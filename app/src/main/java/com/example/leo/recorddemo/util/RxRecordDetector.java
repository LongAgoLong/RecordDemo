package com.example.leo.recorddemo.util;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import io.reactivex.Observable;

/**
 * Create by LEO
 * on 2018/7/30
 * at 17:56
 * in MoeLove Company
 * 视频文件监控
 */

public final class RxRecordDetector {

    private static final String TAG = "RxRecordDetector";
    private static final String EXTERNAL_CONTENT_URI_MATCHER =
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI.toString();
    private static final String[] PROJECTION = new String[]{
            MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DATE_ADDED
    };
    private static final String SORT_ORDER = MediaStore.Video.Media.DATE_ADDED + " DESC";
    private static final long DEFAULT_DETECT_WINDOW_SECONDS = 10;

    private final Activity mActivity;

    private RxRecordDetector(final Activity activity) {
        mActivity = activity;
    }

    /**
     * start screenshot detect, if permission not granted, the observable will terminated with
     * an onError event.
     * <p>
     * <p>
     * <em>Warning:</em> The created observable keeps a strong reference to {@code context}.
     * Unsubscribe to free this reference.
     * <p>
     *
     * @return {@link Observable} that emits screenshot file path.
     */
    public static Observable<String> start(final Activity activity) {
        return new RxRecordDetector(activity)
                .start();
    }

    private static boolean matchPath(String path) {
        return path.toLowerCase().contains("mp4");
    }

    private static boolean matchTime(long currentTime, long dateAdded) {
        return Math.abs(currentTime - dateAdded) <= DEFAULT_DETECT_WINDOW_SECONDS;
    }

    private Observable<String> start() {
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return startAfterPermissionGranted(mActivity);
        } else {
            return Observable.error(new SecurityException("Permission not granted"));
        }
    }

    private Observable<String> startAfterPermissionGranted(final Context context) {
        final ContentResolver contentResolver = context.getContentResolver();

        return Observable.create(emitter -> {
            final ContentObserver contentObserver = new ContentObserver(null) {
                @Override
                public void onChange(boolean selfChange, Uri uri) {
                    Log.d(TAG, "onChange: " + selfChange + ", " + uri.toString());
                    if (uri.toString().startsWith(EXTERNAL_CONTENT_URI_MATCHER)) {
                        Cursor cursor = null;
                        try {
                            cursor = contentResolver.query(uri, PROJECTION, null, null,
                                    SORT_ORDER);
                            if (cursor != null && cursor.moveToFirst()) {
                                String path = cursor.getString(
                                        cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                                long dateAdded = cursor.getLong(cursor.getColumnIndex(
                                        MediaStore.Video.Media.DATE_ADDED));
                                long currentTime = System.currentTimeMillis() / 1000;
//                                Log.d(TAG, "path: " + path + ", dateAdded: " + dateAdded +
//                                        ", currentTime: " + currentTime);
                                if (matchPath(path) && matchTime(currentTime, dateAdded)) {
                                    emitter.onNext(path);
                                }
                            }
                        } catch (Exception e) {
                            Log.d(TAG, "open cursor fail");
                        } finally {
                            if (cursor != null) {
                                cursor.close();
                            }
                        }
                    }
                    super.onChange(selfChange, uri);
                }
            };
            contentResolver.registerContentObserver(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI, true, contentObserver);

            emitter.setCancellable(
                    () -> contentResolver.unregisterContentObserver(contentObserver));
        });
    }
}