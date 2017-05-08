package com.sheatouk.selmy.componentsdonationasu.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sheatouk.selmy.componentsdonationasu.POJO.ReqReply;
import com.sheatouk.selmy.componentsdonationasu.R;
import com.sheatouk.selmy.componentsdonationasu.Util.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.grantland.widget.AutofitTextView;

public class RepliesActivity extends AppCompatActivity {
    @BindView(R.id.replies_list)
    RecyclerView repliesList;
    @BindView(R.id.app_bar)
    Toolbar toolbar;
    private DatabaseReference repliesDB;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replies);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle("Replies");
        mAuth=FirebaseAuth.getInstance();
        repliesDB = FirebaseDatabase.getInstance().getReference().child(Constant.FIREBASE_REPLIES_DB_KEY).child(mAuth.getCurrentUser().getUid());
        repliesDB.keepSynced(true);
    }

    @Override
    protected void onStart() {
        initRecycler();
        super.onStart();
    }

    private void initRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        repliesList.setLayoutManager(layoutManager);
        FirebaseRecyclerAdapter<ReqReply,ReplyViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ReqReply, ReplyViewHolder>(
                ReqReply.class,
                R.layout.reply_row,
                ReplyViewHolder.class,
                repliesDB
        ) {
            @Override
            protected void populateViewHolder(ReplyViewHolder viewHolder, ReqReply model, int position) {
                String msg = model.getDonatorName() + " ";
                if (model.getAccepted())
                    msg += "accepted";
                else
                    msg += "rejected";
                msg += " your request to take " + model.getProductName();
                viewHolder.replyT.setText(msg);
                viewHolder.timeTxt.setReferenceTime(-1 * model.getTime());
            }
        };
        repliesList.setAdapter(firebaseRecyclerAdapter);
    }
    /*
    public static class ReplyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.reply_txt) MyTextView replyT;
        public ReplyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }*/
    public static class ReplyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.reply_time)
        RelativeTimeTextView timeTxt;
        @BindView(R.id.reply_txt)
        AutofitTextView replyT;

        public ReplyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }

}
