package com.sheatouk.selmy.componentsdonationasu.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.sheatouk.selmy.componentsdonationasu.R;
import com.sheatouk.selmy.componentsdonationasu.Util.MyEditText;
import com.sheatouk.selmy.componentsdonationasu.Util.SheamusDialog;
import com.sheatouk.selmy.componentsdonationasu.Util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity {
    @BindView(R.id.email_reg) MyEditText email;
    @BindView(R.id.pass_reg) MyEditText password;
    @BindView(R.id.con_pass_reg) MyEditText passwordConfirm;
    private SheamusDialog dialog;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_new);
        ButterKnife.bind(this);

        mAuth=FirebaseAuth.getInstance();
        dialog = new SheamusDialog(this);
        //dialog.setMessage("wait..");
        dialog.setCancelable(false);
    }
    @OnClick (R.id.sign_up_reg)
    void signUp(){
        String emailSt = email.getText().toString().trim();
        String passSt = password.getText().toString().trim();
        String conPassSt = passwordConfirm.getText().toString().trim();

        if (TextUtils.isEmpty(emailSt) || TextUtils.isEmpty(passSt) || TextUtils.isEmpty(conPassSt)){
            //TODO : dialogue
            Log.d("SignUp","1");
            return;
        }
        if (!passSt.equals(conPassSt)){
            //TODO : dialogue
            Log.d("SignUp","2");
            return;
        }
        if (!Utils.isEmailValid(emailSt)){
            //TODO : dialogue
            Log.d("SignUp","3");
            return;
        }
        if (!Utils.checkInternetConenction(this)){
            //TODO : dialogue
            Log.d("SignUp","4");
            return;
        }
        signUpFirebase(emailSt,passSt);
    }

    private void signUpFirebase(String emailSt, String passSt) {
        //TODO : show dialogue
        dialog.show();
        mAuth.createUserWithEmailAndPassword(emailSt,passSt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<AuthResult> task) {
                if(task.isSuccessful()){
                    // TODO : goto profile setup
                    dialog.dismiss();
                    startActivity(new Intent(SignUpActivity.this,PersonalInfoActivity.class));
                }else{
                    // TODO: dismiss dialogue
                    // TODO: show Error dialogue
                    dialog.dismiss();
                }

            }
        });
    }
    @OnClick (R.id.signin_back)
    void signIn(){
        startActivity(new Intent(this,SignInActivity.class));
    }
}
