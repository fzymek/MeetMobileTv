package meet.mobile.tv.presenter;

import android.support.v17.leanback.widget.AbstractDetailsDescriptionPresenter;

import meet.mobile.model.Image;

/**
 * Created by Filip on 2015-09-07.
 */
public class DetailsDescriptionPresenter
		extends AbstractDetailsDescriptionPresenter {

	@Override
	protected void onBindDescription(ViewHolder viewHolder, Object itemData) {
		Image details = (Image) itemData;

		// Here we provide static data for testing purposes:
		viewHolder.getTitle().setText(details.getTitle());
		viewHolder.getSubtitle().setText(details.getArtist());
		viewHolder.getBody().setText(details.getCaption());
	}
}