package meet.mobile.dagger.componentes;

import android.app.Activity;

import dagger.Component;
import meet.mobile.dagger.modules.ActivityModule;
import meet.mobile.dagger.scope.PerActivity;

/**
 * Created by Filip Zymek on 2015-06-19.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    Activity getActivity();
}
