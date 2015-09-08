package meet.mobile.application;

import android.app.Application;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import meet.mobile.R;
import meet.mobile.dagger.componentes.ApplicationComponent;
import meet.mobile.dagger.componentes.DaggerApplicationComponent;
import meet.mobile.dagger.modules.ApplicationModule;


/**
 * Created by Filip Zymek on 2015-06-19.
 */
public class MeetMobileTvApplication extends Application {

	private ApplicationComponent applicationComponent;

	@Override
	public void onCreate() {
		super.onCreate();

		this.applicationComponent = DaggerApplicationComponent.builder()
			.applicationModule(new ApplicationModule(this))
			.build();
	}

	public ApplicationComponent getApplicationComponent() {
		return applicationComponent;
	}

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