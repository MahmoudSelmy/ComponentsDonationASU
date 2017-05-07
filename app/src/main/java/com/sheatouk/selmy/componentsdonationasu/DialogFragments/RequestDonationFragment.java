package com.sheatouk.selmy.componentsdonationasu.DialogFragments;


import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sheatouk.selmy.componentsdonationasu.POJO.Date;
import com.sheatouk.selmy.componentsdonationasu.POJO.Donation;
import com.sheatouk.selmy.componentsdonationasu.POJO.FirebaseLatling;
import com.sheatouk.selmy.componentsdonationasu.POJO.InstaComponent;
import com.sheatouk.selmy.componentsdonationasu.POJO.ReqDonation;
import com.sheatouk.selmy.componentsdonationasu.POJO.UserModel;
import com.sheatouk.selmy.componentsdonationasu.R;
import com.sheatouk.selmy.componentsdonationasu.Util.Constant;
import com.sheatouk.selmy.componentsdonationasu.Util.Utils;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.grantland.widget.AutofitTextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestDonationFragment extends DialogFragment {
    @BindView(R.id.request_comp) TextView compNameView;
    @BindView(R.id.request_comp_distance) TextView distanceView;
    @BindView(R.id.request_caller_name)
    AutofitTextView userNameView;
    @BindView(R.id.request_donator_name) AutofitTextView donatorView;
    @BindView(R.id.request_due_date) TextView dueDateView;
    private Date startD,endD;
    private String key,compName;
    private FirebaseAuth mAuth;
    private UserModel donator = null,currentUser = null;
    private InstaComponent instaComponent;
    public RequestDonationFragment() {
        // Required empty public constructor
    }
    public static RequestDonationFragment newInstance(String key,String productName) {
        RequestDonationFragment frag = new RequestDonationFragment();
        Bundle args = new Bundle();
        args.putString(Constant.FIREBASE__KEY, key);
        args.putString("PN", productName);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.content_donation_request, container);
        ButterKnife.bind(this,view);
        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Fetch arguments from bundle and set title
        key = getArguments().getString(Constant.FIREBASE__KEY);
        compName = getArguments().getString("PN");
        mAuth = FirebaseAuth.getInstance();
        initData();
        // Show soft keyboard automatically and request focus to field
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);
        // mEditText.requestFocus();
        //compNameView.setText(key);
    }

    @OnClick(R.id.request_due_date)
    void pickDueDate(){
        int mYear,mMonth,mDay;
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        startD = new Date(mDay,mMonth+1,mYear);
        DatePickerDialog datePicker = new DatePickerDialog(getActivity() ,R.style.DialogTheme,((view, year, month, dayOfMonth) -> {
            dueDateView.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
            endD = new Date(dayOfMonth,month+1,year);
        }),mYear, mMonth, mDay);
        datePicker.show();
    }
    @OnClick(R.id.request_confirm_btn)
    void createRequestDonationNode(){
        if (!Utils.checkInternetConenction(getActivity())){
            //TODO : dialogue
            return;
        }
        if (mAuth.getCurrentUser() == null){
            //TODO : ERROR
            return;
        }
        if (startD == null || endD == null){
            //TODO : ERROR
            return;
        }
        Donation donation = new Donation(instaComponent.getOwnerId(),donator.getName(),mAuth.getCurrentUser().getUid(),currentUser.getName(),key,compName,startD,endD,-1*System.currentTimeMillis(),true);
        DatabaseReference donationDB = FirebaseDatabase.getInstance().getReference().child(Constant.FIREBASE_DONATIONS_DB_KEY).push();
        String donationKey = donationDB.getKey();
        donationDB.setValue(donation);
        DatabaseReference requestDB = FirebaseDatabase.getInstance().getReference().child(Constant.FIREBASE_REQ_DONATIONS_DB_KEY).child(mAuth.getCurrentUser().getUid());
        ReqDonation reqDonation = new ReqDonation(donationKey,-1* System.currentTimeMillis());
        requestDB.push().setValue(reqDonation);
        dismiss();
    }
    private void initData(){
        DatabaseReference instCompDB = FirebaseDatabase.getInstance().getReference().child(Constant.FIREBASE_INSTA_COMPONENTS_DB_KEY);
        instCompDB.keepSynced(true);
        instCompDB.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                instaComponent = dataSnapshot.getValue(InstaComponent.class);
                getData(instaComponent.getOwnerId());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    // getData from owners recycler
    private void getData(String instaCompOwner) {
        DatabaseReference usersDB = FirebaseDatabase.getInstance().getReference().child(Constant.FIREBASE_USERS_DB_KEY);
        usersDB.child(instaCompOwner).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (donator == null) {
                    donator = dataSnapshot.getValue(UserModel.class);
                    getData(mAuth.getCurrentUser().getUid());
                    return;
                }
                currentUser =  dataSnapshot.getValue(UserModel.class);
                populateCard();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void populateCard() {
        compNameView.setText(compName);
        userNameView.setText(currentUser.getName());
        donatorView.setText(donator.getName());
        distanceView.setText(distanceTo(donator.getLocation(),currentUser.getLocation())+"Km");
    }
    public static int distanceTo(FirebaseLatling latLng1, FirebaseLatling latLng2){
        Location location = new Location("P0");
        location.setLatitude(latLng1.getLatitude());
        location.setLongitude(latLng1.getLongitude());
        Location location1 = new Location("P1");
        location1.setLatitude(latLng2.getLatitude());
        location1.setLongitude(latLng2.getLongitude());
        return (int) location1.distanceTo(location)/1000;
    }
}
