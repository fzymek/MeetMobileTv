package meet.mobile.dagger.componentes;

import android.app.Activity;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import meet.mobile.dagger.modules.ApplicationModule;

/**
 * Created by Filip Zymek on 2015-06-19.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    Context getContext();

    void inject(Activity baseActivity);
}
