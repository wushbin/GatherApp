package com.example.wushbin.dukegatherapllication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

/**
 * Created by wushbin on 4/15/17.
 */

public class EditPostActivity extends AppCompatActivity {
    private String TAG = "**EditPost**";

    private FirebaseDatabase mFirebaseDatabase; // a fire base database instance
    private DatabaseReference mPostDatabaseReference; // a database reference
    private DatabaseReference mCurrentPostDatabaseReference;
    private EditText mFromEditText;
    private EditText mToEditText;
    private EditText mDateEditText;
    private EditText mTimeEditText;
    private EditText mNumEditText;
    private String postKey;
    private String mUsername;
    private Post currentPost;

    private boolean mPostHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mPostHasChanged = true;
            return false;
        }
    };

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        postKey = getIntent().getStringExtra("postKey");
        Log.v(TAG,postKey);
        mUsername = getIntent().getStringExtra("memberName");

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mPostDatabaseReference = mFirebaseDatabase.getReference().child("post");
        mCurrentPostDatabaseReference = mPostDatabaseReference.child(postKey);
        mFromEditText = (EditText) findViewById(R.id.post_edit_from);
        mToEditText = (EditText) findViewById(R.id.post_edit_to);
        mDateEditText = (EditText) findViewById(R.id.post_edit_date);
        mTimeEditText = (EditText) findViewById(R.id.post_edit_time);
        mNumEditText = (EditText) findViewById(R.id.post_edit_num);

        mFromEditText.setOnTouchListener(mTouchListener);
        mToEditText.setOnTouchListener(mTouchListener);
        mDateEditText.setOnTouchListener(mTouchListener);
        mTimeEditText.setOnTouchListener(mTouchListener);
        mNumEditText.setOnTouchListener(mTouchListener);

        EditText editDate = (EditText)findViewById(R.id.post_edit_date);
        editDate.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View view, boolean hasfocus){
                if(hasfocus){
                    DialogFragment newFragment = new DatePickerFragment(view);
                    newFragment.show(getSupportFragmentManager(), "datePicker");

                }
            }

        });
        EditText editTime = (EditText)findViewById(R.id.post_edit_time);
        editTime.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View view, boolean hasfocus){
                if(hasfocus){
                    DialogFragment newFragment = new TimePickerFragment(view);
                    newFragment.show(getSupportFragmentManager(), "timePicker");

                }
            }

        });

        final ValueEventListener currentPostListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentPost = dataSnapshot.getValue(Post.class);
                Log.v(TAG,currentPost.getFromPlace());
                mFromEditText.setText(currentPost.getFromPlace());
                mToEditText.setText(currentPost.getToPlace());
                mDateEditText.setText(currentPost.getLeaveDate());
                mTimeEditText.setText(currentPost.getLeaveTime());
                mNumEditText.setText(String.valueOf(currentPost.getNumOfPeople()));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mCurrentPostDatabaseReference.addValueEventListener(currentPostListener);

    }

    /**
     * Create Option menun
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_edit_post,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_edit_save:
                savePost();
                return true;
            case android.R.id.home:
                Log.v(TAG,String.valueOf(mPostHasChanged));
                if (!mPostHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditPostActivity.this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(EditPostActivity.this);
                            }
                        };
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void savePost(){
        String fromString = mFromEditText.getText().toString().trim();
        String toString = mToEditText.getText().toString().trim();
        String dateString = mDateEditText.getText().toString().trim();
        String timeString = mTimeEditText.getText().toString().trim();
        String numString = mNumEditText.getText().toString().trim();
        int numOfPeople = Integer.parseInt(numString);
        Post updatedPost = new Post(fromString,toString,timeString,dateString,numOfPeople,mUsername);
        Map<String, Object> postValues = updatedPost.toMap();
        mCurrentPostDatabaseReference.updateChildren(postValues);
        NavUtils.navigateUpFromSameTask(EditPostActivity.this);
    }


    @Override
    public void onBackPressed() {
        if (!mPostHasChanged) {
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }




}
