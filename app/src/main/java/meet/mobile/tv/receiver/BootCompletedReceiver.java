package meet.mobile.tv.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import meet.mobile.tv.service.RecommendationsService;

public class BootCompletedReceiver extends BroadcastReceiver {

    private final static String TAG = BootCompletedReceiver.class.getSimpleName();

    private static final long INITIAL_DELAY = TimeUnit.SECONDS.toMillis(5);

    public BootCompletedReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().endsWith(Intent.ACTION_BOOT_COMPLETED)) {
            Log.d(TAG, "Boot completed!");
            scheduleRecommendationUpdate(context);
        }
    }

    private void scheduleRecommendationUpdate(Context context) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent recommendationIntent = new Intent(context, RecommendationsService.class);
        PendingIntent alarmIntent = PendingIntent.getService(context, 0, recommendationIntent, 0);

        Log.d(TAG, "Setting alarm manager to trigger recomendation service");
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                INITIAL_DELAY,
                TimeUnit.MINUTES.toMillis(5),
                alarmIntent);
    }
}