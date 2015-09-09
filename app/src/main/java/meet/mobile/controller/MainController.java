package meet.mobile.controller;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import meet.mobile.R;
import meet.mobile.config.API;
import meet.mobile.config.Config;
import meet.mobile.model.Result;
import meet.mobile.network.GettyImagesAPI;
import meet.mobile.ui.MainUI;
import retrofit.RestAdapter;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Filip Zymek on 2015-06-08.
 */
public class MainController extends FragmentController<MainUI> implements Observer<Result> {

    public static final String TAG = MainController.class.getSimpleName();
    protected MainUI ui;
    protected RestAdapter restAdapter;
    GettyImagesAPI gettyImages;

    public MainController(Fragment fragment) {
        super(fragment);
    }

    @Override
    public void initialize(MainUI mainUI) {
        this.ui = mainUI;
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

    public void loadData() {
        Log.d(TAG, "loadData");
        ui.onLoadingStarted();
        String searchPhrase = getSearchPhrase();
        Observable<Result> imagesObservable = buildImagesObservable(searchPhrase);
        subscribeWith(imagesObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .cache()
                        .subscribe(this)
        );
    }

    @Override
    public void onCompleted() {
        Log.d(TAG, "onCompleted");
        ui.onLoadingStopped();
    }

    @Override
    public void onError(Throwable e) {
        Log.d(TAG, "onError");
        ui.onError(e);
    }

    @Override
    public void onNext(Result result) {
        Log.d(TAG, "onNext");
        ui.onDisplayImages(result.getImages());
    }

    protected String getSearchPhrase() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getFragment().getActivity());
        int key = Integer.parseInt(sharedPreferences.getString(Config.KEY_PREF_FAV_ANIMAL, Config.PREF_FAV_ANIMAL_DEFAULT));
        String animal = getFragment().getActivity().getResources().getStringArray(R.array.animals_array)[key - 1];
        return animal;
    }

    private Observable<Result> buildImagesObservable(String searchPhrase) {
        Observable<Result> imagesObservable = null;
        if (hasCache()) {
            Log.d(TAG, "hasCache, fetching cached object");
            //noinspection unchecked
            imagesObservable = (Observable<Result>) getCache().get(searchPhrase);
        } else {
            Log.d(TAG, "no cache -> will create new entry");
        }

        if (imagesObservable == null) {
            Log.d(TAG, "creating new entry");
            imagesObservable = gettyImages.getImages(searchPhrase);
            if (hasCache()) {
                Log.d(TAG, "has cache -> will save entry");
                getCache().put(searchPhrase, imagesObservable);
            } else {
                Log.d(TAG, "no cache -> will not store entry");
            }
        }
        return imagesObservable;
    }

    public void refreshData() {
        removeCurrentCacheEntries();
        loadData();
    }

    private void removeCurrentCacheEntries() {
        getCache().clear();
    }
}
