package com.sheatouk.selmy.componentsdonationasu.Activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.lapism.searchview.SearchAdapter;
import com.lapism.searchview.SearchHistoryTable;
import com.lapism.searchview.SearchItem;
import com.lapism.searchview.SearchView;
import com.sheatouk.selmy.componentsdonationasu.POJO.Component;
import com.sheatouk.selmy.componentsdonationasu.R;
import com.sheatouk.selmy.componentsdonationasu.SearchFragment;
import com.sheatouk.selmy.componentsdonationasu.Util.Constant;
import com.sheatouk.selmy.componentsdonationasu.Util.FragmentAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc on 30/04/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @SuppressWarnings("WeakerAccess")
    protected static final int NAV_ITEM_INVALID = -1;
    protected static final int NAV_ITEM_MENU_ITEM = 1;
    protected static final String EXTRA_KEY_TEXT = "text";
    private static final String EXTRA_KEY_VERSION = "version";
    private static final String EXTRA_KEY_THEME = "theme";
    private static final String EXTRA_KEY_VERSION_MARGINS = "version_margins";
    protected SearchView mSearchView = null;
    protected DrawerLayout mDrawerLayout = null;
    protected ActionBarDrawerToggle mActionBarDrawerToggle = null;
    private Toolbar mToolbar = null;
    private DatabaseReference compDB;
    private SearchHistoryTable mHistoryDatabase;

    // ---------------------------------------------------------------------------------------------
    protected int getNavItem() {
        return NAV_ITEM_INVALID;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setDrawer();
        setNavigationView();
        if (mActionBarDrawerToggle != null) {
            mActionBarDrawerToggle.syncState();
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed(); // finish
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mActionBarDrawerToggle != null) {
            mActionBarDrawerToggle.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mActionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
    // setQuery to searsh results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SearchView.SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (results != null && results.size() > 0) {
                String searchWrd = results.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    if (mSearchView != null) {
                        mSearchView.setQuery(searchWrd, true);
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // ---------------------------------------------------------------------------------------------
    protected void setToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            mToolbar.setNavigationIcon(null);
            setSupportActionBar(mToolbar);
        }
    }
    // setToolbar
    protected void setViewPager() {
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(new SearchFragment(), getString(R.string.installed));
        adapter.addFragment(new SearchFragment(), getString(R.string.all));

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    // ---------------------------------------------------------------------------------------------
    private void setDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        if (mDrawerLayout != null && mToolbar != null) {
            mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close) {
                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    if (mSearchView != null && mSearchView.isSearchOpen()) {
                        mSearchView.close(true);
                    }

                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);

                }
            };
            mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
            mActionBarDrawerToggle.syncState();
        }
    }
    // Listners to Nav_items events
    private void setNavigationView() { // @Nullable
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(item -> {
                int id = item.getItemId();

                if (id == R.id.nav_item_toolbar || id == R.id.nav_item_filters) {

                }

                if (id == R.id.nav_item_menu_item) {

                }

                if (id == R.id.nav_item_history) {

                }

                item.setChecked(false);
                mDrawerLayout.closeDrawers();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            });
        }
    }

    // it can be in OnCreate
    // add suggestions
    protected void setSearchView() {

        mHistoryDatabase = new SearchHistoryTable(this);

        mSearchView = (SearchView) findViewById(R.id.searchView);
        if (mSearchView != null) {
            mSearchView.setHint("search");
            mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    getData(query, 0);
                    mSearchView.close(false);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
            compDB = FirebaseDatabase.getInstance().getReference().child("Comp");
            compDB.keepSynced(true);
            compDB.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<SearchItem> suggestionsList = new ArrayList<>();
                    for (DataSnapshot child : dataSnapshot.getChildren()){
                        Component component = child.getValue(Component.class);
                        suggestionsList.add(new SearchItem(component.getName()));
                    }
                    SearchAdapter searchAdapter = new SearchAdapter(BaseActivity.this, suggestionsList);
                    searchAdapter.addOnItemClickListener((view, position) -> {
                        TextView textView = (TextView) view.findViewById(R.id.textView_item_text);
                        String query = textView.getText().toString();
                        getData(query, position);
                        mSearchView.close(false);
                    });
                    mSearchView.setAdapter(searchAdapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            mSearchView.setOnOpenCloseListener(new SearchView.OnOpenCloseListener() {
                @Override
                public boolean onOpen() {

                    return true;
                }

                @Override
                public boolean onClose() {

                    return true;
                }
            });
            //mSearchView.setVoiceSearch(false);
            mSearchView.setVoice(false);
        }
    }

    protected void customSearchView() {
        Bundle extras = getIntent().getExtras();
        if (extras != null && mSearchView != null) {
            mSearchView.setVersion(extras.getInt(EXTRA_KEY_VERSION));
            mSearchView.setVersionMargins(extras.getInt(EXTRA_KEY_VERSION_MARGINS));
            mSearchView.setTheme(extras.getInt(EXTRA_KEY_THEME), true);
            mSearchView.setQuery(extras.getString(EXTRA_KEY_TEXT), false);
            int magId = getResources().getIdentifier("android:id/search_mag_icon", null, null);
            ImageView magImage = (ImageView) mSearchView.findViewById(magId);
            //magImage.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
            magImage.setVisibility(View.GONE);
        }
    }

    // ---------------------------------------------------------------------------------------------
    // setActions of history here
    @CallSuper
    protected void getData(String text, int position) {
        mHistoryDatabase.addItem(new SearchItem(text));
        Query selectedItem = compDB.orderByChild("name").equalTo(text);
        selectedItem.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //String keys = "";
                ArrayList<String> keys = new ArrayList<String>();
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    //keys += child.getKey() +" ";
                    keys.add(child.getKey());
                }
                if (!keys.isEmpty()){
                    Intent intent =  new Intent(BaseActivity.this,SearchResultsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList(Constant.FIREBASE__KEY,keys);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"please select one of our comps or request adding new one to our database", Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
