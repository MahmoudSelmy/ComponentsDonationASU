package com.sheatouk.selmy.componentsdonationasu.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sheatouk.selmy.componentsdonationasu.Fragments.RequestDonationFragment;
import com.sheatouk.selmy.componentsdonationasu.POJO.Component;
import com.sheatouk.selmy.componentsdonationasu.POJO.InstaComponent;
import com.sheatouk.selmy.componentsdonationasu.POJO.UserModel;
import com.sheatouk.selmy.componentsdonationasu.R;
import com.sheatouk.selmy.componentsdonationasu.Util.Constant;
import com.sheatouk.selmy.componentsdonationasu.Util.MyTextView;
import com.sheatouk.selmy.componentsdonationasu.Util.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchResultsActivity extends AppCompatActivity {
    @BindView(R.id.comp_img) ImageView compImg;
    @BindView(R.id.app_bar) Toolbar toolbar;
    @BindView(R.id.comp_owner_recycler) RecyclerView ownerRecyler;
    @BindView(R.id.comp_donations_recycler) RecyclerView donationRecyler;

    private DatabaseReference mCompDB;
    private String compKey;
    private FirebaseAuth mAuth;
    private DatabaseReference mInstaComp,mUsersDB;
    private FirebaseRecyclerAdapter<InstaComponent,OwnerViewHolder> firebaseRecyclerAdapter;
    private Component component;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        mCompDB = FirebaseDatabase.getInstance().getReference().child(Constant.FIREBASE_COMPONENTS_DB_KEY);
        mCompDB.keepSynced(true);
        mInstaComp = FirebaseDatabase.getInstance().getReference().child(Constant.FIREBASE_INSTA_COMPONENTS_DB_KEY);
        mInstaComp.keepSynced(true);
        mUsersDB = FirebaseDatabase.getInstance().getReference().child(Constant.FIREBASE_USERS_DB_KEY);
        mUsersDB.keepSynced(true);
        mAuth=FirebaseAuth.getInstance();
        // take first
        compKey = getIntent().getExtras().getStringArrayList(Constant.FIREBASE__KEY).get(0);
        pickImage();
        initOwnersList();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initOwnersList();
    }

    private void initOwnersList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        ownerRecyler.setLayoutManager(layoutManager);
        Query owners = mInstaComp.orderByChild("productId").equalTo(compKey);
        owners.keepSynced(true);
        firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<InstaComponent, OwnerViewHolder>(
                InstaComponent.class,//PoJo
                R.layout.owners_row,//row view
                OwnerViewHolder.class,//Holder class
                owners
        ) {
            @Override
            protected void populateViewHolder(OwnerViewHolder viewHolder, final InstaComponent model, final int position) {
                mUsersDB.child(model.getOwnerId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserModel user = dataSnapshot.getValue(UserModel.class);
                        viewHolder.setOwnerName(user.getName());
                        viewHolder.setOwnerPosition(user.getPosition());
                        viewHolder.setOwnerPic(user.getImageUrl(),SearchResultsActivity.this);
                        viewHolder.setOwnerAvailable(model.getAvailable(),SearchResultsActivity.this);
                        viewHolder.getOwnerPicView().setOnClickListener(v -> {
                            Intent intent =  new Intent(SearchResultsActivity.this,ProfileActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString(Constant.FIREBASE_USER_ID,model.getOwnerId());
                            intent.putExtras(bundle);
                            startActivity(intent);

                        });
                        viewHolder.getAvailableView().setOnClickListener(v -> {
                            //Log.d("CHECKKEY","0 key = " + firebaseRecyclerAdapter.getRef(position).getKey() +" Auth = " + mAuth.getCurrentUser().getUid());
                            if (dataSnapshot.getKey().equals(mAuth.getCurrentUser().getUid())) {
                                Log.d("CHECKKEY","1");
                                Toast.makeText(SearchResultsActivity.this, "it's your component", Toast.LENGTH_SHORT).show();
                            }
                            else if (model.getAvailable()) {
                                createRequestDonation(position, model.getOwnerId());
                            }
                            else
                                Toast.makeText(SearchResultsActivity.this,"this component not available now",Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };
        ownerRecyler.setAdapter(firebaseRecyclerAdapter);
    }

    private void createRequestDonation(int instaKeyPos , String ownerId){
        if (mAuth.getCurrentUser() == null ){
            //TODO : ERROR
            return;
        }
        /*
        if (mAuth.getCurrentUser().getUid().equals(ownerId)){
            //TODO : ERROR
            return;
        }
        */
        if (component.getName() == null){
            //TODO : ERROR
            Log.d("ERROR","NULL COMP");
            return;
        }
        String instaKey = firebaseRecyclerAdapter.getRef(instaKeyPos).getKey();
        FragmentManager fm = getSupportFragmentManager();
        RequestDonationFragment editNameDialogFragment = RequestDonationFragment.newInstance(instaKey,component.getName());
        editNameDialogFragment.show(fm, "fragment_request_donation");
    }
    private void pickImage() {
        mCompDB.child(compKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                component = dataSnapshot.getValue(Component.class);
                setTitle(component.getName());
                downloadImage("https:"+component.getImageUrl());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void downloadImage(String Url) {
        Picasso.with(this).load(Url).resize(300, 300).networkPolicy(NetworkPolicy.OFFLINE).into(compImg, new Callback() {
            @Override
            public void onSuccess() {

            }
            @Override
            public void onError() {
                // if it not available online we need to download it
                Picasso.with(SearchResultsActivity.this).load(Url).into(compImg);
            }
        });
    }

    @OnClick(R.id.comp_fab_add)
    void addComp(){
        if (!Utils.checkInternetConenction(this)){
            //TODO : dialogue
            Log.d("COMPACTIVE","1");
            return;
        }

        if (mAuth.getCurrentUser().getUid() == null) {
            //TODO : dialoge
            Log.d("COMPACTIVE","2");
            return;
        }
        Log.d("COMPACTIVE","3");
        InstaComponent instaComponent = new InstaComponent(compKey,mAuth.getCurrentUser().getUid(),true);
        mInstaComp.push().setValue(instaComponent);
    }
    public static class OwnerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.owner_img) ImageView ownerPicView;
        @BindView(R.id.owner_name) MyTextView ownerNameView;
        @BindView(R.id.owner_position) TextView ownerPositionView;
        @BindView(R.id.owner_available) MyTextView ownerAvailableView;

        public OwnerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
        public void setOwnerPic(String ownerPic , Context ctx) {
            Picasso.with(ctx).load(ownerPic).resize(300, 300).networkPolicy(NetworkPolicy.OFFLINE).into(ownerPicView, new Callback() {
                @Override
                public void onSuccess() {

                }
                @Override
                public void onError() {
                    // if it not available online we need to download it
                    Picasso.with(ctx).load(ownerPic).into(ownerPicView);
                }
            });
        }

        public void setOwnerName(String ownerName) {
            ownerNameView.setText(ownerName);
        }

        public void setOwnerPosition(String ownerPosition) {
            ownerPositionView.setText(ownerPosition);
        }
        public void setOwnerAvailable(Boolean available, Context ctx) {

            if (available){
                Log.d("OWNERLIST",available+"");
                ownerAvailableView.setText("Available");
                ownerAvailableView.setBackground(ctx.getDrawable(R.drawable.available_border));

            }else{
                ownerAvailableView.setText("Not Available");
                ownerAvailableView.setBackground(ctx.getDrawable(R.drawable.round));
            }
        }
        public ImageView getOwnerPicView() {
            return ownerPicView;
        }
        public MyTextView getAvailableView(){
            return ownerAvailableView;
        }
    }

}
