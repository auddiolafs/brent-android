package is.hi.hbv601g.brent.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import is.hi.hbv601g.brent.Cart;
import is.hi.hbv601g.brent.R;
import is.hi.hbv601g.brent.activities.user.BookingsActivity;
import is.hi.hbv601g.brent.models.Booking;
import is.hi.hbv601g.brent.services.BookingService;

public class CancelBookingActivity extends CurrentActivity {

    private TextView mFullName;
    private TextView mAddress;
    private TextView mCardNumber;
    private TextView mExpiration;
    private TextView mCVC;
    private Cart mCart;
    private BookingService mBookingService;
    private Booking mBooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.connected) {
            setUp();
        }
    }

    @Override
    public void setUp() {
        setContentView(R.layout.activity_cancel_booking);
        super.setUp();
        mBookingService = new BookingService();
        mFullName = findViewById(R.id.fullNameEdit);
        mAddress = findViewById(R.id.addressEdit);
        mCardNumber = findViewById(R.id.cardNumberEdit);
        mExpiration = findViewById(R.id.expirationEdit);
        mCVC = findViewById(R.id.cvcEdit);
        mCart = Cart.getCart();
        Intent intent = getIntent();
        mBooking = intent.getParcelableExtra("booking");
        Button cancelButton = findViewById(R.id.booking_confirm_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (true) {
                    mBookingService.deleteBooking(mBooking.getId());
                    showMessage("Booking deleted!");
                    Intent bookingsIntent = new Intent(getApplicationContext(), BookingsActivity.class);
                    bookingsIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(bookingsIntent);

                } else {
                    showMessage("Invalid credit card information");
                }
            }
        });
    }

    private boolean validateInputs() {
        String visaPattern = "^4[0-9]{6,}$";
        String expirationDatePattern = "(?:0[1-9]|1[0-2])/[0-9]{2}";
        if (mCardNumber.toString().matches(visaPattern) && mCVC.length() == 3
                && mExpiration.toString().matches(expirationDatePattern)) {

            return true;
        }

        return false;
    }

    /**
     * Creates a toast message.
     * @param message
     */
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
