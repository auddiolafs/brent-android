package is.hi.hbv601g.brent.services;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import is.hi.hbv601g.brent.activities.user.BookingActivity;
import is.hi.hbv601g.brent.models.Accessory;
import is.hi.hbv601g.brent.models.Bike;
import is.hi.hbv601g.brent.models.Tour;

public class BookingService {

    private FirebaseApp mApp = FirebaseApp.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance(mApp);
    private FirebaseFirestore mDB = FirebaseFirestore.getInstance();
    private String mUserId = mAuth.getCurrentUser().getUid();
    private ArrayList<Bike> mBikes;
    private ArrayList<Tour> mTours;
    private ArrayList<Accessory> mAccessories;
    private BookingActivity bookingActivity;
    private Map<String, Integer> mItemCounters = new HashMap<>();
    private static final String mTAG = "BookingService";

    public BookingService() { }

    public BookingService(BookingActivity bookingActivity) {
        this.bookingActivity = bookingActivity;
    }

    /**
     * Saves a booking by adding it to the Firestore db.
     * @param bikes
     * @param accessories
     * @param tours
     * @param startDate
     * @param endDate
     * @param pickupLocation
     */
    public void saveBooking(final List<Bike> bikes, final List<Accessory> accessories, final List<Tour> tours,
                            Date startDate, Date endDate, String pickupLocation, int price) {

        Map<String, Object> booking = new HashMap<>();
        if (startDate != null) {
            Timestamp startDateTimestamp = new Timestamp(startDate);
            booking.put("startDate", startDateTimestamp);
        }
        if(endDate != null) {
            Timestamp endDateTimestamp = new Timestamp(endDate);
            booking.put("endDate", endDateTimestamp);
        }
        booking.put("userId", mUserId);
        booking.put("pickupLocation", pickupLocation);
        booking.put("price", price);

        Task<DocumentReference> bookingTask =  mDB.collection("bookings").add(booking);

        bookingTask.addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(mTAG, "ID:" + documentReference.getId());

                Map<String, Object> exists = new HashMap<>();
                exists.put("exists", true);

                for (Bike bike : bikes) {
                    mDB.collection("bookings").document(documentReference.getId())
                            .collection("bikes").document(bike.getId())
                            .set(exists);
                }

                for (Tour tour : tours) {
                    mDB.collection("bookings").document(documentReference.getId())
                            .collection("tours").document(tour.getId())
                            .set(exists);
                }

                for (Accessory accessory : accessories) {
                    mDB.collection("bookings").document(documentReference.getId())
                            .collection("accessories").document(accessory.getId())
                            .set(exists);
                }
            }
        });

        bookingTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(mTAG, "Error posting booking");
            }
        });
    }

    /**
     * Deletes a booking in the Firestore db.
     * @param id
     */
    public void deleteBooking(String id) {
        Task<Void> task = mDB.collection("bookings").document(id).delete();
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(mTAG, "Booking deleted");
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(mTAG, "Error trying deleting booking");
            }
        });
    }

    public void fetchBooking(String bookingId) {
        fetchTours(bookingId);
        fetchBikes(bookingId);
        fetchAccessories(bookingId);
    }

    /**
     * Fetches all tours from Firestore db in the booking.
     */
    private void fetchTours(String bookingId) {
        mTours = new ArrayList<>();
        final Task<QuerySnapshot> task = mDB.collection("bookings").
                document(bookingId).collection("tours").get();
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
                bookingActivity.setTours(mTours);
                setCounter("tours", result.size());
            }
        });
    }

    /**
     * Fetches all accessories from Firestore db in the booking.
     */
    private void fetchAccessories(final String bookingId) {
        mAccessories = new ArrayList<>();
        final Task<QuerySnapshot> task = mDB.collection("bookings").
                document(bookingId).collection("accessories").get();
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
                bookingActivity.setAccessories(mAccessories);
                setCounter("accessories", result.size());
            }
        });
    }


    /**
     * Fetches all bikes from Firestore db in the booking.
     */
    private void fetchBikes(final String bookingId) {
        mBikes = new ArrayList<>();
        final Task<QuerySnapshot> task = mDB.collection("bookings").
                document(bookingId).collection("bikes").get();
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
                bookingActivity.setBikes(mBikes);
                setCounter("bikes", result.size());
            }
        });
    }

    private void decrementCounter(String itemKey) {
        Integer val = mItemCounters.get(itemKey);
        val -= 1;
        mItemCounters.put(itemKey, val);
        if (!itemCounterEmpty()) return;
        bookingActivity.setIsDataFetched(true);
        bookingActivity.setUp();
    }

    private void setCounter(String key, int size) {
        mItemCounters.put(key, size);
        if (mItemCounters.keySet().toArray().length == 1 && itemCounterEmpty()) {
            bookingActivity.setIsDataFetched(true);
            bookingActivity.setUp();
        }
    }

    private boolean itemCounterEmpty() {
        Integer val;
        for (String key : mItemCounters.keySet())  {
            val = mItemCounters.get(key);
            if (val != 0) return false;
        }
        return true;
    }
}
