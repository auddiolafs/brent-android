package is.hi.hbv601g.brent;

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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingService {

    FirebaseApp mApp = FirebaseApp.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance(mApp);
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userId = mAuth.getCurrentUser().getUid();
    private static final String TAG = "BookingService";

    public void saveBooking(final List<Bike> bikes, final List<Accessory> accessories, final List<Tour> tours,
                            Date startDate, Date endDate, String pickupLocation) {

        Map<String, Object> booking = new HashMap<>();
        Timestamp startDateTimestamp = new Timestamp(startDate);
        Timestamp endDateTimestamp = new Timestamp(endDate);

        booking.put("userId", userId);
        booking.put("startDate", startDateTimestamp);
        booking.put("endDate", endDateTimestamp);
        booking.put("pickupLocation", pickupLocation);

        Task<DocumentReference> bookingTask =  db.collection("bookings").add(booking);

        bookingTask.addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "ID:" + documentReference.getId());

                Map<String, Object> exists = new HashMap<>();
                exists.put("exists", true);

                for (Bike bike : bikes) {
                    db.collection("bookings").document(documentReference.getId())
                            .collection("bikes").document(bike.getId())
                            .set(exists);
                }

                for (Tour tour : tours) {
                    db.collection("bookings").document(documentReference.getId())
                            .collection("tours").document(tour.getId())
                            .set(exists);
                }

                for (Accessory accessory : accessories) {
                    db.collection("bookings").document(documentReference.getId())
                            .collection("accessories").document(accessory.getId())
                            .set(exists);
                }
            }
        });

        bookingTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error posting booking");
            }
        });
    }

    public void cancelBooking(String bookingId) {

    }
}
