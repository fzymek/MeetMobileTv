package meet.mobile.tv.controller;

import android.app.Fragment;
import android.util.Log;
import android.widget.ImageView;

import java.util.List;

import meet.mobile.config.API;
import meet.mobile.controller.FragmentController;
import meet.mobile.model.Image;
import meet.mobile.model.Result;
import meet.mobile.network.GettyImagesAPI;
import meet.mobile.tv.ui.TvDetailsUI;
import retrofit.RestAdapter;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Filip on 2015-09-07.
 */
public class TvDetailsController extends FragmentController<TvDetailsUI> implements Observer<Result> {

    private final static String TAG = TvDetailsController.class.getSimpleName();

    TvDetailsUI ui;
    RestAdapter restAdapter;
    GettyImagesAPI gettyImages;

    public TvDetailsController(Fragment fragment) {
        super(fragment);
    }

    @Override
    public void initialize(TvDetailsUI tvDetailsUI) {
        this.ui = tvDetailsUI;
        restAdapter = new RestAdapter.Builder()
                .setEndpoint(API.GETTY_IMAGES_SEARCH_ENDPOINT)
                .build();
        gettyImages = restAdapter.create(GettyImagesAPI.class);
    }

    @Override
    public void saveState(Object outState) {

    }

    @Override
    public void restoreState(Object savedState) {

    }

    public void loadDetails(Image image) {
        ui.showDetails(image);
    }

    public void loadRecomendations(String recomendation) {
        Log.d(TAG, "recomendations requested");
        subscribeWith(
                gettyImages.getImages(recomendation)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this)
        );
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        ui.onError(e);
    }

    @Override
    public void onNext(Result result) {
        ui.showRecomendations(result.getImages());
    }
}
