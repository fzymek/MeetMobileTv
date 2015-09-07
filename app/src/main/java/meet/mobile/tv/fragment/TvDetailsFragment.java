package meet.mobile.tv.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v17.leanback.app.DetailsFragment;
import android.support.v17.leanback.widget.Action;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ClassPresenterSelector;
import android.support.v17.leanback.widget.DetailsOverviewRow;
import android.support.v17.leanback.widget.DetailsOverviewRowPresenter;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnActionClickedListener;
import android.util.Log;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import meet.mobile.R;
import meet.mobile.model.Image;
import meet.mobile.tv.activity.VideoDetailsActivity;
import meet.mobile.tv.controller.TvDetailsController;
import meet.mobile.tv.presenter.DetailsDescriptionPresenter;
import meet.mobile.tv.ui.TvDetailsUI;
import meet.mobile.tv.utils.BackgroundHelper;

/**
 * Created by Filip on 2015-09-07.
 */
public class TvDetailsFragment extends DetailsFragment implements TvDetailsUI {

	TvDetailsController controller;

	private static final int DETAIL_THUMB_WIDTH = 274;
	private static final int DETAIL_THUMB_HEIGHT = 274;

	private static final int ACTION_PLAY = 1;
	private static final int ACTION_WATCH_LATER = 2;
	DetailRowBuilderTask rowBuilderTask;
	BackgroundHelper bgHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initController(savedInstanceState);


		bgHelper = new BackgroundHelper(getActivity());
		bgHelper.prepareBackgroundManager();

		Image image = getActivity().getIntent().getParcelableExtra(VideoDetailsActivity.INTENT_EXTRA_IMAGE);
		controller.loadDetails(image);

	}

	private void initController(Bundle savedInstanceState) {
		controller = new TvDetailsController(this);
		controller.initialize(this);
		controller.restoreState(savedInstanceState);
	}

	@Override
	public void onStop() {
		if (rowBuilderTask != null) {
			rowBuilderTask.cancel(true);
		}
		bgHelper.release();
		super.onStop();
	}

	@Override
	public void showDetails(Image image) {
		bgHelper.setBackgroundUrl(image.getDisplayByType(Image.DisplaySizeType.LARGE).getUri());
		(rowBuilderTask = new DetailRowBuilderTask()).execute(image);
	}

	// Utility method for converting dp to pixels
	public static int dpToPx(int dp, Context ctx) {
		float density = ctx.getResources().getDisplayMetrics().density;
		return Math.round((float) dp * density);
	}


	private class DetailRowBuilderTask extends AsyncTask<Image, Integer, DetailsOverviewRow> {

		@Override
		protected DetailsOverviewRow doInBackground(Image... images) {
			DetailsOverviewRow row = new DetailsOverviewRow(images[0]);
			try {
				Bitmap poster = Picasso.with(getActivity())
						.load(images[0].getDisplayByType(Image.DisplaySizeType.PREVIEW).getUri())
						.resize(dpToPx(DETAIL_THUMB_WIDTH, getActivity().getApplicationContext()),
								dpToPx(DETAIL_THUMB_HEIGHT, getActivity().getApplicationContext()))
						.centerCrop()
						.get();
				row.setImageBitmap(getActivity(), poster);
			} catch (IOException e) {
				Log.e("VideoDetailsFragment", "Cannot load thumbnail for " + images[0].getId(), e);
			}
			row.addAction(new Action(ACTION_PLAY, "Watch"));
			row.addAction(new Action(ACTION_WATCH_LATER, "Watch later"));

			return row;

		}

		@Override
		protected void onPostExecute(DetailsOverviewRow detailRow) {
			ClassPresenterSelector ps = new ClassPresenterSelector();
			DetailsOverviewRowPresenter dorPresenter =
					new DetailsOverviewRowPresenter(new DetailsDescriptionPresenter());
			// set detail background and style
			dorPresenter.setBackgroundColor(getResources().getColor(R.color.primary));
			dorPresenter.setStyleLarge(true);
			dorPresenter.setOnActionClickedListener(new OnActionClickedListener() {
				@Override
				public void onActionClicked(Action action) {
					if (action.getId() == ACTION_PLAY) {
//						Intent intent = new Intent(getActivity(), PlayerActivity.class);
//						intent.putExtra(Video.INTENT_EXTRA_VIDEO, (Serializable)selectedVideo);
//						startActivity(intent);
					}
					else {
						Toast.makeText(getActivity(), action.toString(), Toast.LENGTH_SHORT).show();
					}
				}
			});

			ps.addClassPresenter(DetailsOverviewRow.class, dorPresenter);
			ps.addClassPresenter(ListRow.class,
					new ListRowPresenter());

			/** bonus code for adding related items to details fragment **/
			// <START>
			ArrayObjectAdapter adapter = new ArrayObjectAdapter(ps);
			adapter.add(detailRow);

//			String subcategories[] = {
//					"You may also like"
//			};

//			CursorObjectAdapter rowAdapter = new CursorObjectAdapter(new SinglePresenterSelector(new CardPresenter()));
//			VideoDataManager manager  = new VideoDataManager(getActivity(),getLoaderManager(), VideoItemContract.VideoItem.buildDirUri(),rowAdapter);
//			manager.startDataLoading();
//			HeaderItem header = new HeaderItem(0, subcategories[0], null);
//			adapter.add(new ListRow(header, rowAdapter));
			setAdapter(adapter);
			// <END>



		}

	}
}
