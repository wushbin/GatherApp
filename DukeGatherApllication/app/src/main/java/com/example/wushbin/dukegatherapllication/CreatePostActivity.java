package com.example.wushbin.dukegatherapllication;

import android.app.DatePickerDialog;
import android.content.ClipData;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.wushbin.dukegatherapllication.InGroupActivity.DEFAULT_MSG_LENGTH_LIMIT;

/**
 * Created by Shangxing on 3/3/17.
 */

public class CreatePostActivity extends AppCompatActivity {
    private static final String TAG = "CreateNewPostActivity";
    public static final int DEFAULT_LOCATION_LENGTH_LIMIT = 40;
    private EditText mFromEditText;
    private EditText mToEditText;
    private EditText editDate;
    private EditText editTime;
    private int quantityOfPeoplePost;

    private String mUsername;
    private String mUserEmail;
    private Uri mUserPhotoUri;
    private String mUserUniqID;

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
        mUserUniqID =user.getUid();
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

        mFromEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_LOCATION_LENGTH_LIMIT)});
        mToEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_LOCATION_LENGTH_LIMIT)});
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
            Post newPost = new Post(from, to, leaveTime, leaveDate, quantityOfPeoplePost, mUsername, mUserUniqID);
            Log.v(TAG,newPost.getUserUId());
            postKey = mPostDatabaseReference.push().getKey();
            mPostDatabaseReference.child(postKey).setValue(newPost);
            User postOwner = new User(mUsername, mUserEmail, String.valueOf(mUserPhotoUri),mUserUniqID);
            mPostDatabaseReference.child(postKey).child("User").push().setValue(postOwner);
            NavUtils.navigateUpFromSameTask(CreatePostActivity.this);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_create_post,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create_save:
                createNewPost();
                return true;
            case android.R.id.home:
                Intent intent = new Intent(CreatePostActivity.this, MainActivity.class);
                Intent_Constants.fromSearch = 0;
                startActivity(intent);
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void displayNumber(int number) {
        TextView numberofpeopleTextVuew = (TextView) findViewById(R.id.number_of_people);
        numberofpeopleTextVuew.setText("" + number);
    }
}