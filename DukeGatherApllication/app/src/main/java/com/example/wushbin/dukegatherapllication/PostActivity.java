package com.example.wushbin.dukegatherapllication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by wushbin on 3/3/17.
 */

public class PostActivity extends AppCompatActivity {

    int quantityOfPeoplePost = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Button increaseButton = (Button)findViewById(R.id.buttonplus);
        increaseButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                quantityOfPeoplePost = quantityOfPeoplePost + 1;
                display(quantityOfPeoplePost);
            }
                                          }
        );
        Button decreaseButton = (Button)findViewById(R.id.buttonmin);
        increaseButton.setOnClickListener(new View.OnClickListener(){
                                              public void onClick(View v){
                                                  quantityOfPeoplePost = quantityOfPeoplePost - 1;
                                                  display(quantityOfPeoplePost);
                                              }
                                          }
        );
    }
    private void display(int number){
        TextView numberofpeopleTextVuew = (TextView)findViewById(R.id.numberofpeople);
        numberofpeopleTextVuew.setText(""+number);
    }
    public void onincrease(View view) {

        quantityOfPeoplePost = quantityOfPeoplePost + 1;
        display(quantityOfPeoplePost);
    }

}

