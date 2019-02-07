package com.example.example;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import org.json.JSONArray;

public class MainActivity extends CurrentActivity implements FetchTask.FetchTaskCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Basic things which are done in each activity
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        if(this.connected) {
            setUp();
        }
    }

    public void setUp() {
        setContentView(R.layout.activity_main);
        // Get toolbar in layout (defined in xml file)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Set it as actionbar
        setSupportActionBar(toolbar);


        // Remove label/projectName/title from actionbar
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        /*Button addButton = (Button)findViewById(R.id.addButton);

        // Set action listener
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView firstNumber = (TextView)findViewById(R.id.firstNumber);
                TextView secondNumber = (TextView)findViewById(R.id.secondNumber);
                TextView result = (TextView)findViewById(R.id.result);
                int x = Integer.parseInt(firstNumber.getText().toString()) + Integer.parseInt(secondNumber.getText().toString());
                result.setText("" + x);
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Overide method in AppCompatActivity, which allows us set menu defined in
        // res/menu/main_menu.xml into actionbar
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // If any action happens in menu it will call this method
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Switch on id on each menu item
        switch (item.getItemId()) {
            case R.id.settings:
                // TODO
                return true;
            case R.id.home:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onResultReceived(JSONArray result) {
        //System.out.println(result);
    }
}
