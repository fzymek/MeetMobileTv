package meet.mobile.tv.activity;

import android.app.Activity;
import android.os.Bundle;

import meet.mobile.R;

/**
 * Created by Filip on 2015-09-07.
 */
public class VideoDetailsActivity extends Activity{
	public static final String INTENT_EXTRA_IMAGE = "image";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.avtivity_tv_details);
	}
}
