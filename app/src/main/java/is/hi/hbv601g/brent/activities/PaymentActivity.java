package is.hi.hbv601g.brent.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import is.hi.hbv601g.brent.Cart;
import is.hi.hbv601g.brent.R;

public class PaymentActivity extends CurrentActivity {

    private EditText mFullName;
    private EditText mAddress;
    private EditText mCardNumber;
    private EditText mExpiration;
    private EditText mCVC;
    private Cart mCart;

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
        // mCart = Cart.getCart();
        // Button saveCartButton = findViewById(R.id.saveCartButton);
        /*saveCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCart.saveCart();
            }
        });*/
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
