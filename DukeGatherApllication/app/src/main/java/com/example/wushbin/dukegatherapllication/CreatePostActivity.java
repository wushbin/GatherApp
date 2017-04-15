package com.example.wushbin.dukegatherapllication;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Shangxing on 3/3/17.
 */

public class CreatePostActivity extends AppCompatActivity {
    public static final String ANONYMOUS = "anonymous";
    private static final String TAG = "CreateNewPostActivity";

    private EditText mFromEditText;
    private EditText mToEditText;
    private EditText mTimeText;
    private Button mCreateButton;
    private TimePicker mTimePicker;
    private DatePicker mDatePicker;
    private Calendar calendar= Calendar.getInstance();;
    private int quantityOfPeoplePost;
    private int hour;
    private int min;
    private int month;
    private int day;

    private String mUsername;

    private FirebaseDatabase mFirebaseDatabase; // a fire base database instance
    private DatabaseReference mPostDatabaseReference; // a database reference


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        mUsername = ANONYMOUS;
        mUsername = getIntent().getStringExtra("userName");
        quantityOfPeoplePost = 1;
        display(quantityOfPeoplePost);

        Button increaseButton = (Button)findViewById(R.id.button_plus);
        increaseButton.setOnClickListener(new View.OnClickListener(){
                                              public void onClick(View v){
                                                  quantityOfPeoplePost = quantityOfPeoplePost + 1;
                                                  display(quantityOfPeoplePost);
                                              }
                                          }
        );
        Button decreaseButton = (Button)findViewById(R.id.button_min);
        decreaseButton.setOnClickListener(new View.OnClickListener(){
                                              public void onClick(View v){
                                                  quantityOfPeoplePost = quantityOfPeoplePost - 1;
                                                  if (quantityOfPeoplePost <= 0){
                                                      quantityOfPeoplePost = 1;
                                                  }
                                                  display(quantityOfPeoplePost);
                                              }
                                          }
        );
        EditText editDate = (EditText)findViewById(R.id.edit_date);
        editDate.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View view, boolean hasfocus){
                if(hasfocus){
                    DialogFragment newFragment = new DatePickerFragment(view);
                    newFragment.show(getSupportFragmentManager(), "datePicker");

                }
            }

        });
        ///Shengbin Wu
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mPostDatabaseReference = mFirebaseDatabase.getReference().child("post");



        mFromEditText = (EditText) findViewById(R.id.post_from);
        mToEditText = (EditText) findViewById(R.id.post_to);
        //mTimeText = (EditText) findViewById(R.id.post_time);
        mCreateButton = (Button) findViewById(R.id.button_create_post);
      mTimePicker = (TimePicker) findViewById(R.id.time_picker);
      mTimePicker.setIs24HourView(true);


       hour = calendar.get(Calendar.HOUR_OF_DAY);
        min = calendar.get(Calendar.MINUTE);
       final String leaveDate = editDate.getText().toString();
        //TODO:should set the Time Picker to show the current time
        //TODO:should change the time picker as dialogue
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String from = mFromEditText.getText().toString();
                String to = mToEditText.getText().toString();
             if (Build.VERSION.SDK_INT >= 23 ) {
                  hour = mTimePicker.getHour();
                    min = mTimePicker.getMinute();


               }
              else {
                   hour = mTimePicker.getCurrentHour();
                   min = mTimePicker.getCurrentMinute();
                }
                String leaveTime = hour + ":" + min;

                Log.v(TAG, leaveTime);
                Post newPost = new Post(from, to,leaveTime,leaveDate,quantityOfPeoplePost,mUsername);
                mPostDatabaseReference.push().setValue(newPost);
                Toast.makeText(CreatePostActivity.this,"Posted", Toast.LENGTH_SHORT).show();
            }
        });





    }


    private void display(int number){
        TextView numberofpeopleTextVuew = (TextView)findViewById(R.id.number_of_people);
        numberofpeopleTextVuew.setText(""+number);
    }





}
