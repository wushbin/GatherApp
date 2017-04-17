package com.example.wushbin.dukegatherapllication;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.resource;
import static android.R.attr.right;
import static android.R.attr.targetActivity;

/**
 * Created by wushbin on 3/8/17.
 */

public class PostAdapter extends ArrayAdapter<Post> {
    private Context mContext;
    public PostAdapter(Activity context, int resource, List<Post> posts) {
        super(context, resource, posts);
        this.mContext = context;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.post_item, parent, false);
        }

        TextView postFrom = (TextView) listItemView.findViewById(R.id.post_item_from);
        TextView postTo = (TextView) listItemView.findViewById(R.id.post_item_to);
        TextView postTime = (TextView) listItemView.findViewById(R.id.post_item_time);
        TextView postDate = (TextView) listItemView.findViewById(R.id.post_item_date);
        TextView postOwner = (TextView) listItemView.findViewById(R.id.post_item_owner);
        TextView postNum = (TextView) listItemView.findViewById(R.id.post_item_num2);
        TextView currentNum = (TextView) listItemView.findViewById(R.id.post_item_num1);
        TextView description = (TextView) listItemView.findViewById(R.id.post_item_description);
        final Post currentPost = getItem(position);
        postFrom.setText(currentPost.getFromPlace());
        postTo.setText(currentPost.getToPlace());
        postTime.setText(currentPost.getLeaveTime());
        postDate.setText(currentPost.getLeaveDate());
        postOwner.setText(currentPost.getUserName());
        postNum.setText(String.valueOf(currentPost.getNumOfPeople()));
        currentNum.setText(String.valueOf(currentPost.getCurrentNumofMembers()));

        boolean mOpenStatus = currentPost.getOpenStatus();
        //Log.v("**PostAdapter**",currentPost.getKey());
        //Log.v("**PostAdapter**",String.valueOf(mOpenStatus));
        Button join = (Button) listItemView.findViewById(R.id.post_join);
        Button enter = (Button) listItemView.findViewById(R.id.post_enter);
        if(! mOpenStatus){
            Log.v("**PostAdapter**",currentPost.getKey());
            join.setVisibility(View.GONE);
            //join.setBackgroundResource(R.color.button_disable);
        }
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mContext instanceof MainActivity){
                    String currentUserName = ((MainActivity)mContext).getmUsername();
                    boolean exitStatus = checkExistStatus(currentPost,currentUserName);
                    if(exitStatus == false){
                        ((MainActivity)mContext).showJoinConfirmationDialog(currentPost,exitStatus);
                    }
                    if(exitStatus == true){
                        Toast.makeText(getContext(), "You are already in this group! ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mContext instanceof MainActivity){
                    String currentUserName = ((MainActivity)mContext).getmUsername();
                    boolean exitStatus = checkExistStatus(currentPost,currentUserName);
                    if(exitStatus == true){
                        ((MainActivity)mContext).getIntoInGroupActivity(currentPost,exitStatus);
                    }
                    if(exitStatus == false){
                        Toast.makeText(getContext(), "You are not in this group!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return listItemView;
    }

    boolean checkExistStatus(Post currentPost, String currentUserName){
        boolean existStatus = false;
        for(User member : currentPost.getUsers()){
            if (currentUserName.equals(member.getUserName())){
                existStatus = true;
                break;
            }
        }
        return  existStatus;
    }

}
