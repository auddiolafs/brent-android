package com.example.example;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.concurrent.CountDownLatch;

public abstract class CurrentActivity extends AppCompatActivity {

    private static DialogFragment dialogFragment;
    public boolean connected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        this.dialogFragment = new RequireInternet();
        connected = isConnected();
    }

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        RequireInternet dialogFragment = new RequireInternet();
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null || !ni.isConnected()) {
            dialogFragment.show(this.getSupportFragmentManager(), "internet-required");
            return false;
        }
        return true;

    }

    public abstract void setUp();

}
