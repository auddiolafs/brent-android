package is.hi.hbv601g.brent.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;

import is.hi.hbv601g.brent.fragments.ItemListFragment;
import is.hi.hbv601g.brent.fragments.SelectionListener;
import is.hi.hbv601g.brent.holders.ViewHolder;
import is.hi.hbv601g.brent.models.Bike;
import is.hi.hbv601g.brent.R;

public class BikesActivity extends SelectionListener {

    private ArrayList<Bike> mBikes;
    private ArrayList<Bike> mDisplayedBikes;
    private ArrayList<String> mTypes;
    private ArrayList<String> mSizes;
    private ItemListFragment mItemListFragment;
    private final Calendar mStartDate = Calendar.getInstance();
    private final Calendar mEndDate = Calendar.getInstance();
    private static final String mTAG = "BikesActivity";
    private FirebaseFirestore mDB = FirebaseFirestore.getInstance();
    private boolean mDataFetched = false;
    private final static String KEY_BIKES = "Bikes";
    private final static String KEY_TYPES = "Types";
    private final static String KEY_SIZES = "Sizes";
    private final static String KEY_DISPLAYED_BIKES = "Displayed Bikes";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mBikes = savedInstanceState.getParcelableArrayList(KEY_BIKES);
            mTypes = savedInstanceState.getStringArrayList(KEY_TYPES);
            mSizes = savedInstanceState.getStringArrayList(KEY_SIZES);
            mDisplayedBikes = savedInstanceState.getParcelableArrayList(KEY_DISPLAYED_BIKES);
            mDataFetched = true;
        }
        if (this.connected) {
            setUp();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList(KEY_BIKES, mBikes);
        savedInstanceState.putStringArrayList(KEY_SIZES, mSizes);
        savedInstanceState.putStringArrayList(KEY_TYPES, mTypes);
        savedInstanceState.putParcelableArrayList(KEY_DISPLAYED_BIKES, mDisplayedBikes);
    }

    /**
     * Fetches all the data if it hasn't already been fetched.
     */
    @Override
    public void setUp() {
        if (!mDataFetched) {
            setContentView(R.layout.activity_loading);
            super.setUp();
            setSizes();
            fetchData();
        } else {
            setContentView(R.layout.activity_bikes);
            super.setUp();
            setSpinners();
            setDatePickers();
            setBikeList();
        }
    }


    /**
     * Start the BikeActivity to see bike details for the selected bike and/or book the bike.
     *
     * @param bike - The selected Bike to see more details for and/or book.
     */
    @Override
    public void onBikeSelected(Bike bike) {
        Intent intent = new Intent(getApplicationContext(), BikeActivity.class);
        intent.putExtra("bike", bike);
        intent.putExtra("startDate", mStartDate.getTime());
        intent.putExtra("endDate", mEndDate.getTime());
        startActivity(intent);
    }

    /**
     * Creates the fragment for the list of bikes.
     */
    private void setBikeList() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.bikeListContainer);
        if (fragment == null) {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(ItemListFragment.getArgumentKey(), mDisplayedBikes);
            mItemListFragment = new ItemListFragment();
            mItemListFragment.setArguments(bundle);
            fm.beginTransaction().add(R.id.bikeListContainer, mItemListFragment).commit();
        } else {
            mItemListFragment = (ItemListFragment) fragment;
        }
        mItemListFragment.setViewHolderLayout(R.layout.viewholder_center_inside);
    }


    /**
     * Creates the datepickers.
     * Sets an onClickListener for both datepickers - startDate and endDate.
     */
    private void setDatePickers() {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        final EditText startDateText = findViewById(R.id.startDateText);
        final EditText endDateText = findViewById(R.id.endDateText);
        startDateText.setInputType(InputType.TYPE_NULL);
        endDateText.setInputType(InputType.TYPE_NULL);
        mStartDate.setTime(new Date());
        mEndDate.setTime(new Date());
        startDateText.setText(day + "/" + (month + 1) + "/" + year);
        cldr.add(Calendar.DAY_OF_YEAR, 1);
        day = cldr.get(Calendar.DAY_OF_MONTH);
        endDateText.setText(day + "/" + (month + 1) + "/" + year);

        startDateText.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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

        adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, this.mTypes);
        adapter.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item
        );
        types.setAdapter(adapter);
        types.setSelection(adapter.getPosition("All"));

        adapter = new ArrayAdapter<String>
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
                    filterBikes(selectedType, selectedSize);
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
                    filterBikes(selectedType, selectedSize);
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
                }
                mTypes = types;
                fetchBikes();
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            public void onFailure(Exception e) {
                Log.d(mTAG, "Error fetching types");
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
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Bike bike = Bike.toEntity(document.getId(), document.getData());
                    if (bike == null) {
                        Log.d(mTAG, "error");
                    } else {
                        bikes.add(bike);
                    }
                }
                mBikes = bikes;
                mDisplayedBikes = (ArrayList<Bike>) bikes.clone();
                mDataFetched = true;
                setUp();
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            public void onFailure(Exception e) {
                setUp();
                Log.d(mTAG, "Error fetching bikes");
            }
        });
    }


    /**
     * Set sizes for the dropdown spinner used to filter by size.
     */
    private void setSizes() {
        mSizes = new ArrayList<>();
        mSizes.add("All");
        mSizes.add("S");
        mSizes.add("M");
        mSizes.add("L");
    }

    public void filterBikes(String selectedType, String selectedSize) throws InterruptedException {
        ArrayList<Bike> res = new ArrayList<>();
        for (Bike bike : mBikes) {
            Log.d("ItemListFragment", "Bike added");
            if (bike.getType() != null &&
                    (bike.getType().equals(selectedType) && bike.getSize().equals(selectedSize))) {
                res.add(bike);
            } else if (bike.getType() != null &&
                    (bike.getType().equals(selectedType) && selectedSize.equals("All"))) {
                res.add(bike);
            } else if (selectedType.equals("All") && bike.getSize().equals(selectedSize)) {
                res.add(bike);
            } else if (selectedType.equals("All") && selectedSize.equals("All")) {
                res.add(bike);
            }
        }
        Boolean[] initVals = new Boolean[mDisplayedBikes.size()];
        for (int i = 0; i < initVals.length; i += 1) {
            initVals[i] = new Boolean(false);
        }
        ArrayBlockingQueue<Boolean> shouldBeInList = new ArrayBlockingQueue<>(mBikes.size(), true, Arrays.asList(initVals));
        int n = res.size() - 1;
        while (!(n < 0)) {
            Bike bikeInRes = res.get(n);
            boolean bikePresent = false;
            for (int i = 0; i < mDisplayedBikes.size(); i += 1) {
                Bike bike = mDisplayedBikes.get(i);
                if (bike == bikeInRes) {
                    bikePresent = true;
                    Object[] array = shouldBeInList.toArray();
                    Boolean[] vals = Arrays.copyOf(array, array.length, Boolean[].class);
                    vals[i] = new Boolean(true);
                    shouldBeInList = new ArrayBlockingQueue<>(mBikes.size(), true, Arrays.asList(vals));
                    break;
                }
            }
            if (!bikePresent) {
                mDisplayedBikes.add(bikeInRes);
                shouldBeInList.add(new Boolean(true));
            }
            n -= 1;
        }
        n = 0;
        while (shouldBeInList.size() != 0) {
            if (!shouldBeInList.take()) {
                mDisplayedBikes.remove(n);
            } else {
                n += 1;
            }
        }
        mItemListFragment.updateList();
    }
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int index) {
        bindViewHolder(viewHolder, mDisplayedBikes.get(index));
    }

    public static void bindViewHolder(final ViewHolder viewHolder, final Bike bike) {
        viewHolder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.mListener.onBikeSelected(bike);
            }
        });
        viewHolder.mCardTitle.setText(bike.getName() + " - " + bike.getBrand());
        viewHolder.mCardInfo3.setText(bike.getPrice().toString());
        if (bike.getType().equals("Hybrid")) {
            viewHolder.mCardImage.setImageResource(R.drawable.bike_hybrid);
        } else if (bike.getType().equals("Racer")) {
            viewHolder.mCardImage.setImageResource(R.drawable.bike_racer);
        } else if (bike.getType().equals("MTB")) {
            viewHolder.mCardImage.setImageResource(R.drawable.bike_mbk);
        }
    }
}
