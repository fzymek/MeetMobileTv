package meet.mobile.tv.ui;

import java.util.List;

import meet.mobile.model.Image;
import meet.mobile.ui.DetailsUI;

/**
 * Created by Filip on 2015-09-07.
 */
public interface TvDetailsUI extends DetailsUI {

    void onError(Throwable throwable);
    void showRecomendations(List<Image> recomendations);
}
