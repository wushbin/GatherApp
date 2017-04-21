package com.example.wushbin.dukegatherapllication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;

/**
 * Created by wushbin on 3/2/17.
 */

public class InGroupActivity extends AppCompatActivity{

    public static final String ANONYMOUS = "anonymous";
    private static final String TAG = "InGroupActivity";


    public static final int DEFAULT_MSG_LENGTH_LIMIT = 140;
    private static final int RC_PHOTO_PICKER =  2;
    private static final int RC_EDIT_POST =3;
    private static final int RC_MEMBER_INFO =4;
    private ListView mMessageListView;
    private MessageAdapter mMessageAdapter;
    private EditText mMessageEditText;
    private Button mSendButton;
    private ImageButton mPhotoPickerButton;

    private boolean existStatus;
    private String postKey;
    private String mUsername;
    private String mUserEmail;
    private Uri mUserPhotoUri;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase; // a fire base database instance
    private DatabaseReference mMessagesDatabaseReference; // a database reference
    private ChildEventListener mChildEventListener;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mChatPhotosStorageReference;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private String Owner;


    // the onCreate to initialize this interface
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_list);
        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        mUsername = user.getDisplayName();
        mUserEmail = user.getEmail();
        mUserPhotoUri = user.getPhotoUrl();

        postKey = getIntent().getExtras().getString("postKey");
        existStatus = getIntent().getExtras().getBoolean("existStatus");

        //Toast.makeText(InGroupActivity.this,String.valueOf(postKey), Toast.LENGTH_SHORT).show();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("post").child(postKey);

        if(existStatus == false){
            User currentUser;
            if(mUserPhotoUri == null){
                String tempPhotoUrl = "";
                currentUser = new User (mUsername, mUserEmail, tempPhotoUrl);

            }else{
                currentUser = new User(mUsername, mUserEmail, mUserPhotoUri.toString());
            }
            String currentUserKey = mMessagesDatabaseReference.child("User").push().getKey();
            mMessagesDatabaseReference.child("User").child(currentUserKey).setValue(currentUser);
            existStatus = true;
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

        ValueEventListener OwnerListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Owner = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mMessagesDatabaseReference.child("userName").addValueEventListener(OwnerListener);
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
            case R.id.action_edit:
                // User chose the "Settings" item, show the app settings UI...
                editPostInformation();
                return true;
            case R.id.action_members:
                // User chose the "members" item
                showMemberInformation();
                return true;
            case R.id.action_quit:
                showQuitConfirmationDialog();
                return true;
            case R.id.action_delete:
                deleteGroup();
                return true;
            case R.id.action_post_detail:
                showPostInformation();
                return true;
            case android.R.id.home:
                Intent intent = new Intent(InGroupActivity.this, MainActivity.class);
                Intent_Constants.fromSearch = 0;
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * quite from group
     */
    //show quit group dialogue
    public void showQuitConfirmationDialog( ) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.quit_the_group);
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                quitFromGroup();
                existStatus = false;
                finish();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //deleteThisGroup();
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void quitFromGroup(){
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

    /**
     * show information
     */
    public void showMemberInformation(){
        Intent groupInfoIntent = new Intent(InGroupActivity.this, GroupInfoActivity.class);
        groupInfoIntent.putExtra("postKey",postKey);
        groupInfoIntent.putExtra("existStatus",existStatus);
        startActivityForResult(groupInfoIntent,RC_MEMBER_INFO);
    }


    /**
     * Update Post
     */
    public void editPostInformation(){
        if(! Owner.equals(mUsername)){
            Toast.makeText(InGroupActivity.this, "You're not the Owner of this group.", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(InGroupActivity.this, "Edit The Post.", Toast.LENGTH_LONG).show();
            Intent editInfoIntent = new Intent(InGroupActivity.this, EditPostActivity.class);
            editInfoIntent.putExtra("postKey",postKey);
            editInfoIntent.putExtra("existStatus",existStatus);
            startActivityForResult(editInfoIntent, RC_EDIT_POST);
        }
    }


    /**
     * delete group
     */
    public void deleteGroup(){
        //Log.v(TAG, Owner);
        if(! Owner.equals(mUsername)){
            Toast.makeText(InGroupActivity.this, "You're not the Owner of this group.", Toast.LENGTH_LONG).show();
        }else{
            showDeleteConfirmationDialog();
        }
    }
    //show delete group dialogue
    public void showDeleteConfirmationDialog( ) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_the_group);
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                    deleteThisGroup();
                    finish();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //deleteThisGroup();
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteThisGroup(){
        mMessagesDatabaseReference.removeValue();
    }

    /**
     * close the group
     */
    public void showCloseConfirmationDialog( ) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.close_the_group);
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                closeTheGroup();
                //finish();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //deleteThisGroup();
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void closeTheGroup(){
        mMessagesDatabaseReference.child("openStatus").setValue(false);
    }

    /**
     * Open the group
     */
    public void openTheGroup(){
        mMessagesDatabaseReference.child("openStatus").setValue(true);
    }


    /**
     * show post detail information
     */

    public void showPostInformation(){
        Intent showPostInfoIntent = new Intent(InGroupActivity.this, EditPostActivity.class);
        showPostInfoIntent.putExtra("postKey",postKey);
        showPostInfoIntent.putExtra("existStatus",existStatus);
        showPostInfoIntent.putExtra("onlyShow",true);
        startActivity(showPostInfoIntent);

    }
}
