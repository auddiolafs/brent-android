package is.hi.hbv601g.brent;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingService {

    FirebaseApp mApp;
    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userId;
    private static final String TAG = "BookingService";

    public BookingService() {
        mApp = FirebaseApp.getInstance();
        mAuth = FirebaseAuth.getInstance(mApp);
        userId = mAuth.getCurrentUser().getUid();
    }

    public void saveBooking(List<Bike> bikes, List<Accessory> accessories, List<Tour> tours,
                            Date startDate, Date endDate, String pickupLocation) {

        Map<String, Object> booking = new HashMap<>();

    }

    public void cancelBooking(String bookingId) {

    }
}
