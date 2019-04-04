package is.hi.hbv601g.brent.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import is.hi.hbv601g.brent.Cart;
import is.hi.hbv601g.brent.R;
import is.hi.hbv601g.brent.services.BookingService;

public class PaymentActivity extends CurrentActivity {

    private EditText mFullName;
    private EditText mAddress;
    private EditText mCardNumber;
    private EditText mExpiration;
    private EditText mCVC;
    private Cart mCart;

    private BookingService bookingService = new BookingService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.connected) {
            setUp();
        }
    }

    @Override
    public void setUp() {
        setContentView(R.layout.activity_payment);
        super.setUp();

        mFullName = findViewById(R.id.fullNameEdit);
        mAddress = findViewById(R.id.addressEdit);
        mCardNumber = findViewById(R.id.cardNumberEdit);
        mExpiration = findViewById(R.id.expirationEdit);
        mCVC = findViewById(R.id.cvcEdit);
        mCart = Cart.getCart();
        Button payButton = findViewById(R.id.payButton);
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (true) {
                    bookingService.saveBooking(mCart.getBikes(), mCart.getAccessories(), mCart.getTours(), mCart.getStartDate(),
                            mCart.getEndDate(), null);
                    mCart.resetCart();
                    showMessage("Payment successful");
                    Intent homeIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(homeIntent);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, item.getTitle(), Toast.LENGTH_LONG).show();
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
}
