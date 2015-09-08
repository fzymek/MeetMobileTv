package meet.mobile.tv.ui;

import java.util.List;
import java.util.Map;

import meet.mobile.model.Image;
import meet.mobile.ui.MainUI;

/**
 * Created by Filip on 2015-09-07.
 */
public interface TvMainUI extends MainUI {

	void displayImages(String phrase, List<Image> images);

}
