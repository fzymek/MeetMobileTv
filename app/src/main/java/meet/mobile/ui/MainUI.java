package meet.mobile.ui;


import java.util.List;

import meet.mobile.model.Image;

/**
 * Created by Filip Zymek on 2015-06-08.
 */
public interface MainUI {

    void onError(Throwable error);

    void onLoadingStarted();

    void onLoadingStopped();

    void onDisplayImages(List<Image> images);

}
