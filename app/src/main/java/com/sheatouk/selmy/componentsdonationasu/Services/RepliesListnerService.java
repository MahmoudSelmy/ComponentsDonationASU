package com.sheatouk.selmy.componentsdonationasu.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sheatouk.selmy.componentsdonationasu.Activity.RepliesActivity;
import com.sheatouk.selmy.componentsdonationasu.POJO.ReqReply;
import com.sheatouk.selmy.componentsdonationasu.R;
import com.sheatouk.selmy.componentsdonationasu.Util.Constant;

public class RepliesListnerService extends Service {
    private DatabaseReference repliesDB;
    private FirebaseAuth mAuth;
    public RepliesListnerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAuth= FirebaseAuth.getInstance();
        repliesDB = FirebaseDatabase.getInstance().getReference().child(Constant.FIREBASE_REPLIES_DB_KEY).child(mAuth.getCurrentUser().getUid());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        repliesDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot1, String s) {
                dataSnapshot1.getRef().removeEventListener(this);
                ReqReply reqReply = dataSnapshot1.getValue(ReqReply.class);
                if (!reqReply.getSeen()) {
                    notifyUser(reqReply);
                    reqReply.setSeen(true);
                    dataSnapshot1.getRef().setValue(reqReply);
                }

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

    private void notifyUser(ReqReply reqReply) {
        String titleMsg = reqReply.getDonatorName() + " ";
        if (reqReply.getAccepted())
            titleMsg += "Accepted";
        else
            titleMsg += "rejected";
        titleMsg += " your request";
        Intent intent = new Intent(getApplicationContext(), RepliesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder n  = new Notification.Builder(this)
                .setContentTitle(reqReply.getProductName())
                .setContentText(titleMsg)
                .setSmallIcon(R.drawable.led_icon)
                .setContentIntent(PendingIntent.getActivity(
                        this,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT))
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setAutoCancel(true);
        notificationManager.notify((int)reqReply.getTime()*-1/10000, n.build());
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
