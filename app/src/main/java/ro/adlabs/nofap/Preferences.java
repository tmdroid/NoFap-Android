package ro.adlabs.nofap;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Danny on 9/9/2016.
 */
public class Preferences {

    private static final String KEY_RELIGIOUS = "getReligious";

    private static SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(Application.getContext());
    }

    public static void setReligious(boolean religious) {
        SharedPreferences prefs = getSharedPreferences();
        SharedPreferences.Editor e = prefs.edit();
        e.putBoolean(KEY_RELIGIOUS, religious);
        e.commit();
    }

    public static boolean getReligious() {
        return getSharedPreferences().getBoolean(KEY_RELIGIOUS, false);
    }

}
