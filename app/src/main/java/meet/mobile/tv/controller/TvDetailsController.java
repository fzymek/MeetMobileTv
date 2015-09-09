package meet.mobile.tv.controller;

import android.app.Fragment;

import meet.mobile.controller.FragmentController;
import meet.mobile.model.Image;
import meet.mobile.tv.ui.TvDetailsUI;

/**
 * Created by Filip on 2015-09-07.
 */
public class TvDetailsController extends FragmentController<TvDetailsUI> {

    TvDetailsUI ui;

    public TvDetailsController(Fragment fragment) {
        super(fragment);
    }

    @Override
    public void initialize(TvDetailsUI tvDetailsUI) {
        this.ui = tvDetailsUI;
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
}
