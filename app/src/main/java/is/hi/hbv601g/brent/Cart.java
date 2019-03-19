package is.hi.hbv601g.brent;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import is.hi.hbv601g.brent.models.Accessory;
import is.hi.hbv601g.brent.models.Bike;
import is.hi.hbv601g.brent.models.Booking;
import is.hi.hbv601g.brent.models.Tour;

public class Cart {

    private static Cart mCart = new Cart();
    private static Long mCartID = new Long(1);
    private static final FirebaseFirestore mDB = FirebaseFirestore.getInstance();
    private static final String TAG = "CartActivity";
    private Map<Long, Booking> mBookings;

    private Cart() {
        mBookings = new HashMap();
    }

    public static Cart getCart() {
        return mCart;
    }

    public void putBooking(Booking booking) {
        mBookings.put(mCartID, booking);
        mCartID += 1;
    }

    public void removeBooking(Long bookingId) {
        mBookings.remove(bookingId);
        mCartID -= 1;
    }

    public boolean contains(Date startDate, Date endDate) {
        Iterator it = mBookings.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String key = pair.getKey().toString();
            Booking booking = (Booking) pair.getValue();
            if (booking.getStartDate() == startDate && booking.getEndDate() == endDate) return true;
        }
        return false;
    }

    public Booking popBooking(Date startDate, Date endDate) {
        Iterator it = mBookings.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String key = pair.getKey().toString();
            Booking booking = (Booking) pair.getValue();
            if (booking.getStartDate() == startDate && booking.getEndDate() == endDate)
                return booking;
        }
        return new Booking();
    }

    public void saveCart() {
        Iterator it = mBookings.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Booking booking = (Booking) pair.getValue();
            saveBookingToFirebase(booking);
            removeBooking(mCartID);
        }
    }

    private void saveBookingToFirebase(Booking booking) {
        Map<String, Object> data = new HashMap<>();
        Log.d("Cart.java", "Enter saveBooking");
        data.put("startDate", booking.getStartDate());
        data.put("endDate", booking.getEndDate());
        data.put("pickupLocation", booking.getPickupLocation());
        mDB.collection("bookings")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        FirebaseAuth auth = FirebaseAuth.getInstance(FirebaseApp.getInstance());
                        FirebaseUser user = auth.getCurrentUser();
                        documentReference.update("user", user.getUid());
                        // Get bike, tours and accessories and add
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public void addToCart(List<Bike> bikes, List<Accessory> accessories, List<Tour>tours) {

    }
}
