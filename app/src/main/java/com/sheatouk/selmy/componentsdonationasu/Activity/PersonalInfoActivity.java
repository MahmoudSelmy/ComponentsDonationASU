package com.sheatouk.selmy.componentsdonationasu.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sheatouk.selmy.componentsdonationasu.POJO.FirebaseLatling;
import com.sheatouk.selmy.componentsdonationasu.POJO.UserModel;
import com.sheatouk.selmy.componentsdonationasu.R;
import com.sheatouk.selmy.componentsdonationasu.Util.Constant;
import com.sheatouk.selmy.componentsdonationasu.Util.MyEditText;
import com.sheatouk.selmy.componentsdonationasu.Util.SheamusDialog;
import com.sheatouk.selmy.componentsdonationasu.Util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalInfoActivity extends AppCompatActivity {
    @BindView(R.id.profile_name) MyEditText name;
    @BindView(R.id.profile_position) MyEditText position;
    @BindView(R.id.profile_location) MyEditText address;
    @BindView(R.id.profile_img) CircleImageView profilePic;

    private LatLng location;
    private FirebaseAuth mAuth;
    private DatabaseReference mdatabase;
    private final int GALLERY_REQUEST = 1;
    private final int PLACE_PICKER_REQUEST = 2;
    private Uri imgUri=null;
    private StorageReference fireStorage;

    private SheamusDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        ButterKnife.bind(this);

        mdatabase= FirebaseDatabase.getInstance().getReference().child(Constant.FIREBASE_USERS_DB_KEY);
        mdatabase.keepSynced(true);
        mAuth=FirebaseAuth.getInstance();
        fireStorage= FirebaseStorage.getInstance().getReference();

        dialog = new SheamusDialog(this);
        //dialog.setMessage("wait..");
        dialog.setCancelable(false);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_REQUEST&&resultCode==RESULT_OK){
            imgUri=data.getData();
            profilePic.setImageURI(imgUri);
        }

        if(requestCode==PLACE_PICKER_REQUEST&&resultCode==RESULT_OK){
            Place place = PlacePicker.getPlace(this, data);
            if (place != null) {
                location = place.getLatLng();
                String addressSt = place.getAddress().toString();
                address.setText(addressSt);
            }
        }
    }
    @OnClick(R.id.profile_img)
    void pickImg(){
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,GALLERY_REQUEST);
    }
    @OnClick(R.id.profile_save_btn)
    void saveProfileInfo(){
        //TODO: show dialogue
        dialog.show();
        String nameSt = name.getText().toString();
        String positionSt = position.getText().toString();
        String addressSt = address.getText().toString();
        if (TextUtils.isEmpty(nameSt) || TextUtils.isEmpty(positionSt) || TextUtils.isEmpty(addressSt) || imgUri == null){
            //TODO : dialogue
            dialog.dismiss();
            return;
        }
        if (!Utils.checkInternetConenction(this)){
            //TODO : dialogue
            dialog.dismiss();
            return;
        }
        if (mAuth.getCurrentUser() == null){
            //TODO : dialogue
            dialog.dismiss();
            return;
        }
        String uid = mAuth.getCurrentUser().getUid();
        createFirebaseUserNode(uid,nameSt,positionSt,addressSt,imgUri,location);
    }

    private void createFirebaseUserNode(final String uid, final String nameSt, final String positionSt, final String addressSt, Uri imgUri, final LatLng location) {
        StorageReference ImgRef=fireStorage.child("UsersProfileImages").child(uid);
        ImgRef.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl=taskSnapshot.getDownloadUrl();
                DatabaseReference newUserFB =mdatabase.child(uid);
                UserModel newUser = new UserModel(nameSt,positionSt,downloadUrl.toString(),addressSt,new FirebaseLatling(location.latitude,location.longitude));
                newUserFB.setValue(newUser);
                //TODO: dismiss dialogue | goto mainActivity
                dialog.dismiss();
                startActivity(new Intent(PersonalInfoActivity.this,MenuItemActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //TODO: Failure Dialogue
                dialog.dismiss();
            }
        });
    }

    @OnClick(R.id.profile_location)
    void pickPlace(){
        Log.d("SignUp","5");
        if (location == null){
            locationPlacesIntent();
        }
    }
    private void locationPlacesIntent() {
        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }
}
