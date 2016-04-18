package kidouchi.twitterstreaming.instagram;

import java.util.List;

import kidouchi.twitterstreaming.model.InstagramMedia;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by iuy407 on 4/12/16.
 */
public interface InstagramServiceInterface {

//    tags/{tag-name}?access_token=ACCESS-TOKEN
    @FormUrlEncoded
    @GET("tags/{tag-name}")
    Call<List<InstagramMedia>> search(@Path("tag-name") String tagName, @Field("access_token") String accessToken);
}
