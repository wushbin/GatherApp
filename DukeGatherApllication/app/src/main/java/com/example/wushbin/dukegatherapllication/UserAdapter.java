package com.example.wushbin.dukegatherapllication;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.example.wushbin.dukegatherapllication.R.id.photoImageView;

/**
 * Created by wushbin on 4/14/17.
 */



public class UserAdapter extends ArrayAdapter<User> {
    private String TAG= "**User Adapter";

    public UserAdapter(Activity context, int resource, List<User> users) {
        super(context, resource, users);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if(listItemView == null) {
            listItemView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.user_item, parent, false);
        }

        User currentUser = getItem(position);

        TextView nameText = (TextView) listItemView.findViewById(R.id.name_text_view);
        TextView contactText = (TextView) listItemView.findViewById(R.id.email_text_view);
        ImageView photoImageView = (ImageView) listItemView.findViewById(R.id.image_user_view);

        nameText.setText(currentUser.getUserName());
        contactText.setText(currentUser.getEmail());
        String currentPhotoUrl = currentUser.getPhotoUrl();
        if(currentPhotoUrl != null) {
            Log.v(TAG, currentUser.getPhotoUrl());

            if (currentPhotoUrl.length() > 0) {
                Glide.with(photoImageView.getContext()).load(currentUser.getPhotoUrl()).into(photoImageView);
            }else{
                photoImageView.setImageResource(R.mipmap.ic_launcher);
            }
        }
        return listItemView;
    }
}
