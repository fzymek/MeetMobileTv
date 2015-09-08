package meet.mobile.controller;


import android.app.Fragment;
import android.os.Bundle;

import meet.mobile.fragment.DetailsFragment;
import meet.mobile.model.Image;
import meet.mobile.ui.DetailsUI;

/**
 * Created by Filip on 2015-09-07.
 */
public class DetailsController extends FragmentController<DetailsUI> {

	private static final String IMAGE = "img_parcelable";

	DetailsUI ui;
	Image image;

	public DetailsController(Fragment fragment) {
		super(fragment);
	}

	@Override
	public void initialize(DetailsUI detailsUI) {
		this.ui = detailsUI;
	}

	@Override
	public void saveState(Object outState) {
		if (outState instanceof Bundle) {
			Bundle state = (Bundle) outState;
			state.putParcelable(IMAGE, image);
		}
	}

	@Override
	public void restoreState(Object savedState) {
		if (savedState instanceof Bundle) {
			Bundle state = (Bundle) savedState;
			image = state.getParcelable(IMAGE);
		}
	}

	public void loadData(Bundle arguments) {
		this.image = arguments.getParcelable(DetailsFragment.ARG_IMAGE);
		ui.showDetails(image);
	}
}
