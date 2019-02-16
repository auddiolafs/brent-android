package is.hi.hbv601g.brent;

import android.app.DatePickerDialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BikesActivity extends CurrentActivity implements FetchTask.FetchTaskCallback {

    private JSONArray bikes;
    private List<String> types = new ArrayList<>();
    private List<String> sizes = new ArrayList<>();
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

    }

    @Override
    public void onResultReceived(Map<String,JSONArray> result) {
        // Get toolbar in layout (defined in xml file)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Set it as actionbar
        setSupportActionBar(toolbar);
//        ActionBar ab = getSupportActionBar();
        // Set "go back" functionality
//        ab.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_bikes);
        extractFromResponse(result);
        setDatePickers();
        setSpinners();
        
    }

    private void setDatePickers() {
        final EditText startDate = findViewById(R.id.editText);
        final EditText endDate = findViewById(R.id.editText2);
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
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        startDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
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
