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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.WriteResult;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import is.hi.hbv601g.brent.models.Accessory;
import is.hi.hbv601g.brent.models.Bike;
import is.hi.hbv601g.brent.models.Tour;

public class BookingService {

    private FirebaseApp mApp = FirebaseApp.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance(mApp);
    private FirebaseFirestore mDB = FirebaseFirestore.getInstance();
    private String mUserId = mAuth.getCurrentUser().getUid();
    private static final String mTAG = "BookingService";

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
                            Date startDate, Date endDate, String pickupLocation) {

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
}
