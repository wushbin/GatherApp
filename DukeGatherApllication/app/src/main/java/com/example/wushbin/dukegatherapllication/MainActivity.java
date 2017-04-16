package com.example.wushbin.dukegatherapllication;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ListView;
import android.support.design.widget.FloatingActionButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String ANONYMOUS = "anonymous";
    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "**/MainActivity/**";

    private String mUsername;
    private String mUserEmail;
    private Uri mUserPhotoUri;
    private FirebaseDatabase mFirebaseDatabase; // a fire base database instance
    private DatabaseReference mPostDatabaseReference; // a database reference
    private ChildEventListener mChildEventListener;
    private ListView mPostListView;
    private ArrayList<User> currentPostMembers = new ArrayList<>();
    private PostAdapter mPostAdapter;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private Post selectedPost;

    private List<Post> postList = new ArrayList<>();
    private String num;

    private String from1;
    private String to1;
    private String time1;
    private String date1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUsername = ANONYMOUS;

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mPostDatabaseReference = mFirebaseDatabase.getReference().child("post");

        mPostListView = (ListView)findViewById(R.id.post_list_View);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        //final List<Post> postList = new ArrayList<>();
        mPostAdapter = new PostAdapter(this,R.layout.post_item, postList);
        mPostListView.setAdapter(mPostAdapter);

        mPostListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPost = postList.get(position);
            }
        });

        mFirebaseAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    // user signed in
                    //mUsername = user.getDisplayName();
                    onSignedInInitialize(user.getDisplayName(),user.getEmail(),user.getPhotoUrl());
                    //Toast.makeText(MainActivity.this, "You're now signed in. Welcome to DukeGather.", Toast.LENGTH_SHORT).show();
                }else{
                    onSignedOutCleanUp();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                                    .build(),
                            RC_SIGN_IN);
                }

            }
        };

        // Setup FAB to open CreateNewPostActivity
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreatePostActivity.class);
                //TODO: pass data the a new intent
                intent.putExtra("userName",mUsername);
                intent.putExtra("userEmail",mUserEmail);
                Log.v(TAG,mUsername);
                startActivityForResult(intent, Intent_Constants.Intent_Post_Code);
            }
        });

        //get requirement from search page
        if (Intent_Constants.fromSearch == 1) {
            Intent intent = getIntent();
            from1 =  intent.getStringExtra("from") ;
            to1 = intent.getStringExtra("to");
            date1 = intent.getStringExtra("date");
            time1 = intent.getStringExtra("time");
            //Toast.makeText(MainActivity.this, num, Toast.LENGTH_SHORT).show();
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            if(resultCode == RESULT_OK){
                //Toast.makeText(MainActivity.this,"Signed in", Toast.LENGTH_SHORT).show();
            }else if (resultCode == RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_mainpage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_search:
                // User chose the "search" item, show the app search UI...
                Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(searchIntent);
                return true;
            case R.id.main_setting:
                Intent settingIntent = new Intent(MainActivity.this,SettingActivity.class);
                startActivity(settingIntent);
                return true;
            case R.id.main_signed_out:
                AuthUI.getInstance().signOut(this);
        }
        return super.onOptionsItemSelected(item);
    }

    private void onSignedInInitialize(String userName, String userEmail, Uri userPhotoUri){
        mUsername = userName;
        mUserEmail = userEmail;
        mUserPhotoUri = userPhotoUri;
        attachDatabaseReadListener();
    }

    private void onSignedOutCleanUp(){
        mUsername = ANONYMOUS;
        mPostAdapter.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
        detachDatabaseReadListener();
        mPostAdapter.clear();

    }

    private void attachDatabaseReadListener(){
        if(mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Post mPost = dataSnapshot.getValue(Post.class);
                    String key = dataSnapshot.getKey();
                    mPost.setKey(key);
                    //Log.v(TAG+"child",key);
                    DataSnapshot membersSnapshot = dataSnapshot.child("User");
                    ArrayList<User> memberList = new ArrayList<>();
                    Iterable<DataSnapshot> membersChildren = membersSnapshot.getChildren();
                    for(DataSnapshot memberChild : membersChildren){
                        User member = memberChild.getValue(User.class);
                        if (member != null) {
                            //Log.v(TAG + "child", member.getUserName());
                            memberList.add(member);
                        }
                    }
                    mPost.setUsers(memberList);
                    if (Intent_Constants.fromSearch == 0) {
                        mPostAdapter.add(mPost);
                    }
                    else {
                        if ( (from1.equals(mPost.getFromPlace() ) || from1.equals("")) && (to1.equals(mPost.getToPlace()) || to1.equals(("")) )&&(date1.equals(mPost.getLeaveDate())||date1.equals(""))&&(time1.equals(mPost.getLeaveTime())||time1.equals("")) ) {
                            mPostAdapter.add(mPost);
                        }
                    }
                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            mPostDatabaseReference.addChildEventListener(mChildEventListener);

        }
    }


    private void detachDatabaseReadListener(){
        if(mChildEventListener != null) {
            mPostDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }
    //show to Join in dialogue
    public void showJoinConfirmationDialog(final Post currentPost, final boolean existStatus) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.join_the_group);
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "join" button, so join the group.
                //TODO: implement the enrollment function
                getIntoInGroupActivity(currentPost,existStatus);

            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue on the main page
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void getIntoInGroupActivity(final Post currentPost,boolean existStatus){
        //Toast.makeText(MainActivity.this,"welcome to join", Toast.LENGTH_SHORT).show();
        Intent groupIntent = new Intent(MainActivity.this,InGroupActivity.class);
        //groupIntent.putExtra("memberName",mUsername);
        //groupIntent.putExtra("memberEmail",mUserEmail);
        groupIntent.putExtra("postKey",currentPost.getKey());
        groupIntent.putExtra("existStatus", existStatus);
        //Log.v(TAG+"toGro",String.valueOf(existStatus));
        //Log.v(TAG,currentPost.getKey());
        startActivity(groupIntent);
    }

    public String getmUsername(){
        return mUsername;
    }

}