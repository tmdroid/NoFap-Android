package org.nofap.emergency;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.nofap.emergency.NoFapApplication;

public class Preferences {

    private static final String KEY_RELIGIOUS = "getReligious";

    private static SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(NoFapApplication.getContext());
    }

    /**
     * Sets the religious value in the shared preferences
     *
     * @param religious
     */
    public static void setReligious(boolean religious) {
        SharedPreferences prefs = getSharedPreferences();
        SharedPreferences.Editor e = prefs.edit();
        e.putBoolean(KEY_RELIGIOUS, religious);
        e.apply();
    }

    /**
     * Gets the religious value from the shared preferences
     *
     * @return
     */
    public static boolean getReligious() {
        return getSharedPreferences().getBoolean(KEY_RELIGIOUS, false);
    }

}
