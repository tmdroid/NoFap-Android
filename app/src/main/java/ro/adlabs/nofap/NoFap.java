package ro.adlabs.nofap;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

class NoFap extends AsyncTask<String, Void, Void> {
    private static final String C_CATEGORY = "cat=";
    private static final String C_RELIGIOUS = "religious=";

    static final String CATEGORY_EMERGENCY = "em";
    static final String CATEGORY_REJECTION = "rej";
    static final String CATEGORY_DEPRESSION = "dep";
    static final String CATEGORY_RELAPSED = "rel";

    private String baseUrl = "https://emergency.nofap.com/director.php?";

    NoFap() {
    }


    void get(String category) {
        boolean religious = Preferences.getReligious();
        String requestUrl = baseUrl + C_CATEGORY + category + "&" + C_RELIGIOUS + religious
                + "&platform=androidNative";

        Log.d("URL", requestUrl);

        execute(requestUrl);
    }

    @Override
    protected Void doInBackground(String... params) {
        String url = params[0];

        try {
            Document doc = Jsoup.connect(url).get();
            String newUrl = doc.text();

            Intent response = new Intent(Home.INTENT_FILTER_SERVICE_READY);
            response.putExtra(Home.INTENT_EXTRA_NAME, newUrl);
            LocalBroadcastManager.getInstance(Application.getContext()).sendBroadcast(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
