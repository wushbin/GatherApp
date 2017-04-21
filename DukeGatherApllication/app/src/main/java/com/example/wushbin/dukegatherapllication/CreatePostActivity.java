package com.example.wushbin.dukegatherapllication;

import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Shangxing on 3/3/17.
 */

public class CreatePostActivity extends AppCompatActivity {
    private static final String TAG = "CreateNewPostActivity";

    private EditText mFromEditText;
    private EditText mToEditText;
    private EditText editDate;
    private EditText editTime;

    private Button mCreateButton;
    private int quantityOfPeoplePost;

    private String mUsername;
    private String mUserEmail;
    private Uri mUserPhotoUri;
    private String postKey;

    private FirebaseDatabase mFirebaseDatabase; // a fire base database instance
    private DatabaseReference mPostDatabaseReference; // a database reference
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        mUsername = user.getDisplayName();
        mUserEmail = user.getEmail();
        mUserPhotoUri = user.getPhotoUrl();

        quantityOfPeoplePost = 1;
        displayNumber(quantityOfPeoplePost);
        Button increaseButton = (Button) findViewById(R.id.button_plus);
        increaseButton.setOnClickListener(new View.OnClickListener() {
                                              public void onClick(View v) {
                                                  quantityOfPeoplePost = quantityOfPeoplePost + 1;
                                                  displayNumber(quantityOfPeoplePost);
                                              }
                                          }
        );
        Button decreaseButton = (Button) findViewById(R.id.button_min);
        decreaseButton.setOnClickListener(new View.OnClickListener() {
                                              public void onClick(View v) {
                                                  quantityOfPeoplePost = quantityOfPeoplePost - 1;
                                                  if (quantityOfPeoplePost <= 0) {
                                                      quantityOfPeoplePost = 1;
                                                  }
                                                  displayNumber(quantityOfPeoplePost);
                                              }
                                          }
        );
        editDate = (EditText) findViewById(R.id.edit_date);
        editDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View view, boolean hasfocus) {
                if (hasfocus) {
                    DialogFragment newFragment = new DatePickerFragment(view);
                    newFragment.show(getSupportFragmentManager(), "datePicker");

                }
            }

        });
        editTime = (EditText) findViewById(R.id.edit_time);
        editTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View view, boolean hasfocus) {
                if (hasfocus) {
                    DialogFragment newFragment = new TimePickerFragment(view);
                    newFragment.show(getSupportFragmentManager(), "timePicker");

                }
            }

        });
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mPostDatabaseReference = mFirebaseDatabase.getReference().child("post");
        mFromEditText = (EditText) findViewById(R.id.post_from);
        mToEditText = (EditText) findViewById(R.id.post_to);
        mCreateButton = (Button) findViewById(R.id.button_create_post);

        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewPost();
            }
        });
    }

    private void createNewPost() {

        String from = mFromEditText.getText().toString().trim();
        String to = mToEditText.getText().toString().trim();
        String leaveTime = editTime.getText().toString().trim();
        String leaveDate = editDate.getText().toString().trim();

        if (from.equals("") || to.equals("") || leaveTime.equals("") || leaveDate.equals("")) {
            Toast.makeText(CreatePostActivity.this, "Please check your input", Toast.LENGTH_SHORT).show();
        } else {
            Log.v("leavet", leaveTime);
            Log.v("leaveD", leaveDate);
            Post newPost = new Post(from, to, leaveTime, leaveDate, quantityOfPeoplePost, mUsername);
            postKey = mPostDatabaseReference.push().getKey();
            mPostDatabaseReference.child(postKey).setValue(newPost);
            User postOwner = new User(mUsername, mUserEmail, String.valueOf(mUserPhotoUri));
            mPostDatabaseReference.child(postKey).child("User").push().setValue(postOwner);
            NavUtils.navigateUpFromSameTask(CreatePostActivity.this);
        }

    }

    private void displayNumber(int number) {
        TextView numberofpeopleTextVuew = (TextView) findViewById(R.id.number_of_people);
        numberofpeopleTextVuew.setText("" + number);
    }
}