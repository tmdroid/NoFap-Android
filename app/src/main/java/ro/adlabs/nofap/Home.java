package ro.adlabs.nofap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

public class Home extends AppCompatActivity implements View.OnClickListener{

    public static final String INTENT_FILTER_SERVICE_READY = "ro.adlabs.nofap.SERVICE_READY";
    public static final String INTENT_EXTRA_NAME = "newUrl";

    private CheckBox cbReligious;

    private BroadcastReceiver receiver;
    private boolean receiverRegistered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        cbReligious = (CheckBox) findViewById(R.id.cbReligious);
        cbReligious.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("App", "check changed");
            }
        });

        LinearLayout llEmergency = (LinearLayout) findViewById(R.id.ll_em);
        LinearLayout llRejection = (LinearLayout) findViewById(R.id.ll_rej);
        LinearLayout llDepression = (LinearLayout) findViewById(R.id.ll_dep);
        LinearLayout llRelapsed = (LinearLayout) findViewById(R.id.ll_rel);
        LinearLayout llReligious = (LinearLayout) findViewById(R.id.llReligious);

        if (llEmergency != null) {
            llEmergency.getChildAt(0).setOnClickListener(this);
        }
        if (llRejection != null) {
            llRejection.getChildAt(0).setOnClickListener(this);
        }
        if (llDepression != null) {
            llDepression.getChildAt(0).setOnClickListener(this);
        }
        if (llRelapsed != null) {
            llRelapsed.getChildAt(0).setOnClickListener(this);
        }
        if (llReligious != null) {
            llReligious.getChildAt(0).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        int id = ((View) v.getParent()).getId();

        switch (id) {
            case R.id.llReligious:
                Functions.religious(cbReligious);
                break;
            case R.id.ll_em:
                Functions.emergency();
                break;
            case R.id.ll_rej:
                Functions.rejection();
                break;
            case R.id.ll_dep:
                Functions.depression();
                break;
            case R.id.ll_rel:
                Functions.relapsed();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver();
    }

    private void registerReceiver() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String newUrl = intent.getExtras().getString(INTENT_EXTRA_NAME);

                if(Functions.isChromeCustomTabsSupported()) {
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    builder.setToolbarColor(Color.parseColor("#36465D"));
                    builder.setStartAnimations(Home.this, R.anim.slide_in_right, R.anim.slide_out_left);
                    builder.setExitAnimations(Home.this, R.anim.slide_in_left, R.anim.slide_out_right);
                    builder.addDefaultShareMenuItem();

                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.launchUrl(Home.this, Uri.parse(newUrl));
                } else {
                    Intent openInBrowser = new Intent(Intent.ACTION_VIEW);
                    openInBrowser.setData(Uri.parse(newUrl));
                    startActivity(openInBrowser);
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(INTENT_FILTER_SERVICE_READY);

        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
        receiverRegistered = true;
    }

    private void unregisterReceiver() {
        if(receiverRegistered) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_submit:
                Functions.submitNew(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
