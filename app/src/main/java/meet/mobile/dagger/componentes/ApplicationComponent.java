package meet.mobile.dagger.componentes;

import android.content.Context;

import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import javax.inject.Singleton;

import dagger.Component;
import meet.mobile.activity.BaseActivity;
import meet.mobile.dagger.modules.ApplicationModule;

/**
 * Created by Filip Zymek on 2015-06-19.
 */
@Singleton
@Component (modules = ApplicationModule.class)
public interface ApplicationComponent {
	Context getContext();

	void inject(BaseActivity baseActivity);
}
