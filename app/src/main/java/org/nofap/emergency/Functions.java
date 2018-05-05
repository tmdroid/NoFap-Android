package org.nofap.emergency;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsServiceConnection;
import android.widget.CheckBox;

public class Functions {
    /**
     * Change the state of the religious post request in the shared preferences
     *
     * @param religious
     */
    public static void religious(CheckBox religious) {
        if (religious.isChecked()) {
            religious.setChecked(false);
        } else {
            religious.setChecked(true);
        }
    }

    /**
     * Open the form to add a new item to the emergency database
     * @param context
     */
    public static void submitNew(Context context) {
        String url = "https://emergency.nofap.com/suggestor.php";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        context.startActivity(i);
    }

    /**
     * Check if Chrome CustomTabs are supported.
     * Some devices don't have Chrome or it may not be
     * updated to a version where custom tabs is supported.
     *
     * @return whether custom tabs are supported
     */
    public static boolean isChromeCustomTabsSupported() {
        Context context = NoFapApplication.getContext();
        Intent serviceIntent = new Intent("android.support.customtabs.action.CustomTabsService");
        serviceIntent.setPackage("com.android.chrome");

        CustomTabsServiceConnection serviceConnection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(final ComponentName componentName, final CustomTabsClient customTabsClient) {
            }

            @Override
            public void onServiceDisconnected(final ComponentName name) {
            }
        };

        boolean customTabsSupported =
                context.bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE | Context.BIND_WAIVE_PRIORITY);
        context.unbindService(serviceConnection);

        return customTabsSupported;
    }

}
