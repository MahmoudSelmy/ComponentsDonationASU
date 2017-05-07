package com.sheatouk.selmy.componentsdonationasu.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sheatouk.selmy.componentsdonationasu.Activity.reqListActivity;
import com.sheatouk.selmy.componentsdonationasu.POJO.Donation;
import com.sheatouk.selmy.componentsdonationasu.POJO.ReqDonation;
import com.sheatouk.selmy.componentsdonationasu.R;
import com.sheatouk.selmy.componentsdonationasu.Util.Constant;

public class DonationReqListner extends Service {
    private DatabaseReference donationsDB,reqDonationsDB;
    private FirebaseAuth mAuth;
    public DonationReqListner() {

    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAuth=FirebaseAuth.getInstance();
        reqDonationsDB = FirebaseDatabase.getInstance().getReference().child(Constant.FIREBASE_REQ_DONATIONS_DB_KEY).child(mAuth.getCurrentUser().getUid());
        donationsDB = FirebaseDatabase.getInstance().getReference().child(Constant.FIREBASE_DONATIONS_DB_KEY);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO : save notfied req keys
        reqDonationsDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ReqDonation donation = dataSnapshot.getValue(ReqDonation.class);
                donationsDB.child(donation.getReqId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String key = dataSnapshot.getKey();
                        notifyUser(dataSnapshot.getValue(Donation.class),key);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return START_STICKY;
    }

    private void notifyUser(Donation donation , String key) {
        if (donation == null)
            return;
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(getApplicationContext(), reqListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Log.d("NotificationLis",key);
        // TODO : setContent
        Notification.Builder n  = new Notification.Builder(this)
                .setContentTitle("Donation Request")
                .setSmallIcon(R.drawable.led_icon)
                .setContentIntent(PendingIntent.getActivity(
                        this,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT))
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setAutoCancel(true);
        notificationManager.notify((int)donation.getTime()/1000, n.build());
    }
}
