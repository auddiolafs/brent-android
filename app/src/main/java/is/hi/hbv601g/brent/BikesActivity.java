package is.hi.hbv601g.brent;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BikesActivity extends CurrentActivity implements FetchTask.FetchTaskCallback {

    private List<Bike> mBikes = new ArrayList<>();
    private List<String> mTypes = new ArrayList<>();
    private List<String> mSizes = new ArrayList<>();
    private final Calendar mStartDate = Calendar.getInstance();
    private final Calendar mEndDate = Calendar.getInstance();
    private static final String TAG = "BikesActivity";
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

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

        fetchBikesFirestore();

        List<Bike> bikes = new ArrayList<>();
        bikes.add(new Bike("Trek", "Venge", "M", "JH4NA12641T940293", new Long(9900), "n3b48QKTjPvJleUawXJm"));
        bikes.add(new Bike("Specialized", "Madone", "L", "WBSDX9C59CE293468", new Long(11900), "lKG6wY3NUTK8ahbxckzF"));

        List<Tour> tours = new ArrayList<>();
        tours.add(new Tour("bcvgy3GhRnbz379TkA82", "Capital area biking", "Reykjavík", new Long(9900), new Date(), new Date()));

        new BookingService().saveBooking(bikes, null, tours, new Date(), new Date(), "Reykjavík");
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

        if (!result.containsKey("error")) {
            extractFromResponse(result);
            setDatePickers();
            setSpinners();
        }
        Button bikeButton = findViewById(R.id.bikeButton);
        final Intent intent = new Intent(this, BikeActivity.class);
        bikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bike bike = new Bike("brand6000", "name6000", "L", "serial6000", new Long(10900), "AS97ikocUK");
                intent.putExtra("bike", bike);
                startActivity(intent);
            }
        });
    }

    private void fetchBikesFirestore() {
        final List<Bike> bikes = new ArrayList<>();
        final Task<QuerySnapshot> task = db.collection("bikes")
                .get();

        task.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    bikes.add(toEntity(document.getId(), document.getData()));
                    Log.d(TAG, document.getId() + " => " + document.getData());
                }

                mBikes = bikes;
                setBikes();
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            public void onFailure(Exception e) {
                Log.d(TAG, "error");
            }
        });
    }

    private static Bike toEntity(String bikeId, Map<String, Object> bikeData) {
        Bike b = new Bike();
        try {
            b.setId(bikeId);
            b.setBrand(bikeData.get("brand").toString());
            b.setName(bikeData.get("name").toString());
            b.setSize(bikeData.get("size").toString());
            b.setSerial(bikeData.get("serial").toString());
            b.setPrice(Long.parseLong( bikeData.get("ppd").toString()));
            return b;
        } catch (Exception e) {
            Log.d(TAG, "error");
            return null;
        }
    }

    private void setBikes() {
        // Log.d(TAG, mBikes.get(0).getName());
        // TODO: create cards
    }

    private void setDatePickers() {
        final EditText startDateText = findViewById(R.id.startDateText);
        final EditText endDateText = findViewById(R.id.endDateText);
        startDateText.setInputType(InputType.TYPE_NULL);
        endDateText.setInputType(InputType.TYPE_NULL);

        startDateText.setOnClickListener(
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
                                        startDateText.setText(day + "/" + (month + 1) + "/" + year);
                                    }
                                }, year, month, day);
                        mStartDate.setTime(new Date(year, month, day));
                        startPicker.show();
                    }
                });

        endDateText.setOnClickListener(
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
                                        endDateText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                    }
                                }, year, month, day);
                        mEndDate.setTime(new Date(year, month, day));
                        endPicker.show();
                    }
                });
    }

    private void setSpinners() {
        Spinner types = findViewById(R.id.types);
        Spinner sizes = findViewById(R.id.sizes);
        ArrayAdapter<String> adapter;

        adapter= new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, this.mTypes);
        adapter.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item
        );
        types.setAdapter(adapter);

        adapter= new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, this.mSizes);
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
                mTypes.add(type);
            }
            // Extract sizes
            obj = (JSONObject) s.get(0);
            keys = obj.keys();
            while(keys.hasNext()) {
                String type = keys.next();
                mSizes.add(type);
            }

            /*
            JSONArray bikes = result.get("Bikes");
            for (int i = 0; i < bikes.length(); i++) {
                obj = bikes.getJSONObject(i);
                String g = (String)obj.get("brand");
                Bike bike = new Bike(
                        (String) obj.get("brand"),
//                        (String) obj.get("name"),
                        "name",
                        (String) obj.get("size"),
                        (String) obj.get("serial"),
//                        Long.parseLong((String) obj.get("price"))
                        new Long(1)
                );
                mBikes.add(bike);
            }
            */

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
