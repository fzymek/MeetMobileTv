package meet.mobile.tv.adapter;

import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.PresenterSelector;

import java.util.List;

import meet.mobile.model.Image;

/**
 * Created by Filip on 2015-09-07.
 */
public class ImageAdapter extends ArrayObjectAdapter {

    public ImageAdapter(PresenterSelector presenter, List<Image> images) {
        super(presenter);
        addAll(0, images);
    }

}
