package is.hi.hbv601g.brent;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BikesActivity extends CurrentActivity implements FetchTask.FetchTaskCallback {

    private JSONArray bikes;
    private List<String> types = new ArrayList<>();
    private List<String> sizes = new ArrayList<>();
    private Date startDate;
    private Date endDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.connected) {
            setUp();
        }
    }

    @Override
    public void setUp() {
        setContentView(R.layout.activity_loading);
        // Get toolbar in layout (defined in xml file)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Set it as actionbar
        setSupportActionBar(toolbar);
//        ActionBar ab = getSupportActionBar();
        // Set "go back" functionality
//        ab.setDisplayHomeAsUpEnabled(true);
        new FetchTask(this).execute("/types", "/getall");
        /* Back arrow (Not needed with BRENT Logo)
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, item.getTitle(), Toast.LENGTH_LONG).show();
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResultReceived(Map<String,JSONArray> result) {
        setContentView(R.layout.activity_bikes);
        // Get toolbar in layout (defined in xml file)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Set it as actionbar
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
//        ActionBar ab = getSupportActionBar();
        // Set "go back" functionality
//        ab.setDisplayHomeAsUpEnabled(true);
        extractFromResponse(result);
        setDatePickers();
        setSpinners();

        Button bikeButton = findViewById(R.id.bikeButton);
        final Intent intent = new Intent(this, BikeActivity.class);
        bikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passDates(intent);
                startActivity(intent);
            }
        });

    }

    private void passDates(Intent intent) {
        EditText startDateText = findViewById(R.id.startDate);
        EditText endDateText = findViewById(R.id.endDate);
        String[] x = startDateText.getText().toString().split("/");
        String[] y = endDateText.getText().toString().split("/");
        int day, month, year;
        day = Integer.parseInt(x[0]);
        month = Integer.parseInt(x[1]);
        year = Integer.parseInt(x[2]);
        Date startDate = new GregorianCalendar(year, month, day).getTime();
        Date endDate = new GregorianCalendar(year, month, day).getTime();
        intent.putExtra("startDate", startDate);
        intent.putExtra("endDate", endDate);
    }

    private void setDatePickers() {
        final EditText startDate = findViewById(R.id.startDate);
        final EditText endDate = findViewById(R.id.endDate);
        startDate.setInputType(InputType.TYPE_NULL);
        endDate.setInputType(InputType.TYPE_NULL);

        startDate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar cldr = Calendar.getInstance();
                        int day = cldr.get(Calendar.DAY_OF_MONTH);
                        int month = cldr.get(Calendar.MONTH);
                        int year = cldr.get(Calendar.YEAR);
                        // date picker dialog
                        DatePickerDialog startPicker = new DatePickerDialog(BikesActivity.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int day) {
                                        startDate.setText(day + "/" + (month + 1) + "/" + year);
                                    }
                                }, year, month, day);
                        startPicker.show();
                    }
                });

        endDate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar cldr = Calendar.getInstance();
                        int day = cldr.get(Calendar.DAY_OF_MONTH);
                        int month = cldr.get(Calendar.MONTH);
                        int year = cldr.get(Calendar.YEAR);
                        // date picker dialog
                        DatePickerDialog endPicker = new DatePickerDialog(BikesActivity.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        endDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                    }
                                }, year, month, day);
                        endPicker.show();
                    }
                });
    }

    private void setSpinners() {
        Spinner types = findViewById(R.id.types);
        Spinner sizes = findViewById(R.id.sizes);
        ArrayAdapter<String> adapter;

        adapter= new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, this.types);
        adapter.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item
        );
        types.setAdapter(adapter);

        adapter= new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, this.sizes);
        adapter.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item
        );
        sizes.setAdapter(adapter);
    }

    private void extractFromResponse(Map<String,JSONArray> result) {
        JSONArray t = result.get("Types");
        JSONArray s = result.get("Sizes");
        try {

            JSONObject obj = null;
            Iterator<String> keys;
            // Extract types
            obj = (JSONObject) t.get(0);
            keys = obj.keys();
            while(keys.hasNext()) {
                String type = keys.next();
                this.types.add(type);
            }
            // Extract sizes
            obj = (JSONObject) s.get(0);
            keys = obj.keys();
            while(keys.hasNext()) {
                String type = keys.next();
                this.sizes.add(type);
            }

            this.bikes = result.get("Bikes");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
