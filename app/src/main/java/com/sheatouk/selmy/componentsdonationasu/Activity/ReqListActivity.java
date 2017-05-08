package com.sheatouk.selmy.componentsdonationasu.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sheatouk.selmy.componentsdonationasu.POJO.Date;
import com.sheatouk.selmy.componentsdonationasu.POJO.Donation;
import com.sheatouk.selmy.componentsdonationasu.POJO.InstaComponent;
import com.sheatouk.selmy.componentsdonationasu.POJO.ReqDonation;
import com.sheatouk.selmy.componentsdonationasu.POJO.ReqReply;
import com.sheatouk.selmy.componentsdonationasu.R;
import com.sheatouk.selmy.componentsdonationasu.Util.Constant;
import com.sheatouk.selmy.componentsdonationasu.Util.MyTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReqListActivity extends AppCompatActivity {
    @BindView(R.id.req_list) RecyclerView reqList;
    @BindView(R.id.app_bar) Toolbar toolbar;
    private DatabaseReference mDonationDB;
    private FirebaseAuth mAuth;
    private DatabaseReference mReqDonations,repliesDB;
    private FirebaseRecyclerAdapter<ReqDonation,ReqViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_req_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle("Requests");
        mAuth=FirebaseAuth.getInstance();
        mDonationDB = FirebaseDatabase.getInstance().getReference().child(Constant.FIREBASE_DONATIONS_DB_KEY);
        mDonationDB.keepSynced(true);
        repliesDB = FirebaseDatabase.getInstance().getReference().child(Constant.FIREBASE_REPLIES_DB_KEY);
        repliesDB.keepSynced(true);
        mReqDonations = FirebaseDatabase.getInstance().getReference().child(Constant.FIREBASE_REQ_DONATIONS_DB_KEY).child(mAuth.getCurrentUser().getUid());
        mReqDonations.keepSynced(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initRecycler();
    }

    private void initRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        reqList.setLayoutManager(layoutManager);
        firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<ReqDonation, ReqViewHolder>(
                ReqDonation.class,//PoJo
                R.layout.req_swipe_row,//row view
                ReqViewHolder.class,//Holder class
                mReqDonations
        ) {
            @Override
            protected void populateViewHolder(ReqViewHolder viewHolder, final ReqDonation model, final int position) {
                mDonationDB.child(model.getReqId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Donation donation = dataSnapshot.getValue(Donation.class);
                        if (donation == null)
                            return;
                        String keyDonation = dataSnapshot.getKey();
                        // populateRow(viewHolder,donation,keyDonation,position);
                        viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, viewHolder.swipeLayout.findViewById(R.id.bottom_wrapper));
                        viewHolder.rejectT.setOnClickListener(v -> {
                            //TODO : send rejection
                            ReqReply reply = new ReqReply(donation.getProductName(),donation.getDonatorName(),-1*System.currentTimeMillis(),false,false);
                            DatabaseReference box = FirebaseDatabase.getInstance().getReference().child(Constant.FIREBASE_REPLIES_DB_KEY).child(donation.getReqUserId());
                            box.push().setValue(reply);
                            mDonationDB.child(keyDonation).removeValue();
                            firebaseRecyclerAdapter.getRef(position).removeValue();
                        });
                        viewHolder.acceptT.setOnClickListener(v -> {
                            donation.setWaiting(false);
                            mDonationDB.child(keyDonation).setValue(donation);
                            DatabaseReference box = FirebaseDatabase.getInstance().getReference().child(Constant.FIREBASE_REPLIES_DB_KEY).child(donation.getReqUserId());
                            ReqReply reply = new ReqReply(donation.getProductName(),donation.getDonatorName(),-1*System.currentTimeMillis(),true,false);
                            //String keyReply = box.push().getKey();
                            box.push().setValue(reply);
                            DatabaseReference compDB =  FirebaseDatabase.getInstance().getReference().child(Constant.FIREBASE_INSTA_COMPONENTS_DB_KEY);
                            compDB.child(donation.getProductId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    InstaComponent instaComp = dataSnapshot.getValue(InstaComponent.class);
                                    instaComp.setAvailable(false);
                                    compDB.child(dataSnapshot.getKey()).setValue(instaComp);

                                    firebaseRecyclerAdapter.getRef(position).removeValue();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        });
                        viewHolder.compName.setText(donation.getProductName());
                        viewHolder.compName.setOnClickListener(v -> {
                            Intent intent = new Intent(ReqListActivity.this,ProfileActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString(Constant.FIREBASE__KEY,donation.getReqUserId());
                            startActivity(intent);
                        });
                        viewHolder.reqUserName.setText(donation.getReqUserName());
                        viewHolder.reqUserName.setOnClickListener(v -> {
                            Intent intent =  new Intent(ReqListActivity.this,ProfileActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString(Constant.FIREBASE_USER_ID,donation.getReqUserId());
                            intent.putExtras(bundle);
                            startActivity(intent);
                        });
                        viewHolder.setReqFrom(donation.getStart());
                        viewHolder.setReqTo(donation.getEnd());

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };
        reqList.setAdapter(firebaseRecyclerAdapter);
    }


    public static class ReqViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.req_swipe) SwipeLayout swipeLayout;
        @BindView(R.id.req_user) MyTextView reqUserName;
        @BindView(R.id.req_comp) TextView compName;
        @BindView(R.id.req_from) MyTextView reqFrom;
        @BindView(R.id.req_to) MyTextView reqTo;
        @BindView(R.id.req_accept) TextView acceptT;
        @BindView(R.id.req_reject) TextView rejectT;

        public ReqViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setReqFrom(Date date) {
            reqFrom.setText(date.getDay()+" - "+date.getMonth()+" - "+date.getYear());
        }

        public void setReqTo(Date date) {
            reqTo.setText(date.getDay()+" - "+date.getMonth()+" - "+date.getYear());
        }
    }

}
