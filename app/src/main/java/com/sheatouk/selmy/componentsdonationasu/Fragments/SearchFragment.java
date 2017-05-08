package com.sheatouk.selmy.componentsdonationasu.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.sheatouk.selmy.componentsdonationasu.POJO.Date;
import com.sheatouk.selmy.componentsdonationasu.POJO.Donation;
import com.sheatouk.selmy.componentsdonationasu.R;
import com.sheatouk.selmy.componentsdonationasu.Util.Constant;
import com.sheatouk.selmy.componentsdonationasu.Util.MyTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    private int Flag ;
    private DatabaseReference donationDB;
    private FirebaseAuth mAuth;
    private Query query;
    private RecyclerView recyclerView;
    public SearchFragment() {
    }

    public SearchFragment(int installed) {
        Flag = installed;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String[] strings = {getString(R.string.in_string), getString(R.string.out_string)};
        mAuth=FirebaseAuth.getInstance();
        donationDB = FirebaseDatabase.getInstance().getReference().child(Constant.FIREBASE_DONATIONS_DB_KEY);
        if (getResources().getString(Flag).equals(strings[1])){
            query = donationDB.orderByChild("reqUserId").equalTo(mAuth.getCurrentUser().getUid());
        }else {
            query = donationDB.orderByChild("donatorId").equalTo(mAuth.getCurrentUser().getUid());
        }
        query.keepSynced(true);
        recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        return recyclerView;
    }

    @Override
    public void onStart() {
        super.onStart();
        initRecycler();
    }

    private void initRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        FirebaseRecyclerAdapter<Donation,ReqViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Donation, ReqViewHolder>(
                Donation.class,
                R.layout.donation_row,
                ReqViewHolder.class,
                query
        ) {
            @Override
            protected void populateViewHolder(ReqViewHolder viewHolder, Donation model, int position) {
                viewHolder.donatorNameT.setText(model.getDonatorName());
                viewHolder.reqUserNameT.setText(model.getReqUserName());
                viewHolder.compNameT.setText(model.getProductName());
                viewHolder.setReqTo(model.getEnd());
                viewHolder.setReqFrom(model.getStart());
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
    public static class ReqViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.donation_donator_name) MyTextView donatorNameT;
        @BindView(R.id.donation_req_user) TextView reqUserNameT;
        @BindView(R.id.donation_comp) MyTextView compNameT;
        @BindView(R.id.donation_to) MyTextView reqToT;
        @BindView(R.id.donation_from) TextView reqFromT;
        public ReqViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setReqFrom(Date date) {
            reqFromT.setText(date.getDay()+" - "+date.getMonth()+" - "+date.getYear());
        }

        public void setReqTo(Date date) {
            reqToT.setText(date.getDay()+" - "+date.getMonth()+" - "+date.getYear());
        }
    }

}
