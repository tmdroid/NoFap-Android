package org.nofap.emergency;

import android.content.Context;

/**
 * Created by Danny on 9/9/2016.
 */
public class NoFapApplication extends android.app.Application {

    private static Context mContext = null;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;
    }

    public static Context getContext() {
        return mContext;
    }
}
