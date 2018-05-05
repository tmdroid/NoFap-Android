package org.nofap.emergency.api;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import org.nofap.emergency.Preferences;

public class NoFapApi {
    public static final String CATEGORY_EMERGENCY = "em";
    public static final String CATEGORY_REJECTION = "rej";
    public static final String CATEGORY_DEPRESSION = "dep";
    public static final String CATEGORY_RELAPSED = "rel";

    private static final String BASE_URL = "https://emergency.nofap.com/";
    private static NoFapService service = null;

    private static OkHttpClient buildClient() {
        return new OkHttpClient
                .Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
    }

    private static NoFapService getClient() {
        if (service == null) {
            service = new Retrofit.Builder()
                    .client(buildClient())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .build()
                    .create(NoFapService.class);
        }

        return service;
    }

    public static Observable<String> load(String category) {
        return getClient()
                .getPostUrl(category, Preferences.getReligious())
                .subscribeOn(Schedulers.io());
    }
}
