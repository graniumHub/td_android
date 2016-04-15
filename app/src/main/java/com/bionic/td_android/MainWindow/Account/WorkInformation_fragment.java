package com.bionic.td_android.MainWindow.Account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.bionic.td_android.Entity.Job;
import com.bionic.td_android.Entity.User;
import com.bionic.td_android.Entity.WorkSchedule;
import com.bionic.td_android.MainWindow.MainActivity;
import com.bionic.td_android.Networking.Requests.UpdateWorkInfo;
import com.bionic.td_android.R;
import com.bionic.td_android.Utility.EntitySaver;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by user on 15.04.2016.
 */

public class WorkInformation_fragment extends Fragment implements TextWatcher {


    private MainActivity activity;
    private Toolbar toolbar;
    private CheckBox driver,operator;
    private EditText monday,tuesday,wednesday,thursday,friday,saturday,sunday;
    private CheckBox day_contract, zero_day_contract;
    private EditText contract_days;
    private CheckBox mounthly_payments, four_week_payments;
    private View scheduleBlock;
    private View button_help;
    private TextView error;
    private User user;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_step2, container, false);

        configure(view);
        return view;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        if(day_contract.isChecked()) {
            int time = getHoursSum();
            int expectedTime = 0;
            try {
                expectedTime = Integer.parseInt(contract_days.getText().toString());
            } catch (Exception e) {
                Log.e("Bionic", "Error parsing time");
            }
            if (time != expectedTime) {
                error.setVisibility(View.VISIBLE);
            }else error.setVisibility(View.GONE);

        }else {
            error.setVisibility(View.GONE);
        }
    }

    private void configure(View view){

        activity = (MainActivity)getActivity();

        configureToolbar(view);
        configureViews(view);
        checkboxBehaviour();
        loadValues();

    }

    private void loadValues(){
        user = EntitySaver.getUser();

        List<Job> jobs = user.getJobs();
        for (Job job : jobs) {
            if(job.getJobName().equals("Driver"))driver.setChecked(true);
            if(job.getJobName().equals("Operator"))operator.setChecked(true);
        }
        if(user.isFourWeekPayOff())four_week_payments.setChecked(true);
        else mounthly_payments.setChecked(true);

        if(user.isZeroHours()){
            zero_day_contract.setChecked(true);
            scheduleBlock.setVisibility(View.GONE);


        }else {
            day_contract.setChecked(true);
            contract_days.setText(String.valueOf(user.getContractHours()));
            WorkSchedule schedule = user.getWorkSchedule();
            monday.setText(schedule.getMonday());
            tuesday.setText(schedule.getTuesday());
            wednesday.setText(schedule.getWednesday());
            thursday.setText(schedule.getThursday());
            friday.setText(schedule.getFriday());
            saturday.setText(schedule.getSaturday());
            sunday.setText(schedule.getSunday());

        }



    }

    private void configureViews(View view){

        driver = (CheckBox) view.findViewById(R.id.checkbox_driver);
        operator = (CheckBox) view.findViewById(R.id.checkbox_operator);

        monday = (EditText) view.findViewById(R.id.input_monday);
        tuesday = (EditText) view.findViewById(R.id.input_tuesday);
        wednesday = (EditText) view.findViewById(R.id.input_wednesday);
        thursday = (EditText) view.findViewById(R.id.input_thursday);
        friday = (EditText) view.findViewById(R.id.input_friday);
        saturday = (EditText) view.findViewById(R.id.input_saturday);
        sunday = (EditText) view.findViewById(R.id.input_sunday);

        monday.addTextChangedListener(this);
        tuesday.addTextChangedListener(this);
        wednesday.addTextChangedListener(this);
        thursday.addTextChangedListener(this);
        friday.addTextChangedListener(this);
        saturday.addTextChangedListener(this);
        sunday.addTextChangedListener(this);



        day_contract = (CheckBox) view.findViewById(R.id.checkbox_day_contract);
        zero_day_contract = (CheckBox) view.findViewById(R.id.checkbox_zero_contract);
        contract_days = (EditText) view.findViewById(R.id.input_contract_days);
        contract_days.addTextChangedListener(this);

        mounthly_payments = (CheckBox) view.findViewById(R.id.checkbox_mounth_payments);
        four_week_payments = (CheckBox) view.findViewById(R.id.checkbox_four_week_payments);
        scheduleBlock = view.findViewById(R.id.block_schedule);
        error = (TextView) view.findViewById(R.id.error_hours);
        Button register = (Button)view.findViewById(R.id.button_register);
        button_help = view.findViewById(R.id.button_help);

        button_help.setOnClickListener(v -> {
            AlertDialog.Builder ab = new AlertDialog.Builder(v.getContext());
            ab.setTitle("Schedule tip");
            ab.setMessage("In this work schedule you fill in the number of hours you work each day");
            ab.setCancelable(true);
            ab.show();


        });
        register.setText("Apply");
        register.setOnClickListener(v -> {
            if(!validateForm(v)) return;

            Log.e("Bionic", "User before: " + user.toString());
            List<Job> jobs = new ArrayList<Job>();
            if(driver.isChecked())jobs.add(new Job("Driver"));
            if(operator.isChecked())jobs.add(new Job("Operator"));
            user.setJobs(jobs);

            if(zero_day_contract.isChecked()){
                user.setZeroHours(true);
                user.setWorkSchedule(null);
            }
            else {
                if(!contract_days.getText().toString().isEmpty())
                    user.setContractHours(Integer.parseInt(contract_days.getText().toString()));
                user.setZeroHours(false);
                WorkSchedule schedule = user.getWorkSchedule();
                if(schedule == null) schedule = new WorkSchedule();
                schedule.setCreationTime(new Date());
                schedule.setMonday(monday.getText().toString());
                schedule.setTuesday(tuesday.getText().toString());
                schedule.setWednesday(wednesday.getText().toString());
                schedule.setThursday(thursday.getText().toString());
                schedule.setFriday(friday.getText().toString());
                schedule.setSaturday(saturday.getText().toString());
                schedule.setSunday(sunday.getText().toString());
                user.setWorkSchedule(schedule);
            }
            if(four_week_payments.isChecked())user.setFourWeekPayOff(true);
            else user.setFourWeekPayOff(false);

            new UpdateWorkInfo(user,getView()).execute();



        });

    }

    public boolean validateForm(View layout) {

        boolean checkbox = (driver.isChecked() || operator.isChecked())
                && ( (day_contract.isChecked() && !contract_days.getText().toString().isEmpty()) || zero_day_contract.isChecked())
                && (mounthly_payments.isChecked() || four_week_payments.isChecked());


        if(day_contract.isChecked()) {

            int time = getHoursSum();
            int expectedTime = 0;
            try {
                expectedTime = Integer.parseInt(contract_days.getText().toString());
            } catch (Exception e) {
                Log.e("Bionic", "Error parsing time");
                Snackbar.make(layout, "Fill in necessary forms", Snackbar.LENGTH_LONG).show();
                return false;
            }
            if (time != expectedTime) {
                Snackbar.make(layout,"The hours sum must be equal to total time",Snackbar.LENGTH_LONG).show();
                return false;
            }


            if (!(isDayValid(monday) && isDayValid(tuesday) && isDayValid(wednesday) && isDayValid(thursday) && isDayValid(friday) && isDayValid(saturday) && isDayValid(sunday))) {
                Snackbar.make(layout,"The input value must be between 0 and 24",Snackbar.LENGTH_LONG).show();
                return false;
            }

        }
        return checkbox;

    }

    private boolean isDayValid(EditText day) {
        if(day.getText().toString().isEmpty())return true;
        if ((Integer.parseInt(day.getText().toString()) < 0 || Integer.parseInt(day.getText().toString()) > 24)) {
            return false;
        } else {
            return true;
        }
    }

    private int getHoursSum(){
        int res = 0;
        try{
            if(!monday.getText().toString().isEmpty())
                res += Integer.parseInt(monday.getText().toString());
            if(!tuesday.getText().toString().isEmpty())
                res += Integer.parseInt(tuesday.getText().toString());
            if(!wednesday.getText().toString().isEmpty())
                res += Integer.parseInt(wednesday.getText().toString());
            if(!thursday.getText().toString().isEmpty())
                res += Integer.parseInt(thursday.getText().toString());
            if(!friday.getText().toString().isEmpty())
                res += Integer.parseInt(friday.getText().toString());
            if(!saturday.getText().toString().isEmpty())
                res += Integer.parseInt(saturday.getText().toString());
            if(!sunday.getText().toString().isEmpty())
                res += Integer.parseInt(sunday.getText().toString());
            return res;

        }catch (Exception e){
            Log.e("Bionic","Error parsing time");
            return 0;
        }

    }


    private void checkboxBehaviour(){

        day_contract.setOnClickListener(v -> {
            if(day_contract.isChecked())contract_days.setEnabled(true);
            else contract_days.setEnabled(false);
            zero_day_contract.setChecked(false);
            scheduleBlock.setVisibility(View.VISIBLE);
            afterTextChanged(null);
        });

        zero_day_contract.setOnClickListener(v -> {
            if(zero_day_contract.isChecked()){
                day_contract.setChecked(false);
                contract_days.setEnabled(false);
            }
            scheduleBlock.setVisibility(View.GONE);
            error.setVisibility(View.GONE);
        });


        four_week_payments.setOnClickListener(v -> {
            if(mounthly_payments.isChecked())mounthly_payments.setChecked(false);
        });

        mounthly_payments.setOnClickListener(v -> {
            if(four_week_payments.isChecked())four_week_payments.setChecked(false);
        });

    }

    private void configureToolbar(View view){


        toolbar = (Toolbar)view.findViewById(R.id.simple_toolbar);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        activity.getSupportActionBar().setTitle("Registration");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                activity.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

