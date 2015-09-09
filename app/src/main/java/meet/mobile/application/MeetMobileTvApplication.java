package meet.mobile.application;

import android.app.Application;

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
}
