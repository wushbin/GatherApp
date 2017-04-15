package com.example.wushbin.dukegatherapllication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wushbin on 3/2/17.
 */

public class InGroupActivity extends AppCompatActivity{

    public static final String ANONYMOUS = "anonymous";
    private static final String TAG = "InGroupActivity";


    public static final int DEFAULT_MSG_LENGTH_LIMIT = 140;
    private static final int RC_SIGN_IN = 1;
    private static final int RC_PHOTO_PICKER =  2;
    private ListView mMessageListView;
    private MessageAdapter mMessageAdapter;
    private EditText mMessageEditText;
    private Button mSendButton;
    private ImageButton mPhotoPickerButton;

    private boolean exitStatus;
    private String postKey;
    private String mUsername;
    private String mUserEmail;
    private FirebaseDatabase mFirebaseDatabase; // a fire base database instance
    private DatabaseReference mMessagesDatabaseReference; // a database reference
    private ChildEventListener mChildEventListener;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mChatPhotosStorageReference;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;


    // the onCreate to initialize this interface
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_list);
        mUsername = ANONYMOUS;
        mUsername = getIntent().getStringExtra("memberName");
        mUserEmail = getIntent().getStringExtra("memberEmail");
        postKey = getIntent().getStringExtra("postKey");
        exitStatus = getIntent().getExtras().getBoolean("existStatus");
        Log.v(TAG+"toGro",String.valueOf(exitStatus));
        //Toast.makeText(InGroupActivity.this,mUsername+"Signed in", Toast.LENGTH_SHORT).show();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        //mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("post").child(postKey).child("message");
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("post").child(postKey);


        if(exitStatus == false){
            Log.v(TAG+"toGro",String.valueOf(exitStatus));
            Log.v(TAG+"toGro",String.valueOf(postKey));
            User currentUser = new User(mUsername,mUserEmail);
            Log.v(TAG+"toGro",currentUser.getUserName());
            String currentUserKey = mMessagesDatabaseReference.child("User").push().getKey();
            Log.v(TAG+"toGro",currentUserKey);
            mMessagesDatabaseReference.child("User").child(currentUserKey).setValue(currentUser);
        }
        mFirebaseStorage = FirebaseStorage.getInstance();
        mChatPhotosStorageReference = mFirebaseStorage.getReference().child("chat_photos");
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mMessageListView = (ListView) findViewById(R.id.message_list);
        mPhotoPickerButton = (ImageButton) findViewById(R.id.photoPickerButton);
        mSendButton = (Button) findViewById(R.id.message_send);
        mMessageEditText = (EditText) findViewById(R.id.message_content);

        List<Message> messages = new ArrayList<>();

        mMessageAdapter = new MessageAdapter(this, R.layout.message_item, messages);
        mMessageListView.setAdapter(mMessageAdapter);
        // ImagePickerButton
        mPhotoPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                startActivityForResult(intent.createChooser(intent,"Complete action using"), RC_PHOTO_PICKER);
            }
        });
        // Enable Send button
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});

        // Send button sends a message and clears the EditText
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Clear input box
                Message chatMessage = new Message(mUsername,mMessageEditText.getText().toString(), null);
                mMessagesDatabaseReference.child("message").push().setValue(chatMessage);
                mMessageEditText.setText("");
            }
        });

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message chatMessage = dataSnapshot.getValue(Message.class);
                mMessageAdapter.add(chatMessage);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };

        mMessagesDatabaseReference.child("message").addChildEventListener(mChildEventListener);
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
                            @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            Toast.makeText(InGroupActivity.this,"photo uploaded", Toast.LENGTH_SHORT).show();
                            Log.v("photo message","photo uploading");
                            Message chatMessage = new Message(mUsername, null, downloadUrl.toString());
                            mMessagesDatabaseReference.child("message").push().setValue(chatMessage);
                        }
                    });
        }
    }


    // Inflate the menu options from the res/menu/action_bar.xml file.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;
            case R.id.action_members:
                // User chose the "members" item
                Intent groupInfoIntent = new Intent(InGroupActivity.this, GroupInfoActivity.class);
                groupInfoIntent.putExtra("postKey",postKey);
                groupInfoIntent.putExtra("memberName",mUsername);
                groupInfoIntent.putExtra("existStatus",exitStatus);
                startActivity(groupInfoIntent);
                return true;

            case R.id.action_quit:
                quitFromGroup();
                //finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void quitFromGroup(){
        Query userRef = mMessagesDatabaseReference.child("User").orderByChild("userName").equalTo(mUsername);
        userRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                dataSnapshot.getRef().removeValue();

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
        });

    }

    public void updatePostInfo(){

    }

}
