package is.hi.hbv601g.brent.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import is.hi.hbv601g.brent.Cart;
import is.hi.hbv601g.brent.R;

public class CartActivity extends CurrentActivity {

    private Cart mCart;

    public static Intent newIntent(BikeActivity bikeActivity) {
        return new Intent(bikeActivity, CartActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.connected) {
            setUp();
        }
    }

    @Override
    public void setUp() {
        mCart = Cart.getCart();
        setContentView(R.layout.activity_cart);
        super.setUp();

        TextView mTotalPrice = findViewById(R.id.totalPriceValueText);
        mTotalPrice.setText(mCart.getTotalPrice().toString() + " kr.");

        Button continueButton = findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent paymentActivity = new Intent(getApplicationContext(), PaymentActivity.class);
                startActivity(paymentActivity);
                // saveCartButton.setClickable(false);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, item.getTitle(), Toast.LENGTH_LONG).show();
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateUI() {
        Intent homeActivity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(homeActivity);
        finish();
    }
}
