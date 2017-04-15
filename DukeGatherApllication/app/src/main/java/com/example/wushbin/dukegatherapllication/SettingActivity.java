package com.example.wushbin.dukegatherapllication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wushbin.dukegatherapllication.data.UserContract;
import com.example.wushbin.dukegatherapllication.data.UserContract.UserEntry;
import com.example.wushbin.dukegatherapllication.data.UserDbHelper;

/**
 * Created by wushbin on 3/3/17.
 */

public class SettingActivity extends AppCompatActivity {
    private Spinner mGenderSpinner;
    private int mGender = 0;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String FirstName = "firstNameKey";
    public static final String LastName = "lastNameKey";
    public static final String Gender = "genderKey";
    public static final String Email = "emailKey";
    SharedPreferences sharedpreferences;
    EditText lastName;
    EditText firstName;
    EditText email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        lastName = (EditText)findViewById(R.id.last_name);
        firstName = (EditText)findViewById(R.id.first_name);
        email = (EditText)findViewById(R.id.edit_email);
        mGenderSpinner = (Spinner) findViewById(R.id.spinner_gender);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String mLastName;
        String mFirstName;
        String mEmail;
        mLastName = sharedpreferences.getString(LastName,null);
        mFirstName = sharedpreferences.getString(FirstName,null);
        mEmail = sharedpreferences.getString(Email,null);
        mGender = sharedpreferences.getInt(Gender,0);

        lastName.setText(mLastName);
        firstName.setText(mFirstName);
        email.setText(mEmail);

        setupSpinner();
        mGenderSpinner.setSelection(mGender);
        //displayDatabaseInfo();
    }

    private void setupSpinner() {
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);
        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mGenderSpinner.setAdapter(genderSpinnerAdapter);
        // Set the integer mSelected to the constant values
        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_male))) {
                        mGender = 1; // Male
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        mGender = 2; // Female
                    } else {
                        mGender = 0; // Unknown
                    }
                }
            }
            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = 0; // Unknown
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        switch (item.getItemId()) {
            case R.id.save:
                String first  = firstName.getText().toString();
                String last  = lastName.getText().toString();
                String em  = email.getText().toString();
                editor.putString(FirstName, first);
                editor.putString(LastName, last);
                editor.putString(Email, em);
                editor.putInt(Gender,mGender);
                editor.commit();
                Toast.makeText(this,"saved",Toast.LENGTH_LONG).show();

                //save the information into the sharePreference
                return true;
            case R.id.discard:
                // discard edition
                lastName.setText("");
                firstName.setText("");
                email.setText("");
                mGenderSpinner.setSelection(0);
                return true;
            case R.id.clear:
                // discard edition
                editor.clear();
                editor.commit();
                lastName.setText("");
                firstName.setText("");
                email.setText("");
                mGenderSpinner.setSelection(0);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private void displayDatabaseInfo() {
        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        UserDbHelper mDbHelper = new UserDbHelper(this);
        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        // Perform this raw SQL query "SELECT * FROM pets"
        // to get a Cursor that contains all rows from the pets table.
        Cursor cursor = db.rawQuery("SELECT * FROM " + UserEntry.TABLE_NAME, null);

        try {
            // Display the number of rows in the Cursor (which reflects the number of rows in the
            // pets table in the database).
            TextView displayView = (TextView) findViewById(R.id.information);
            displayView.setText("Number of rows in users database table: " + cursor.getCount());
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }
}
