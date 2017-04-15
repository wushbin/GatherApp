package com.example.wushbin.dukegatherapllication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by wushbin on 3/3/17.
 */

public class SearchActivity extends AppCompatActivity {
    String from1 = "";
    String to1 = "";
    //Intent intent = new Intent(SearchActivity.this, MainActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_search, menu);
        return true;
    }

    /*public void searchForMatch(View view) {
        Intent intent = new Intent(SearchActivity.this, MainActivity.class);
        startActivity(intent);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(SearchActivity.this, MainActivity.class);
        switch (item.getItemId()) {
            case R.id.search_submit:
                // User chose the "search" item, show the app search UI...
                //transfer data to main page
                Intent_Constants.fromSearch = 1;
                EditText from_text = (EditText) findViewById(R.id.postfrom);
                from1 = from_text.getText().toString();
                EditText to_text = (EditText) findViewById(R.id.postto);
                to1 = to_text.getText().toString();

                //Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                intent.putExtra("from", from1);
                intent.putExtra("to", to1);
                startActivity(intent);
                return true;

            case android.R.id.home:
                Intent_Constants.fromSearch = 0;
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}