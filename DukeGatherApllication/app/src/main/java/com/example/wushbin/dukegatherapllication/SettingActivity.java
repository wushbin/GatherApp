package com.example.wushbin.dukegatherapllication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * Created by wushbin on 3/3/17.
 */

public class SettingActivity extends AppCompatActivity {
    private Spinner mGenderSpinner;
    private int mGender = 0;

    private static final int RC_SIGN_IN = 1;
    private static final int RC_PHOTO_PICKER =  2;
    private StorageReference mChatPhotosStorageReference;
    private FirebaseStorage mFirebaseStorage;

    Uri downloadUrl;
    EditText Name;
    EditText email;
    boolean emailVerified = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Name = (EditText)findViewById(R.id.mname);
        email = (EditText)findViewById(R.id.edit_email);
        mGenderSpinner = (Spinner) findViewById(R.id.spinner_gender);
        String mName="";
        String mEmail = "";

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            Uri userurl = user.getPhotoUrl();
            if(userurl!=null){
                ImageView iconforuser = (ImageView)findViewById(R.id.user_view);
                Glide.with(iconforuser.getContext()).load(userurl).into(iconforuser);
            }
        }

     //   Uri photoUrl;

        if(user!=null){
            mName = user.getDisplayName();
            mEmail = user.getEmail();
            emailVerified = user.isEmailVerified();
       //     photoUrl = user.getPhotoUrl();
        }
        Button verifybutton = (Button)findViewById(R.id.verifyemail);
        if(emailVerified == true){
            verifybutton.setEnabled(false);
            verifybutton.setBackgroundResource(R.color.button_disable);
        }
        verifybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.sendEmailVerification()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SettingActivity.this,"Verification Email Send", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        mFirebaseStorage = FirebaseStorage.getInstance();
        mChatPhotosStorageReference = mFirebaseStorage.getReference().child("chat_photos");
        Button uploadPhoto = (Button)findViewById(R.id.uploadphoto) ;
        uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                startActivityForResult(intent.createChooser(intent,"Complete action using"), RC_PHOTO_PICKER);
            }
        });


        Name.setText(mName);
        email.setText(mEmail);

        setupSpinner();
        mGenderSpinner.setSelection(mGender);
        //displayDatabaseInfo();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK){
            Uri selectedImageUri = data.getData();
            StorageReference photoRef =
                    mChatPhotosStorageReference.child(selectedImageUri.getLastPathSegment());
            // it upload file and add an listener
            photoRef.putFile(selectedImageUri).addOnSuccessListener
                    (this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            downloadUrl = taskSnapshot.getDownloadUrl();
                            Toast.makeText(SettingActivity.this,"photo uploaded", Toast.LENGTH_SHORT).show();

                            ImageView iconforuser = (ImageView)findViewById(R.id.user_view);
                            Glide.with(iconforuser.getContext()).load(downloadUrl).into(iconforuser);


                          //  Message chatMessage = new Message(mUsername, null, downloadUrl.toString());
                          //  mMessagesDatabaseReference.child("message").push().setValue(chatMessage);
                        }
                    });
    }
    }



    private void setupSpinner() {
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);
        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mGenderSpinner.setAdapter(genderSpinnerAdapter);
        // Set the integer mSelected to the constant values
        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_male))) {
                        mGender = 1; // Male
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        mGender = 2; // Female
                    } else {
                        mGender = 0; // Unknown
                    }
                }
            }
            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = 0; // Unknown
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                String sname  = Name.getText().toString();
                String em  = email.getText().toString();

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(sname).setPhotoUri(downloadUrl)
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            }
                        });
                user.updateEmail(em)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            }
                        });

                Toast.makeText(this,"saved",Toast.LENGTH_LONG).show();

                //save the information into the sharePreference
                return true;
            case R.id.discard:
                // discard edition
                finish();
                return true;

            case android.R.id.home:
                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                Intent_Constants.fromSearch = 0;
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
