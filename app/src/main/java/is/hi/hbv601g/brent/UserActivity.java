package is.hi.hbv601g.brent;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserActivity extends CurrentActivity implements FetchTask.FetchTaskCallback {

    private List<Booking> mBookings = new ArrayList<>();
    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance(FirebaseApp.getInstance());
    private static final String userId = mAuth.getCurrentUser().getUid();
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "ProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.connected) {
            setUp();
        }
    }

    @Override
    public void setUp() {
        setContentView(R.layout.activity_user);
        // Get toolbar in layout (defined in xml file)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Set it as actionbar
        setSupportActionBar(toolbar);

        fetchBookingsFirestore();

        /* Back arrow (Not needed with BRENT Logo)
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }*/
    }

    private void fetchBookingsFirestore() {

        final List<Booking> bookings = new ArrayList<>();
        final List<Bike> bikes = new ArrayList<>();
        final List<Accessory> accessories = new ArrayList<>();
        final List<Tour> tours = new ArrayList<>();

        final Task<QuerySnapshot> task = db.collection("bookings")
                .whereEqualTo("userId", userId).get();

        task.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                // Get bookings
                for (QueryDocumentSnapshot document : task.getResult()) {

                    final Booking booking = EntityParser.bookingToEntity(document.getId(), document.getData());
                    Log.d(TAG, document.getId() + " => " + document.getData());

                    // Get bikes for this booking
                    final Task<QuerySnapshot> taskBikes = db.collection("bookings")
                            .document(document.getId()).collection("bikes").get();

                    taskBikes.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot doc : taskBikes.getResult()) {

                                final Task<DocumentSnapshot> taskBikeDetails = db
                                        .collection("bikes")
                                        .document(doc.getId()).get();

                                taskBikeDetails.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        bikes.add(EntityParser.bikeToEntity(documentSnapshot.getId(), documentSnapshot.getData()));
                                        Log.d(TAG, taskBikeDetails.getResult().getId() + " => " + taskBikeDetails.getResult().getData());
                                    }
                                });

                                booking.setBikes(bikes);
                                // mBookings.add(booking);
                                // setBookings();
                            }


                        }
                    });

                    // Get accessories for this booking
                    final Task<QuerySnapshot> taskAccessories = db.collection("bookings")
                            .document(document.getId()).collection("accessories").get();

                    taskAccessories.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot doc : taskAccessories.getResult()) {

                                final Task<DocumentSnapshot> taskAccessoryDetails = db
                                        .collection("accessories")
                                        .document(doc.getId()).get();

                                taskAccessoryDetails.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        accessories.add(EntityParser.accessoryToEntity(documentSnapshot.getId(), documentSnapshot.getData()));
                                        Log.d(TAG, taskAccessoryDetails.getResult().getId() + " => " + taskAccessoryDetails.getResult().getData());
                                    }
                                });

                                booking.setAccessories(accessories);
                            }


                        }
                    });

                    // Get tours for this booking
                    final Task<QuerySnapshot> taskTours = db.collection("bookings")
                            .document(document.getId()).collection("tours").get();

                    taskTours.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot doc : taskTours.getResult()) {

                                final Task<DocumentSnapshot> taskTourDetails = db
                                        .collection("tours")
                                        .document(doc.getId()).get();

                                taskTourDetails.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        tours.add(EntityParser.tourToEntity(documentSnapshot.getId(), documentSnapshot.getData()));
                                        Log.d(TAG, taskTourDetails.getResult().getId() + " => " + documentSnapshot.getData());
                                    }
                                });

                                booking.setTours(tours);
                                mBookings.add(booking);
                                setBookings();
                            }


                        }
                    });

                    // Booking booking = EntityParser.bookingToEntity(document.getId(), document.getData());
                    // booking.setBikes(bikes);
                    // booking.setAccessories(accessories);
                    // booking.setTours(tours);
                    // mBookings.add(booking);
                }

                // mBookings = bookings;
                // setBookings();
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            public void onFailure(Exception e) {
                Log.d(TAG, "Error fetching booking");
            }
        });
    }

    private void setBookings() {
        Log.d(TAG, mBookings.get(0).getId());
        // TODO: create cards
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
    public void onResultReceived(Map<String, JSONArray> result) {

    }
}
