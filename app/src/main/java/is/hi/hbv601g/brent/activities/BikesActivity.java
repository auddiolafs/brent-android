package is.hi.hbv601g.brent.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import is.hi.hbv601g.brent.models.Bike;
import is.hi.hbv601g.brent.fragments.BikeListFragment;
import is.hi.hbv601g.brent.R;

public class BikesActivity extends CurrentActivity implements BikeListFragment.SelectionListener {

    private ArrayList<Bike> mBikes = new ArrayList<>();
    private ArrayList<String> mTypes = new ArrayList<>();
    private ArrayList<String> mSizes = new ArrayList<>();
    private BikeListFragment mBikeListFragment;
    private final Calendar mStartDate = Calendar.getInstance();
    private final Calendar mEndDate = Calendar.getInstance();
    private static final String TAG = "BikesActivity";
    private FirebaseFirestore mDB = FirebaseFirestore.getInstance();
    private boolean mDataFetched = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.connected) {
            setUp();
        }
    }
    
    @Override
    public void setUp() {
        setSizes();
        fetchData();

        if (!mDataFetched) {
            setContentView(R.layout.activity_loading);
            super.setUp();
            setSizes();
            fetchData();
        }
        else {
            setContentView(R.layout.activity_bikes);
            super.setUp();
        }
    }

    /**
     * Start the BikeActivity to see bike details for the selected bike and/or book the bike.
     * @param bike - The selected Bike to see more details for and/or book.
     */
    @Override
    public void onBikeSelected(Bike bike) {
        Intent intent = new Intent(getApplicationContext(),
                BikeActivity.class);
        intent.putExtra("bike", bike);
        intent.putExtra("startDate", mStartDate.getTime());
        intent.putExtra("endDate", mEndDate.getTime());
        startActivity(intent);
    }

    /**
     * Creates the fragment for the list of bikes.
     */
    private void setBikeList() {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(BikeListFragment.BIKES_KEY, mBikes);
        mBikeListFragment = new BikeListFragment();
        mBikeListFragment.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.bikeListContainer, mBikeListFragment).commit();
    }


    /**
     * Creates the datepickers.
     * Sets an onClickListener for both datepickers - startDate and endDate.
     */
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


    /**
     * Set spinner data for both types spinner and sizes spinner.
     * Sets default selection to "All".
     * Adds an onItemSelectedListener for both spinners.
     */
    private void setSpinners() {
        final Spinner types = findViewById(R.id.types);
        final Spinner sizes = findViewById(R.id.sizes);
        ArrayAdapter<String> adapter;

        adapter= new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, this.mTypes);
        adapter.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item
        );
        types.setAdapter(adapter);
        types.setSelection(adapter.getPosition("All"));

        adapter= new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, this.mSizes);
        adapter.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item
        );
        sizes.setAdapter(adapter);
        sizes.setSelection(adapter.getPosition("All"));

        // On item selected listeners for spinners
        types.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedType = types.getSelectedItem().toString();
                String selectedSize = sizes.getSelectedItem().toString();
                try {
                    mBikeListFragment.filterBikes(selectedType, selectedSize);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO: on nothing selected
            }
        });

        sizes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedType = types.getSelectedItem().toString();
                String selectedSize = sizes.getSelectedItem().toString();
                try {
                    mBikeListFragment.filterBikes(selectedType, selectedSize);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO: on nothing selected
            }
        });
    }

    /**
     * Fetches types from Firestore and calls the function to fetch bikes.
     */
    private void fetchData() {
        final ArrayList<String> types = new ArrayList<>();
        final Task<QuerySnapshot> task = mDB.collection("types").get();

        // Fetch types first and then bikes, both are asynchronous calls so both need to finish
        // before setting the content view
        task.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot document : task.getResult()) {
                    types.add(document.getData().get("type").toString());
                    Log.d(TAG, document.getId() + " => " + document.getData());
                }
                mTypes = types;
                fetchBikes();
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            public void onFailure(Exception e) {
                Log.d(TAG, "Error fetching types");
            }
        });
    }

    /**
     * Fetches all bikes from Firestore db, to be displayed in the bikes list.
     */
    private void fetchBikes() {
        final ArrayList<Bike> bikes = new ArrayList<>();
        final Task<QuerySnapshot> task = mDB.collection("bikes")
                .get();

        task.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                mDataFetched = true;
                setUp();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Bike bike = Bike.toEntity(document.getId(), document.getData());
                    if (bike == null) {
                        Log.d(TAG, "error");
                    } else {
                        bikes.add(bike);
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                }
                mBikes = bikes;
                setSpinners();
                setDatePickers();
                setBikeList();
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            public void onFailure(Exception e) {
                mDataFetched = true;
                setUp();
                Log.d(TAG, "Error fetching bikes");
            }
        });
    }


    /**
     * Set sizes for the dropdown spinner used to filter by size.
     */
    private void setSizes() {
        mSizes.add("All");
        mSizes.add("S");
        mSizes.add("M");
        mSizes.add("L");
    }
}
