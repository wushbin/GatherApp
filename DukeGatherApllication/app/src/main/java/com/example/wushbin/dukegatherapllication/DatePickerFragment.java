package com.example.wushbin.dukegatherapllication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

/**
 * Created by Shangxing on 4/13/2017.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    EditText txtdate;
    public DatePickerFragment(View view){
        txtdate = (EditText)view;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstancesState){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(),this,year,month,day);

        }
         return new DatePickerDialog(getActivity(),this,0,0,0);
    }
    public void onDateSet(DatePicker view, int year, int month, int day){
        String date = day + "-"+(month+1)+"-"+year;
        txtdate.setText(date);

    }
}
