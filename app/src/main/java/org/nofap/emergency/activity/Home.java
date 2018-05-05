package org.nofap.emergency.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import org.nofap.emergency.Preferences;
import org.nofap.emergency.R;
import org.nofap.emergency.Functions;
import org.nofap.emergency.api.NoFapApi;

public class Home extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.cb_religious)
    CheckBox cbReligious;

    @BindView(R.id.ll_emergency)
    LinearLayout llEmergency;

    @BindView(R.id.ll_rejection)
    LinearLayout llRejection;

    @BindView(R.id.ll_depression)
    LinearLayout llDepression;

    @BindView(R.id.ll_relapsed)
    LinearLayout llRelapsed;

    @BindView(R.id.ll_religious)
    LinearLayout llReligious;

    @BindView(R.id.root)
    RelativeLayout relativeLayout;

    Unbinder unbinder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

/*
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
*/

        setContentView(R.layout.activity_home);
        unbinder = ButterKnife.bind(this);

        llEmergency.getChildAt(0).setOnClickListener(this);
        llRejection.getChildAt(0).setOnClickListener(this);
        llDepression.getChildAt(0).setOnClickListener(this);
        llRelapsed.getChildAt(0).setOnClickListener(this);
        llReligious.getChildAt(0).setOnClickListener(this);

        cbReligious.setOnCheckedChangeListener((buttonView, isChecked) -> Preferences.setReligious(isChecked));
        cbReligious.setChecked(Preferences.getReligious());
    }

    @Override
    public void onClick(View view) {
        int id = ((View) view.getParent()).getId();

        switch (id) {
            case R.id.ll_religious:
                Functions.religious(cbReligious);
                break;
            case R.id.ll_emergency:
                loadCategory(NoFapApi.CATEGORY_EMERGENCY);
                break;
            case R.id.ll_rejection:
                loadCategory(NoFapApi.CATEGORY_REJECTION);
                break;
            case R.id.ll_depression:
                loadCategory(NoFapApi.CATEGORY_DEPRESSION);
                break;
            case R.id.ll_relapsed:
                loadCategory(NoFapApi.CATEGORY_RELAPSED);
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) unbinder.unbind();
    }

    /**
     * Load post from certain category of posts
     *
     * @param category
     */
    private void loadCategory(String category) {
        Snackbar.make(relativeLayout, R.string.request_sent, Snackbar.LENGTH_SHORT)
                .show();

        NoFapApi.load(category)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::loadUrlInBrowser, t -> {
                    t.printStackTrace();
                    Toast.makeText(this, R.string.network_error, Toast.LENGTH_LONG).show();
                });
    }

    /**
     * Load the specified url in a browser or in Chrome Custom Tabs
     * @param url
     */
    private void loadUrlInBrowser(String url) {
        if (Functions.isChromeCustomTabsSupported()) {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setToolbarColor(ContextCompat.getColor(this, R.color.chrome_tabs));
            builder.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
            builder.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);
            builder.addDefaultShareMenuItem();

            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(this, Uri.parse(url));
        } else {
            Intent openInBrowser = new Intent(Intent.ACTION_VIEW);
            openInBrowser.setData(Uri.parse(url));
            startActivity(openInBrowser);
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
