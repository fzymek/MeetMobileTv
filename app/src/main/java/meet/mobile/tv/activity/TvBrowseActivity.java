package meet.mobile.tv.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import meet.mobile.R;
import meet.mobile.tv.service.RecommendationsService;

/**
 * Created by Filip on 2015-09-06.
 */
public class TvBrowseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_browse);

        //only for demo -> boot reciver does that
        startService(new Intent(this, RecommendationsService.class));
    }
}
