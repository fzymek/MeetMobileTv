package meet.mobile.tv.controller;

import android.app.Fragment;
import android.util.Pair;

import meet.mobile.R;
import meet.mobile.config.API;
import meet.mobile.controller.FragmentController;
import meet.mobile.model.Result;
import meet.mobile.network.GettyImagesAPI;
import meet.mobile.tv.ui.TvMainUI;
import retrofit.RestAdapter;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Filip on 2015-09-07.
 */
public class TvBrowseController extends FragmentController<TvMainUI> implements Observer<Pair<String, Result>> {

	TvMainUI ui;
	protected RestAdapter restAdapter;
	GettyImagesAPI gettyImages;

	public TvBrowseController(Fragment fragment) {
		super(fragment);
	}

	@Override
	public void initialize(TvMainUI tvMainUI) {
		this.ui = tvMainUI;
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
		ui.onLoadingStarted();
		String searchPhrases[] = getFragment().getActivity().getResources().getStringArray(R.array.animals_array);

		subscribeWith(
				Observable.from(searchPhrases)
						.concatMap(phrase -> Observable.just(new Pair<>(phrase, gettyImages.getImages(phrase).toBlocking().single())))
						.subscribeOn(Schedulers.io())
						.observeOn(AndroidSchedulers.mainThread())
						.subscribe(this)
		);

	}

	@Override
	public void onCompleted() {
		ui.onLoadingStopped();
	}

	@Override
	public void onError(Throwable throwable) {
		ui.onError(throwable);
	}

	@Override
	public void onNext(Pair<String, Result> resultPair) {
		ui.displayImages(resultPair.first, resultPair.second.getImages());
	}
}
