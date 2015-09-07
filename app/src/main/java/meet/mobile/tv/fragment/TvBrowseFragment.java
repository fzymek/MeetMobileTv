package meet.mobile.tv.fragment;

import android.os.Bundle;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.SinglePresenterSelector;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import meet.mobile.R;
import meet.mobile.model.Image;
import meet.mobile.tv.TvMainUI;
import meet.mobile.tv.adapter.ImageAdapter;
import meet.mobile.tv.controller.TvBrowseController;
import meet.mobile.tv.presenter.CardPresenter;
import meet.mobile.tv.utils.BackgroundHelper;

/**
 * Created by Filip on 2015-09-07.
 */
public class TvBrowseFragment extends BrowseFragment implements TvMainUI {

	private static final String TAG = TvBrowseFragment.class.getSimpleName();

	private ArrayObjectAdapter rowsAdapter;
	private BackgroundHelper bgHelper;

	TvBrowseController controller;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initController(savedInstanceState);
	}

	private void initController(Bundle savedInstanceState) {
		controller = new TvBrowseController(this);
		controller.initialize(this);
		controller.restoreState(savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		init();
	}

	public void init() {
		rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
		setAdapter(rowsAdapter);

		setBrandColor(getResources().getColor(R.color.primary));
		setBadgeDrawable(getResources().getDrawable(R.mipmap.ic_launcher));

		bgHelper = new BackgroundHelper(getActivity());
		bgHelper.prepareBackgroundManager();

		controller.loadData();
	}

	@Override
	public void onStop() {
		bgHelper.release();
		super.onStop();
	}

	protected OnItemViewClickedListener getDefaultItemViewClickedListener() {
		return (viewHolder, o, viewHolder2, row) -> {

		};
	}

	protected OnItemViewSelectedListener getDefaultItemSelectedListener() {

		return (itemViewHolder, item, rowViewHolder, row) -> {
			if (item instanceof Image) {
				bgHelper.setBackgroundUrl(((Image) item).getDisplayByType(Image.DisplaySizeType.LARGE).getUri());
			}
		};
	}

	@Override
	public void onError(Throwable error) {
		Toast.makeText(getActivity(), "Error happened: " + error.getMessage(), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onLoadingStarted() {

	}

	@Override
	public void onLoadingStopped() {

	}

	@Override
	public void onDisplayImages(List<Image> images) {
		throw new UnsupportedOperationException("not implemented");
	}

	@Override
	public void displayImages(String phrase, List<Image> images) {
		if (getOnItemViewClickedListener() == null) {
			setOnItemViewClickedListener(getDefaultItemViewClickedListener());
		}
		if (getOnItemViewSelectedListener() == null) {
			setOnItemViewSelectedListener(getDefaultItemSelectedListener());
		}

		ImageAdapter adapter = new ImageAdapter(new SinglePresenterSelector(new CardPresenter()), images);
		HeaderItem headerItem = new HeaderItem(phrase);
		rowsAdapter.add(new ListRow(headerItem, adapter));
	}
}
