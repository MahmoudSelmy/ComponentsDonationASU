package com.sheatouk.selmy.componentsdonationasu.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;

import com.sheatouk.selmy.componentsdonationasu.R;
import com.sheatouk.selmy.componentsdonationasu.Services.DonationReqListner;
import com.sheatouk.selmy.componentsdonationasu.Services.RepliesListnerService;

public class MenuItemActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_item);
        setToolbar();
        setViewPager();
        setTitle("");
        startService(new Intent(this, DonationReqListner.class));
        startService(new Intent(this, RepliesListnerService.class));
        // invalidateOptionsMenu();
        // int mCurrentVersion = getIntent().getIntExtra(EXTRA_KEY_VERSION, SearchView.VERSION_TOOLBAR);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        mActionBarDrawerToggle.setToolbarNavigationClickListener(v -> {
            mDrawerLayout.openDrawer(GravityCompat.START); // WITHOUT finish(); + finish();
        });

        setSearchView();
        mSearchView.setNavigationIcon(R.drawable.led_icon);
        customSearchView();
    }

}

