package meet.mobile.tv.utils;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import meet.mobile.R;

public class TvImageUtils {
    private TvImageUtils(){}

    public static DisplayImageOptions.Builder getDefaultImageOptions() {
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_launcher)
                .resetViewBeforeLoading(false)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer());
    }

    public static DisplayImageOptions.Builder getTVBackgroundImageOptions() {
        return new DisplayImageOptions.Builder()
                .cacheOnDisk(false)
                .cacheInMemory(true)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher);
    }
}
