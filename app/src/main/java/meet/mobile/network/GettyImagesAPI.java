package meet.mobile.network;

import meet.mobile.config.API;
import meet.mobile.model.Result;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by Filip Zymek on 2015-06-08.
 */
public interface GettyImagesAPI {

	@Headers("Api-Key: " + API.CONSUMER_KEY)
	@GET("/search/images?fields=detail_set,display_set")
	Observable<Result> getImages(@Query("phrase") String phrase);

}
