package meet.mobile.tv.service;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import java.util.Random;

import meet.mobile.R;
import meet.mobile.config.API;
import meet.mobile.model.Image;
import meet.mobile.model.Result;
import meet.mobile.network.GettyImagesAPI;
import meet.mobile.tv.activity.TvDetailsActivity;
import meet.mobile.tv.activity.TvPlayerActivity;
import meet.mobile.tv.fragment.TvDetailsFragment;
import meet.mobile.tv.utils.TvImageUtils;
import retrofit.RestAdapter;

public class RecommendationsService extends IntentService {

    public static final String EXTRA_BACKGROUND_IMAGE_URL = "background_image_url";

    private static final String TAG = "RecommendationsService";
    private static final int MAX_RECOMMENDATIONS = 3;
    private static final int DETAIL_THUMB_WIDTH = 274;
    private static final int DETAIL_THUMB_HEIGHT = 274;
    private static final DisplayImageOptions DISPLAY_OPTIONS = TvImageUtils.getDefaultImageOptions().build();
    private static final String[] RECOMENDATIONS = {
            "bear",
            "wasp",
            "tiger"
    };

    GettyImagesAPI api;
    RestAdapter restAdapter;
    NotificationManager mNotificationManager;


    public RecommendationsService() {
        super("RecommendationsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(TAG, "handing recomendations request");
        mNotificationManager = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);

        restAdapter = new RestAdapter.Builder()
                .setEndpoint(API.GETTY_IMAGES_SEARCH_ENDPOINT)
                .build();
        api = restAdapter.create(GettyImagesAPI.class);

        //run on current thread as IntentService are not running on main thread
        //this will also ensure context is valid in onNext because service is not stopped
        String phrase = RECOMENDATIONS[new Random().nextInt(RECOMENDATIONS.length)];
        api.getImages(phrase)
                .subscribe(
                        (this::provideRecomendation),
                        (throwable) -> {
                            Log.d(TAG, "Cannot provide recomendation: " + throwable.getMessage());
                        }
                );
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void provideRecomendation(Result result) {

        Log.d(TAG, "creating recomendations");

        int count = 1;
        for (int i = 0; i < MAX_RECOMMENDATIONS; i++) {
            Image img = result.getImages().get(i);
            Log.d(TAG, "creating recomendation for: "+ img);
            PendingIntent pendingIntent = buildPendingIntent(img);

            Bundle extras = new Bundle();
            extras.putString(EXTRA_BACKGROUND_IMAGE_URL, img.getDisplayByType(Image.DisplaySizeType.LARGE).getUri());
            count++;

            ImageSize size = new ImageSize(DETAIL_THUMB_WIDTH, DETAIL_THUMB_HEIGHT);
            Bitmap image = ImageLoader.getInstance().loadImageSync(img.getDisplayByType(Image.DisplaySizeType.PREVIEW).getUri(),
                    size,
                    DISPLAY_OPTIONS);


            Notification notification = new NotificationCompat.BigPictureStyle(
                    new NotificationCompat.Builder(getApplicationContext())
                            .setContentTitle(img.getTitle())
                            .setContentText(img.getArtist())
                            .setPriority(4)
                            .setLocalOnly(true)
                            .setOngoing(true)
                            .setColor(getApplicationContext().getResources().getColor(R.color.primary))
                            .setCategory(Notification.CATEGORY_RECOMMENDATION)
                            .setCategory("recommendation")
                            .setLargeIcon(image)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentIntent(pendingIntent)
                            .setExtras(extras))
                    .build();
            mNotificationManager.notify(count, notification);
        }

        mNotificationManager = null;

    }

    private PendingIntent buildPendingIntent(Image image) {
        Intent detailsIntent = new Intent(this, TvPlayerActivity.class);
        detailsIntent.putExtra(Image.INTENT_EXTRA_IMAGE, image);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(TvDetailsActivity.class);
        stackBuilder.addNextIntent(detailsIntent);
        // Ensure a unique PendingIntents, otherwise all recommendations end up with the same
        // PendingIntent
        detailsIntent.setAction(image.getId());

        PendingIntent intent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        return intent;
    }

}
