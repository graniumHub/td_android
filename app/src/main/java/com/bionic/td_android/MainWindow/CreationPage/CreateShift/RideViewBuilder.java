package com.bionic.td_android.MainWindow.CreationPage.CreateShift;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bionic.td_android.Entity.Ride;
import com.bionic.td_android.R;
import com.bionic.td_android.Utility.DateUtility;
import com.bionic.td_android.Utility.TimePack;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by user on 12.05.2016.
 */
public class RideViewBuilder {


    private Ride ride;
    private ShiftPageBuilder shiftBuilder;



    @JsonIgnore
    private FragmentManager manager;
    @JsonIgnore
    private View view;
    @JsonIgnore
    private TimePack startHour;
    @JsonIgnore
    private TimePack endHour;
    @JsonIgnore
    private Date selectedDate;
    @JsonIgnore
    private TextView datefield;
    @JsonIgnore
    private TextView fromTimefield;
    @JsonIgnore
    private TextView endTimefield;
    @JsonIgnore
    public View getView(){return view;}

    public Ride getRide() {
        return ride;
    }

    public RideViewBuilder(Ride ride, ShiftPageBuilder shiftBuilder) {
        this.ride = ride;
        this.shiftBuilder = shiftBuilder;
        createTimePacks();
    }

    @JsonIgnore
    public View getViewPresentation(Context context,FragmentManager manager){
        this.manager = manager;
        view = LayoutInflater.from(context).inflate(R.layout.view_add_ride,null);
        configView();
        if(ride.getStartTime() != null && ride.getEndTime() !=null)populateView();
        return view;
    }


    public void populateView(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(ride.getStartTime());
        datefield.setText(DateUtility.getDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)));
        fromTimefield.setText(DateUtility.getTime(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE)));
        cal.setTime(ride.getEndTime());
        endTimefield.setText(DateUtility.getTime(cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE)));

    }
    private void configView(){

        if(startHour == null)startHour = new TimePack();
        if(endHour == null)endHour = new TimePack();
        datefield = (TextView) view.findViewById(R.id.item_begin_date);
        datefield.setOnClickListener(v -> new DatePickerFragment(datefield,this).show(manager,""));
        fromTimefield = (TextView) view.findViewById(R.id.item_begin_time);
        fromTimefield.setOnClickListener(v -> {
            if(selectedDate != null)
                new TimePickerFragment(fromTimefield,this).show(manager,"StartTime");
            else  Snackbar.make(shiftBuilder.getView(), "Select date first", Snackbar.LENGTH_LONG).show();
        });
        endTimefield = (TextView) view.findViewById(R.id.item_end_time);
        endTimefield.setOnClickListener(v -> {
            if(startHour.isValid())
                new TimePickerFragment(endTimefield,this).show(manager,"EndTime");
            else  Snackbar.make(shiftBuilder.getView(), "Input start time first", Snackbar.LENGTH_LONG).show();
        });
        ImageButton del = (ImageButton) view.findViewById(R.id.button_delete_ride);
        del.setOnClickListener(v -> shiftBuilder.deleteView(view,ride));
    }

    public void invalidateEndTime(){
        endHour.invalidate();
        ride.setEndTime(null);
        endTimefield.setText("00:00");
    }

    public void invalidateStartTime(){
        startHour.invalidate();
        ride.setStartTime(null);
        fromTimefield.setText("00:00");
    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

        public TextView textView;
        private RideViewBuilder ride;

        public TimePickerFragment() {

        }
        public TimePickerFragment(TextView textView,RideViewBuilder ride) {
            this.textView = textView;
            this.ride = ride;
        }

        @Override
        public Dialog onCreateDialog(Bundle saveInstanceState) {
            final Calendar cal = Calendar.getInstance();
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(), this, hour, minute, android.text.format.DateFormat.is24HourFormat(getActivity()));
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            StringBuilder text = new StringBuilder();
            if (hourOfDay < 10)
                text.append("0");
            text.append(hourOfDay).append(":");
            if (minute < 10)
                text.append("0");
            text.append(minute);
            if(getTag().contains("Start")) {
                ride.startHour.hours = hourOfDay;
                ride.startHour.minutes = minute;
                textView.setText(text);
                ride.invalidateEndTime();
                Log.e("Bionic", "Ride date: " + ride.startHour.toString());

            }
            if (getTag().contains("End")){
                ride.endHour.hours = hourOfDay;
                ride.endHour.minutes = minute;
                textView.setText(text);
                ride.invokeAutoCountings();
                Log.e("Bionic", "Ride end date: " + ride.endHour.toString());
            }
        }
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

        private TextView textView;
        private RideViewBuilder ride;

        public DatePickerFragment() {

        }
        public DatePickerFragment(TextView textView,RideViewBuilder ride) {
            this.textView = textView;
            this.ride = ride;
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

            Calendar cal = Calendar.getInstance();
            cal.set(year,monthOfYear,dayOfMonth, 0, 0 ,0);

            cal.set(Calendar.MILLISECOND,0);

            ride.selectedDate = cal.getTime();
            textView.setText(text);
            ride.invalidateStartTime();
            ride.invalidateEndTime();

        }
    }

    public boolean validate(View view){
        if(!startHour.isValid() || !endHour.isValid()) return false;
        createDates();
        if(ride.getStartTime() == null || ride.getEndTime() == null){
            Snackbar.make(view,"Input time in rides" , Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void createTimePacks(){
        if(ride.getStartTime() == null || ride.getEndTime() == null)return;
        Calendar cal = Calendar.getInstance();
        cal.setTime(ride.getStartTime());
        selectedDate = new Date(cal.get(Calendar.YEAR) - 1900 ,cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH),0,0,0);
        startHour = new TimePack(cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE));
        cal.setTime( ride.getEndTime());
        endHour = new TimePack(cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE));
    }

    private void createDates(){
        Date start = new Date(selectedDate.getTime() + startHour.getLongFromDaystart());
        ride.setStartTime(start);
        Date end = null;
        Date nextSelectedDay = new Date(selectedDate.getTime() + 86400000l);
        if(endHour.hours < startHour.hours || (endHour.hours == startHour.hours && endHour.minutes < startHour.minutes))
            end = new Date(nextSelectedDay.getTime() + endHour.getLongFromDaystart() );
        else end = new Date(selectedDate.getTime() + endHour.getLongFromDaystart() );
        ride.setEndTime(end);
    }
    public void invokeAutoCountings(){
        shiftBuilder.afterTextChanged(null);
    }


}
