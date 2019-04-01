package is.hi.hbv601g.brent.activities.user;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import is.hi.hbv601g.brent.R;
import is.hi.hbv601g.brent.activities.CurrentActivity;
import is.hi.hbv601g.brent.fragments.BookingsFragment;
import is.hi.hbv601g.brent.fragments.BookingsFragment;
import is.hi.hbv601g.brent.models.Booking;
import is.hi.hbv601g.brent.models.Booking;

public class BookingsActivity extends CurrentActivity implements BookingsFragment.SelectionListener {


    private ArrayList<Booking> mBookings = new ArrayList<>();
    private BookingsFragment mBookingFragment;
    private static final String mTAG = "BookingsActivity";
    private FirebaseFirestore mDB = FirebaseFirestore.getInstance();
    private boolean mDataFetched = false;

    private FirebaseApp mApp;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApp = FirebaseApp.getInstance();
        mAuth = FirebaseAuth.getInstance(mApp);
        user = mAuth.getCurrentUser();

        if (this.connected) {
            setUp();
        }
    }

    /**
     * Fetches the data if it hasn't already been fetched.
     */
    @Override
    public void setUp() {
        if (!mDataFetched) {
            setContentView(R.layout.activity_loading);
            super.setUp();
            fetchBookings();
        } else {
            setContentView(R.layout.activity_bookings);
            super.setUp();
        }
    }

    /**
     * Fetches all bookings from Firestore db, to be displayed in the bookings list.
     */
    private void fetchBookings() {
        final ArrayList<Booking> bookings = new ArrayList<>();

        final Task<QuerySnapshot> task = mDB.collection("bookings")
                .whereEqualTo("user", user.getUid())
                .get();

        task.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                setContentView(R.layout.activity_bookings);

                for (QueryDocumentSnapshot document : task.getResult()) {
                    Booking booking = Booking.toEntity(document.getId(), document.getData());

                    if (booking == null) {
                        Log.d(mTAG, "error");
                    } else {

                            bookings.add(booking);

//                        Log.d(mTAG, document.getId() + " => " + document.getData());
                    }
                }

                Log.d("TE", Integer.toString(bookings.size()));
                mBookings = bookings;
                setBookingList();
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            public void onFailure(Exception e) {
                Log.d(mTAG, "Error fetching bookings");
            }
        });
    }

    /**
     * Creates the fragment for the list of bookings.
     */
    private void setBookingList() {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("bookings", mBookings);
        mBookingFragment = new BookingsFragment();
        mBookingFragment.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.bookingsListContainer, mBookingFragment).commit();
    }


    /**
     * Start the BookingAcvity to see details for the selected booking.
     * @param booking
     */
    @Override
    public void onBookingSelected(Booking booking) {
        Intent intent = new Intent(getApplicationContext(),
                BookingsActivity.class);
        intent.putExtra("booking", booking);
//        intent.putExtra("location", booking.getLocation());
//        intent.putExtra("length", booking.getLength());
        startActivity(intent);
    }
}
