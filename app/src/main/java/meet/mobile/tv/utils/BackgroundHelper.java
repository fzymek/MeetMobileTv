package meet.mobile.tv.utils;


import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.support.v17.leanback.app.BackgroundManager;
import android.util.DisplayMetrics;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import java.util.Timer;
import java.util.TimerTask;

import meet.mobile.application.MeetMobileTvApplication;
import rx.Observable;
import rx.Subscriber;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BackgroundHelper {


    private static final long BACKGROUND_UPDATE_DELAY = 200;

    private final Handler mHandler = new Handler();
    private final DisplayImageOptions backgroundImageOptions;
    private Activity mActivity;
    private DisplayMetrics mMetrics;
    private Timer mBackgroundTimer;
    private String mBackgroundURL;
    private BackgroundManager backgroundManager;

    public BackgroundHelper(Activity mActivity) {
        this.mActivity = mActivity;

        backgroundImageOptions = MeetMobileTvApplication.getTVBackgroundImageOptions()
                .postProcessor(new RsBlurProcessor(mActivity))
                .build();
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.mBackgroundURL = backgroundUrl;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void prepareBackgroundManager() {
        backgroundManager = BackgroundManager.getInstance(mActivity);
        backgroundManager.attach(mActivity.getWindow());

        mMetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }

    /**
     * Release references to Drawables. Typically called to reduce memory
     * overhead when not visible.
     */
    public void release() {
        if (backgroundManager != null) {
            backgroundManager.release();
        }
    }

    public void onDestroy() {
        if (mBackgroundTimer != null) {
            mBackgroundTimer.cancel();
            mBackgroundTimer = null;
        }
        backgroundManager = null;
    }

    protected void updateBackground(final String url) {
        AppObservable.bindActivity(mActivity, Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                ImageSize size = new ImageSize(mMetrics.widthPixels / 4, mMetrics.heightPixels / 4);
                Bitmap bitmap = ImageLoader.getInstance().loadImageSync(url, size, backgroundImageOptions);
                subscriber.onNext(bitmap);
                subscriber.onCompleted();
            }
        }))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(backgroundManager::setBitmap);


        if (null != mBackgroundTimer) {
            mBackgroundTimer.cancel();
        }
    }

    public void startBackgroundTimer() {
        if (null != mBackgroundTimer) {
            mBackgroundTimer.cancel();
        }
        mBackgroundTimer = new Timer();
        mBackgroundTimer.schedule(new UpdateBackgroundTask(), BACKGROUND_UPDATE_DELAY);
    }

    private class UpdateBackgroundTask extends TimerTask {
        @Override
        public void run() {
            mHandler.post(() -> {
                if (mBackgroundURL != null) {
                    updateBackground(mBackgroundURL);
                }
            });
        }
    }

}