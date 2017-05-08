package com.sheatouk.selmy.componentsdonationasu.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sheatouk.selmy.componentsdonationasu.POJO.Component;
import com.sheatouk.selmy.componentsdonationasu.POJO.InstaComponent;
import com.sheatouk.selmy.componentsdonationasu.POJO.UserModel;
import com.sheatouk.selmy.componentsdonationasu.R;
import com.sheatouk.selmy.componentsdonationasu.Util.Constant;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.grantland.widget.AutofitTextView;

public class ProfileActivity extends AppCompatActivity {
    @BindView(R.id.profile_Image_actvity)
    ImageView profilePicV;
    @BindView(R.id.profile_Name_activity)
    TextView profileNameV;
    @BindView(R.id.profile_Position_Activity)
    TextView profilePositionV;
    @BindView(R.id.profile_Address_Activity)
    AutofitTextView profileAddressV;
    @BindView(R.id.recyclerView)
    RecyclerView compsList;
    private String userId;
    private DatabaseReference instaCompDB;
    private FirebaseAuth mAuth;
    private Query query;
    private FirebaseRecyclerAdapter<InstaComponent,CompViewHolder> firebaseRecyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        if (getIntent().getExtras() != null){
            userId = getIntent().getExtras().getString(Constant.FIREBASE_USER_ID);
            populateUserData(userId);
        }
        mAuth=FirebaseAuth.getInstance();
        instaCompDB = FirebaseDatabase.getInstance().getReference().child(Constant.FIREBASE_INSTA_COMPONENTS_DB_KEY);
        query = instaCompDB.orderByChild("ownerId").equalTo(userId);
        query.keepSynced(true);
    }

    private void populateUserData(String userId) {
        DatabaseReference mUsers = FirebaseDatabase.getInstance().getReference().child(Constant.FIREBASE_USERS_DB_KEY);
        mUsers.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserModel user = dataSnapshot.getValue(UserModel.class);
                profileNameV.setText(user.getName());
                profileAddressV.setText(user.getAddress());
                profilePositionV.setText(user.getPosition());
                Picasso.with(ProfileActivity.this).load(user.getImageUrl()).resize(300, 300).networkPolicy(NetworkPolicy.OFFLINE).into(profilePicV, new Callback() {
                    @Override
                    public void onSuccess() {

                    }
                    @Override
                    public void onError() {
                        // if it not available online we need to download it
                        Picasso.with(ProfileActivity.this).load(user.getImageUrl()).into(profilePicV);
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        initRecycler();
    }

    private void initRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        compsList.setLayoutManager(layoutManager);
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<InstaComponent, CompViewHolder>(
                InstaComponent.class,
                R.layout.component_row,
                CompViewHolder.class,
                query
        ) {
            @Override
            protected void populateViewHolder(CompViewHolder viewHolder, InstaComponent model, int position) {
                viewHolder.pickCompData(model.getProductId(),ProfileActivity.this);
                if (model.getAvailable())
                    viewHolder.compAvailable.setText("Available");
                else
                    viewHolder.compAvailable.setText("Not Available");
                viewHolder.compName.setOnLongClickListener(v -> {
                    if (model.getOwnerId().equals(mAuth.getCurrentUser().getUid())) {
                        model.setAvailable(!model.getAvailable());
                        firebaseRecyclerAdapter.getRef(position).setValue(model);
                    }
                    return true;
                });
            }
        };
        compsList.setAdapter(firebaseRecyclerAdapter);
    }
    public static class CompViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.row_comp_name)
        AutofitTextView compName;
        @BindView(R.id.row_comp_available)
        TextView compAvailable;
        @BindView(R.id.row_comp_image)
        ImageView compImage;
        private DatabaseReference mCompDB;
        public CompViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            mCompDB = FirebaseDatabase.getInstance().getReference().child(Constant.FIREBASE_COMPONENTS_DB_KEY);
        }
        public void pickCompData(String compKey, Context ctx) {
            mCompDB.child(compKey).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Component component = dataSnapshot.getValue(Component.class);
                    compName.setText(component.getName());
                    downloadImage("https:"+component.getImageUrl(),ctx);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        private void downloadImage(String Url, Context ctx) {
            Picasso.with(ctx).load(Url).resize(300, 300).networkPolicy(NetworkPolicy.OFFLINE).into(compImage, new Callback() {
                @Override
                public void onSuccess() {

                }
                @Override
                public void onError() {
                    // if it not available online we need to download it
                    Picasso.with(ctx).load(Url).into(compImage);
                }
            });
        }
    }
}
