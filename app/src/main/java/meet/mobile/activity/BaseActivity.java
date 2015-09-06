package meet.mobile.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import javax.inject.Inject;

import meet.mobile.application.AdvancedRecyclerViewApplication;
import meet.mobile.dagger.componentes.ApplicationComponent;
import meet.mobile.dagger.modules.ActivityModule;


/**
 * Created by Filip Zymek on 2015-06-19.
 */
public abstract class BaseActivity extends AppCompatActivity {

	@Inject
	protected DisplayImageOptions options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getApplicationComponent().inject(this);
	}

	public DisplayImageOptions getDisplayImageOptions() {
		return options;
	}

	protected ApplicationComponent getApplicationComponent() {
		return ((AdvancedRecyclerViewApplication)getApplication()).getApplicationComponent();
	}

	protected ActivityModule getActivityModule() {
		return new ActivityModule(this);
	}
}
