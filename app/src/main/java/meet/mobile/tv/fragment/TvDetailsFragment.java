package meet.mobile.tv.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v17.leanback.app.DetailsFragment;
import android.support.v17.leanback.widget.Action;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ClassPresenterSelector;
import android.support.v17.leanback.widget.DetailsOverviewRow;
import android.support.v17.leanback.widget.DetailsOverviewRowPresenter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.SinglePresenterSelector;
import android.util.Log;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import java.util.List;
import java.util.Random;

import meet.mobile.R;
import meet.mobile.model.Image;
import meet.mobile.tv.activity.TvPlayerActivity;
import meet.mobile.tv.adapter.ImageAdapter;
import meet.mobile.tv.controller.TvDetailsController;
import meet.mobile.tv.presenter.CardPresenter;
import meet.mobile.tv.presenter.DetailsDescriptionPresenter;
import meet.mobile.tv.ui.TvDetailsUI;
import meet.mobile.tv.utils.BackgroundHelper;
import rx.Observable;
import rx.Subscriber;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static meet.mobile.tv.utils.TvImageUtils.getTVBackgroundImageOptions;

/**
 * Created by Filip on 2015-09-07.
 */
public class TvDetailsFragment extends DetailsFragment implements TvDetailsUI {

    private final static String TAG = TvDetailsFragment.class.getSimpleName();

    private static final int POSTER_WIDTH = 274;
    private static final int POSTER_HEIGHT = 274;

    private static final int ACTION_PLAY = 1;
    private static final int ACTION_WATCH_LATER = 2;

    private static final DisplayImageOptions DISPLAY_OPTIONS = getTVBackgroundImageOptions().build();
    private static final String[] RECOMENDATIONS = new String[]{
            "kangaroo",
            "koala",
            "jellyfish"
    };

    TvDetailsController controller;
    BackgroundHelper bgHelper;
    ColorDrawable defaultDrawable;
    ArrayObjectAdapter rowAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defaultDrawable = new ColorDrawable(getResources().getColor(R.color.primary));
        Image image = getActivity().getIntent().getParcelableExtra(Image.INTENT_EXTRA_IMAGE);

        initController(savedInstanceState);
        initBackground(image);

        controller.loadDetails(image);
    }

    @Override
    public void onStop() {
        bgHelper.release();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (bgHelper != null) {
            bgHelper.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    public void showDetails(Image image) {
        final DetailsOverviewRowPresenter rowPresenter = new DetailsOverviewRowPresenter(new DetailsDescriptionPresenter());

        rowPresenter.setStyleLarge(true);
        rowPresenter.setOnActionClickedListener(action -> {
            long actionId = action.getId();

            if (actionId == ACTION_PLAY) {
                Intent player = new Intent(getActivity(), TvPlayerActivity.class);
                player.putExtra(Image.INTENT_EXTRA_IMAGE, image);
                startActivity(player);

            } else if (actionId == ACTION_WATCH_LATER) {
                Toast.makeText(getActivity(), getString(R.string.watch_later), Toast.LENGTH_SHORT).show();
            }

        });

        final ClassPresenterSelector presenterSelector = new ClassPresenterSelector();
        presenterSelector.addClassPresenter(DetailsOverviewRow.class, rowPresenter);
        presenterSelector.addClassPresenter(ListRow.class, new ListRowPresenter());

        rowAdapter = new ArrayObjectAdapter(presenterSelector);
        setAdapter(rowAdapter);

        prepareDetailsOverviewRow(image).subscribe(detailsOverviewRow -> {
            ((ArrayObjectAdapter) getAdapter()).add(detailsOverviewRow);

            //load recomendations
            String recomendation = RECOMENDATIONS[new Random().nextInt(RECOMENDATIONS.length)];
            Log.d(TAG, "requesting recomendations for: "+ recomendation);
            controller.loadRecomendations(recomendation);

        });
    }

    private void initController(Bundle savedInstanceState) {
        controller = new TvDetailsController(this);
        controller.initialize(this);
        controller.restoreState(savedInstanceState);
    }

    private void initBackground(Image image) {
        bgHelper = new BackgroundHelper(getActivity());
        bgHelper.prepareBackgroundManager();
        bgHelper.setBackgroundUrl(image.getDisplayByType(Image.DisplaySizeType.LARGE).getUri());
        bgHelper.startBackgroundTimer();
    }

    /**
     * Prepares details row to be displayed
     * <p>
     * Due to neccessity of downloading or reading image from disk we prepare row in io thread before adding it to adapter
     *
     * @param details
     * @return
     */
    private Observable<DetailsOverviewRow> prepareDetailsOverviewRow(final Image details) {
        return AppObservable.bindFragment(this, Observable.create(new Observable.OnSubscribe<DetailsOverviewRow>() {
            @Override
            public void call(Subscriber<? super DetailsOverviewRow> subscriber) {
                DetailsOverviewRow row = new DetailsOverviewRow(details);
                row.addAction(new Action(ACTION_PLAY, getString(R.string.play)));
                row.addAction(new Action(ACTION_WATCH_LATER, getString(R.string.watch_later)));
                Bitmap poster = loadPoster(details.getDisplayByType(Image.DisplaySizeType.PREVIEW).getUri());
                if (poster != null) {
                    row.setImageBitmap(getActivity(), poster);
                } else {
                    row.setImageDrawable(defaultDrawable);
                }
                subscriber.onNext(row);
                subscriber.onCompleted();
            }
        }))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Bitmap loadPoster(@NonNull final String url) {
        ImageSize size = new ImageSize(POSTER_WIDTH, POSTER_HEIGHT);
        return ImageLoader.getInstance().loadImageSync(url, size, DISPLAY_OPTIONS);
    }

    @Override
    public void onError(Throwable throwable) {
        Toast.makeText(getActivity(), "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showRecomendations(List<Image> recomendations) {

        Log.d(TAG, "Got recomendations: "+ recomendations);
        HeaderItem recomendationsHeader = new HeaderItem(getString(R.string.recomendations));
        ImageAdapter imageAdapter = new ImageAdapter(new SinglePresenterSelector(new CardPresenter()), recomendations);
        rowAdapter.add(new ListRow(recomendationsHeader, imageAdapter));
    }

}
