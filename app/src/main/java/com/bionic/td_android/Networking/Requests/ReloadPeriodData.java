package com.bionic.td_android.Networking.Requests;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.bionic.td_android.Data.DbManager;
import com.bionic.td_android.Entity.User;
import com.bionic.td_android.MainWindow.MainActivity;
import com.bionic.td_android.MainWindow.Overview.Utility.DateUpdater;
import com.bionic.td_android.Networking.API;
import com.bionic.td_android.Networking.IRequest;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import dmax.dialog.SpotsDialog;

/**
 * Created by user on 14.05.2016.
 */
public class ReloadPeriodData implements IRequest {


    private View view;
    private long year,period;
    private MainActivity activity;
    private DateUpdater reloader;

    public ReloadPeriodData(View view, long year, long period, MainActivity activity, DateUpdater reloader) {
        this.view = view;
        this.year = year;
        this.period = period;
        this.activity = activity;
        this.reloader = reloader;
    }

    @Override
    public void execute() {

        Log.e("Bionic", "Start");
        DbManager manager = new DbManager(view.getContext());
        User user = manager.loadUser();
        String url = API.GET_SUMMARY(user.getmId(), year, period);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        String login = sharedPref.getString("login", "");
        String pass = sharedPref.getString("password", "");

        final AlertDialog dialog = new SpotsDialog(activity,"Updating period");
        dialog.show();
        String encoded = Base64.encodeToString((login + ":" + pass).getBytes(), 0);
        Log.e("Bionic", encoded);
        Log.e("Bionic", year + "Period: " +  period);
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization","Basic " + encoded);
        client.get(view.getContext(), url, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                dialog.dismiss();
                Log.e("Bionic", "Fail " + statusCode + responseString);
                switch (statusCode){
                    case 400:
                        //future
                        Snackbar.make(view, "You cant watch shifts from future", Snackbar.LENGTH_LONG).show();
                        break;
                    case 404:
                        //no shifts for this period
                        Snackbar.make(view, "You dont have any shifts for this period", Snackbar.LENGTH_LONG).show();
                        break;

                    default:
                        Snackbar.make(view, "Error loading period", Snackbar.LENGTH_LONG).show();
                        break;
                }
                activity.onBackPressed();

            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                dialog.dismiss();
                Log.e("Bionic", statusCode + " " + responseString);
                reloader.updateData(responseString);
            }
        });
    }
}
