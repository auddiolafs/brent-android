package is.hi.hbv601g.brent;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Date;

public class BookingService {

    FirebaseApp mApp;
    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "BookingService";

    public BookingService() {
        mApp = FirebaseApp.getInstance();
        mAuth = FirebaseAuth.getInstance(mApp);
        FirebaseUser user = mAuth.getCurrentUser();

    }

    public void saveBooking(List<Bike> bikes, List<Accessory> accessories, List<Tour> tours,
                            Date startDate, Date endDate, String pickupLocation) {

    }

    public void cancelBooking(String bookingId) {

    }
}
