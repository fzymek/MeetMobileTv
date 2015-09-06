package meet.mobile.activity;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;

import meet.mobile.R;
import meet.mobile.fragment.MainFragment;


public class MainActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setupPreferences();

		if (savedInstanceState == null) {
			Log.d("main", "attaching fragment");
			MainFragment f = MainFragment.newInstance();
			getFragmentManager().beginTransaction()
				.replace(R.id.content_frame, f, "main")
				.commit();
		} else {
			Log.d("main", "no fragment");
		}

	}

	@Override
	public void onBackPressed() {
		if (getFragmentManager().getBackStackEntryCount() > 0) {
			getFragmentManager().popBackStack();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		switch (id) {
			case android.R.id.home:
				onBackPressed();
				return true;

		}
		return super.onOptionsItemSelected(item);
	}


	private void setupPreferences() {
		PreferenceManager.setDefaultValues(this, R.xml.settings, false);
	}

}
