package com.bionic.td_android.Utility;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;

import com.bionic.td_android.MainWindow.CreationPage.CreateShift.ShiftPageBuilder;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by user on 09.04.2016.
 */

public class ShiftDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    private TextView textView;
    private ShiftPageBuilder shift;

    public ShiftDatePicker() {

    }
    public ShiftDatePicker(TextView textView, ShiftPageBuilder shift) {
        this.textView = textView;
        this.shift = shift;
    }

    @Override
    public Dialog onCreateDialog(Bundle saveInstanceState) {
        final Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        StringBuilder text = new StringBuilder();
        if (dayOfMonth < 10)
            text.append("0");
        text.append(dayOfMonth).append("/");
        if (monthOfYear + 1 < 10)
            text.append("0");
        text.append(monthOfYear + 1).append("/").append(year);
        textView.setText(text);

        if(getTag().contains("Start")) {
            Calendar calendar = GregorianCalendar.getInstance();
            Date date = shift.getShift().getStartTime();
            if(date != null){
                calendar.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                calendar.set(Calendar.MILLISECOND,0);
                Calendar tmp = GregorianCalendar.getInstance();
                tmp.setTime(date);

                calendar.add(Calendar.MINUTE, tmp.get(Calendar.MINUTE));
                calendar.add(Calendar.HOUR_OF_DAY,tmp.get(Calendar.HOUR_OF_DAY));

            }
            else calendar.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
            calendar.set(Calendar.MILLISECOND,0);
            shift.getShift().setStartTime(calendar.getTime());
            Log.e("Bionic", "Shift date: " + shift.getShift().getStartTime().toString());
        }
        if(getTag().contains("End")){

            Calendar calendar = GregorianCalendar.getInstance();
            Date date = shift.getShift().getEndTime();
            if(date != null){
                calendar.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                Calendar tmp = GregorianCalendar.getInstance();
                tmp.setTime(date);
                calendar.add(Calendar.MINUTE, tmp.get(Calendar.MINUTE));
                calendar.add(Calendar.HOUR_OF_DAY, tmp.get(Calendar.HOUR_OF_DAY));

            }else calendar.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
            calendar.set(Calendar.MILLISECOND,0);
            shift.getShift().setEndTime(calendar.getTime());
            Log.e("Bionic", "Shift date: " + shift.getShift().getEndTime().toString());
        }
        shift.recountPause();
    }
}