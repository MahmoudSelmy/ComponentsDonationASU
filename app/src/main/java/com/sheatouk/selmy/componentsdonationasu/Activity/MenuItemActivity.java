package com.sheatouk.selmy.componentsdonationasu.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;

import com.sheatouk.selmy.componentsdonationasu.R;

public class MenuItemActivity extends BaseActivity {

    @Override
    protected int getNavItem() {
        return NAV_ITEM_MENU_ITEM;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_item);
        setToolbar();
        setViewPager();
        setTitle("");
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

