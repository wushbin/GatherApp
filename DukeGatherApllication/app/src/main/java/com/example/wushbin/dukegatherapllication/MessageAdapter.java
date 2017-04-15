package com.example.wushbin.dukegatherapllication;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.message;

/**
 * Created by wushbin on 3/8/17.
 */


public class MessageAdapter extends ArrayAdapter<Message> {
    public MessageAdapter(Activity context, int resource, List<Message> messages) {
        super(context, resource, messages);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.message_item, parent, false);
        }

        ImageView photoImageView = (ImageView) listItemView.findViewById(R.id.photoImageView);
        TextView messageName = (TextView) listItemView.findViewById(R.id.message_name);
        TextView messageContent = (TextView) listItemView.findViewById(R.id.message_content);

        Message currentMessage = getItem(position);

        boolean isPhoto = currentMessage.getPhotoUrl() != null;

        if (isPhoto) {
            messageContent.setVisibility(View.GONE);
            photoImageView.setVisibility(View.VISIBLE);
            // setImage
            Glide.with(photoImageView.getContext()).load(currentMessage.getPhotoUrl()).into(photoImageView);
        }else{
            messageContent.setVisibility(View.VISIBLE);
            photoImageView.setVisibility(View.GONE);
            messageContent.setText(currentMessage.getMessageContent());
        }

        messageName.setText(currentMessage.getUserName());
        return listItemView;
    }
}