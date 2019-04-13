package is.hi.hbv601g.brent.activities.user;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import is.hi.hbv601g.brent.Cart;
import is.hi.hbv601g.brent.R;
import is.hi.hbv601g.brent.activities.BikeActivity;
import is.hi.hbv601g.brent.activities.CancelBookingActivity;
import is.hi.hbv601g.brent.activities.CartActivity;
import is.hi.hbv601g.brent.fragments.BookingListFragment;
import is.hi.hbv601g.brent.fragments.SelectionListener;
import is.hi.hbv601g.brent.models.Accessory;
import is.hi.hbv601g.brent.models.Bike;
import is.hi.hbv601g.brent.models.Booking;
import is.hi.hbv601g.brent.models.Route;
import is.hi.hbv601g.brent.models.Tour;
import is.hi.hbv601g.brent.services.BookingService;

public class BookingActivity extends SelectionListener {

    private Booking mBooking;
    private TextView mBookingTitle;
    private TextView mBookingStartDate;
    private TextView mBookingEndDate;
    private TextView mBookingDuration;
    private TextView mBookingPrice;
    private TextView mCancelBookingButton;
    private TextView mChangeBookingButton;
    private BookingListFragment mBookingFragment;
    private FirebaseFirestore mDB = FirebaseFirestore.getInstance();
    private ArrayList<Route> mRoutes;
    private boolean mDataFetched = false;
    private ArrayList<Bike> mBikes;
    private String mTAG = "BookingActivity";
    private Map<String, Integer> mItemCounters = new HashMap<>();
    private ArrayList<Accessory> mAccessories;
    private ArrayList<Tour> mTours;
    private Cart mCart;
    private BookingService mBookingService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.connected) {
            setUp();
        }
    }

    @Override
    public void setUp() {
        if (!mDataFetched) {
            setContentView(R.layout.activity_loading);
            super.setUp();
            Intent bookingsActivity_intent = getIntent();
            mBooking = bookingsActivity_intent.getParcelableExtra("booking");
            fetchTours();
            fetchBikes();
            fetchAccessories();
        } else {
            setContentView(R.layout.activity_booking);
            super.setUp();
            mBookingTitle = findViewById(R.id.booking_title);
            mBookingStartDate = findViewById(R.id.booking_start_date);
            mBookingEndDate = findViewById(R.id.booking_end_date);
            mBookingDuration = findViewById(R.id.booking_duration);
            mBookingPrice = findViewById(R.id.booking_price);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
            mBookingTitle.setText("Booking Title");
            final Date startDate = mBooking.getStartDate();
            mBookingStartDate.setText(dateFormat.format(startDate));
            Date endDate = mBooking.getEndDate();
            mBookingEndDate.setText(dateFormat.format(endDate));
            mBookingDuration.setText(getDuration(startDate, endDate));
            mBookingPrice.setText("" + mBooking.getPrice());
            setList();
            mCart = Cart.getCart();
            mBookingService = new BookingService();
            mCancelBookingButton = findViewById(R.id.booking_cancel_booking);
            mCancelBookingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), CancelBookingActivity.class);
                    intent.putExtra("booking", mBooking);
                    startActivity(intent);
                }
            });

        }
    }

    private void setList() {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("bikes", mBikes);
        bundle.putParcelableArrayList("tours", mTours);
        bundle.putParcelableArrayList("accessories", mAccessories);
        mBookingFragment = new BookingListFragment();
        mBookingFragment.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.bookingListContainer, mBookingFragment).commit();
    }

    private String getDuration(Date startDate, Date endDate) {
        long milliseconds = startDate.getTime() - endDate.getTime();
        int days = (int)(milliseconds * 10 / (3600 * 24));
        return days + " days";
    }

    /**
     * Fetches all tours from Firestore db in the booking.
     */
    private void fetchTours() {
        mTours = new ArrayList<>();
        final Task<QuerySnapshot> task = mDB.collection("bookings").
                document(mBooking.getId()).collection("tours").get();
        task.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                QuerySnapshot result = task.getResult();
                for (QueryDocumentSnapshot document : result) {
                    final Task<DocumentSnapshot> task = mDB.collection("tours").document(document.getId()).get();
                    task.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Tour tour = Tour.toEntity(documentSnapshot.getId(), documentSnapshot.getData());
                            mTours.add(tour);
                            decrementCounter("tours");
                        }
                    });
                }
                setCounter("tours", result.size());
            }
        });
    }

    /**
     * Fetches all accessories from Firestore db in the booking.
     */
    private void fetchAccessories() {
        mAccessories = new ArrayList<>();
        final Task<QuerySnapshot> task = mDB.collection("bookings").
                document(mBooking.getId()).collection("accessories").get();
        task.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                QuerySnapshot result = task.getResult();
                for (QueryDocumentSnapshot document : result) {
                    final Task<DocumentSnapshot> task = mDB.collection("accessories").document(document.getId()).get();
                    task.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Accessory accessory = Accessory.toEntity(documentSnapshot.getId(), documentSnapshot.getData());
                            mAccessories.add(accessory);
                            decrementCounter("accessories");
                        }
                    });
                }
                setCounter("accessories", result.size());
            }
        });
    }


    /**
     * Fetches all bikes from Firestore db in the booking.
     */
    private void fetchBikes() {
        mBikes = new ArrayList<>();
        final Task<QuerySnapshot> task = mDB.collection("bookings").
                document(mBooking.getId()).collection("bikes").get();
        task.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                QuerySnapshot result = task.getResult();
                for (QueryDocumentSnapshot document : result) {
                    final Task<DocumentSnapshot> task = mDB.collection("bikes").document(document.getId()).get();
                    task.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Bike bike = Bike.toEntity(documentSnapshot.getId(), documentSnapshot.getData());
                            mBikes.add(bike);
                            decrementCounter("bikes");
                        }
                    });
                }
                setCounter("bikes", result.size());
            }
        });
    }

    private void setCounter(String key, int size) {
        mItemCounters.put(key, size);
        if (mItemCounters.keySet().toArray().length == 1 && itemCounterEmpty()) {
            mDataFetched = true;
            setUp();
        }
    }

    private void decrementCounter(String itemKey) {
        Integer val = mItemCounters.get(itemKey);
        val -= 1;
        mItemCounters.put(itemKey, val);
        if (!itemCounterEmpty()) return;
        mDataFetched = true;
        setUp();
    }

    private boolean itemCounterEmpty() {
        Integer val;
        for (String key : mItemCounters.keySet())  {
            val = mItemCounters.get(key);
            if (val != 0) return false;
        }
        return true;
    }


    @Override
    public void onBikeSelected(Bike bike) {
        Intent intent = new Intent(getApplicationContext(), BikeActivity.class);
        intent.putExtra("bike", bike);
        intent.putExtra("startDate", mBooking.getStartDate());
        intent.putExtra("endDate", mBooking.getEndDate());
        startActivity(intent);
    }

}
