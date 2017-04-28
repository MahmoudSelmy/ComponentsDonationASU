package com.sheatouk.selmy.componentsdonationasu.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sheatouk.selmy.componentsdonationasu.R;
import com.sheatouk.selmy.componentsdonationasu.Util.Constant;
import com.sheatouk.selmy.componentsdonationasu.Util.MyEditText;
import com.sheatouk.selmy.componentsdonationasu.Util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInActivity extends AppCompatActivity {
    @BindView(R.id.email_sign) MyEditText email;
    @BindView(R.id.pass_sign) MyEditText password;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private DatabaseReference mdatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);

        mdatabase= FirebaseDatabase.getInstance().getReference().child(Constant.FIREBASE_USERS_DB_KEY);
        mdatabase.keepSynced(true);
        mAuth=FirebaseAuth.getInstance();
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            //triggered after Authacation
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (mAuth.getCurrentUser() != null) {
                    validateUserNode(mAuth.getCurrentUser().getUid());
                }else {
                    //TODO: Error Dialogue
                }
            }
        };

    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);

    }

    private void validateUserNode(final String uid) {
        mdatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(uid)){
                    //TODO: goto profile
                }else{
                    //TODO: goto setup
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @OnClick(R.id.btn_sign)
    void signIn(){
        String emailSt = email.getText().toString().trim();
        String passSt = password.getText().toString().trim();
        if (TextUtils.isEmpty(emailSt) || TextUtils.isEmpty(passSt)){
            //TODO : dialogue
            return;
        }
        if (!Utils.isEmailValid(emailSt)){
            //TODO : dialogue
            return;
        }
        if (!Utils.checkInternetConenction(this)){
            //TODO : dialogue
            return;
        }
        signInFirebase(emailSt,passSt);
    }

    private void signInFirebase(String emailSt, String passSt) {
        mAuth.signInWithEmailAndPassword(emailSt,passSt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    //TODO : Error Dialogue
                }

            }
        });
    }
}
