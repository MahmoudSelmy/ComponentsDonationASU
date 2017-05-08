package com.sheatouk.selmy.componentsdonationasu.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sheatouk.selmy.componentsdonationasu.POJO.UserModel;
import com.sheatouk.selmy.componentsdonationasu.R;
import com.sheatouk.selmy.componentsdonationasu.Services.DonationReqListner;
import com.sheatouk.selmy.componentsdonationasu.Services.RepliesListnerService;
import com.sheatouk.selmy.componentsdonationasu.Util.Constant;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MenuItemActivity extends BaseActivity {
    @BindView(R.id.nav_image_profile)
    CircleImageView profilePicV;
    @BindView(R.id.nav_name_profile)
    TextView profileNameV;
    @BindView(R.id.nav_position_profile)
    TextView profilePositionV;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_item);
        setToolbar();
        setViewPager();
        setTitle("");
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        ButterKnife.bind(this,navigationView.getHeaderView(0));
        startService(new Intent(this, DonationReqListner.class));
        startService(new Intent(this, RepliesListnerService.class));
        mAuth=FirebaseAuth.getInstance();
        populateUserData(mAuth.getCurrentUser().getUid());
        // invalidateOptionsMenu();
        // int mCurrentVersion = getIntent().getIntExtra(EXTRA_KEY_VERSION, SearchView.VERSION_TOOLBAR);
    }
    private void populateUserData(String userId) {
        DatabaseReference mUsers = FirebaseDatabase.getInstance().getReference().child(Constant.FIREBASE_USERS_DB_KEY);
        mUsers.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserModel user = dataSnapshot.getValue(UserModel.class);
                profileNameV.setText(user.getName());
                profilePositionV.setText(user.getPosition());
                Picasso.with(MenuItemActivity.this).load(user.getImageUrl()).resize(300, 300).networkPolicy(NetworkPolicy.OFFLINE).into(profilePicV, new Callback() {
                    @Override
                    public void onSuccess() {

                    }
                    @Override
                    public void onError() {
                        // if it not available online we need to download it
                        Picasso.with(MenuItemActivity.this).load(user.getImageUrl()).into(profilePicV);
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

