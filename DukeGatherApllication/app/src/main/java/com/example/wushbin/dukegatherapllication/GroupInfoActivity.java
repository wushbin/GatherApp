package com.example.wushbin.dukegatherapllication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wushbin on 4/14/17.
 */

public class GroupInfoActivity extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase; // a fire base database instance
    private DatabaseReference mPostDatabaseReference; // a database reference

    private UserAdapter mUserAdapter;
    private ListView mUserListView;

    private String postKey;
    private String TAG = "** GroupInfo **";
    private ChildEventListener mChildEventListener;
    private TextView OwnerNameView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list);
        OwnerNameView = (TextView) findViewById(R.id.group_owner);

        postKey = getIntent().getStringExtra("postKey");

        Log.v(TAG,postKey);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mPostDatabaseReference = mFirebaseDatabase.getReference().child("post").child(postKey);
        mUserListView = (ListView) findViewById(R.id.members_list);
        List<User> users = new ArrayList<>();

        mUserAdapter = new UserAdapter(this,R.layout.user_item,users);
        mUserListView.setAdapter(mUserAdapter);

        ValueEventListener OwnerListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String OwnerName = dataSnapshot.getValue(String.class);
                OwnerNameView.setText(OwnerName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mPostDatabaseReference.child("userName").addValueEventListener(OwnerListener);

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User member = dataSnapshot.getValue(User.class);
                mUserAdapter.add(member);
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
        mPostDatabaseReference.child("User").addChildEventListener(mChildEventListener);
    }
}