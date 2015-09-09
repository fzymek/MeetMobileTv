package meet.mobile.dagger.modules;

import android.app.Activity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Filip Zymek on 2015-06-19.
 */
@Module
public class ActivityModule {

    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;

    }

    @Provides
    public Activity getActivity() {
        return this.activity;
    }

}
