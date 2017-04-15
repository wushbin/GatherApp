package com.example.wushbin.dukegatherapllication;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.icu.text.DateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TimePicker;
import android.widget.EditText;

/**
 * Created by Shangxing on 4/15/2017.
 */

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    EditText txtdate;
    public TimePickerFragment(View view){
        txtdate = (EditText)view;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstancesState){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(),this,hour,minute, android.text.format.DateFormat.is24HourFormat(getActivity()));

        }
        return new TimePickerDialog(getActivity(),this,0,0, android.text.format.DateFormat.is24HourFormat(getActivity()));
    }
    public void onTimeSet(TimePicker view, int hour, int minute){
        String time = hour + ":"+minute;
        txtdate.setText(time);

    }
}
