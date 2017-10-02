package ro.adlabs.nofap.api;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NoFapService {
    @GET("director.php?platform=androidNative")
    Observable<String> getPostUrl(
            @Query("cat") String category,
            @Query("religious") Boolean religious
    );
}
