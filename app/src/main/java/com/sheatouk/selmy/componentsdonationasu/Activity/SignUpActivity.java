package com.sheatouk.selmy.componentsdonationasu.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.sheatouk.selmy.componentsdonationasu.R;
import com.sheatouk.selmy.componentsdonationasu.Util.MyEditText;
import com.sheatouk.selmy.componentsdonationasu.Util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity {
    @BindView(R.id.email_reg) MyEditText email;
    @BindView(R.id.pass_reg) MyEditText password;
    @BindView(R.id.con_pass_reg) MyEditText passwordConfirm;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        mAuth=FirebaseAuth.getInstance();
    }
    @OnClick (R.id.sign_up_reg)
    void signUp(){
        String emailSt = email.getText().toString().trim();
        String passSt = password.getText().toString().trim();
        String conPassSt = passwordConfirm.getText().toString().trim();

        if (TextUtils.isEmpty(emailSt) || TextUtils.isEmpty(passSt) || TextUtils.isEmpty(conPassSt)){
            //TODO : dialogue
            return;
        }
        if (!emailSt.equals(conPassSt)){
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
        signUpFirebase(emailSt,passSt);
    }

    private void signUpFirebase(String emailSt, String passSt) {
        //TODO : show dialogue
        mAuth.createUserWithEmailAndPassword(emailSt,passSt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<AuthResult> task) {
                if(task.isSuccessful()){
                    // TODO: dismiss dialogue
                    String userId=mAuth.getCurrentUser().getUid();
                    // TODO : goto profile setup

                }else{
                    // TODO: dismiss dialogue
                    // TODO: show Error dialogue

                }

            }
        });
    }
}
