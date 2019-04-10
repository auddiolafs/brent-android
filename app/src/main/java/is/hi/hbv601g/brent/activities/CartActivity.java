package is.hi.hbv601g.brent.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static java.lang.Math.toIntExact;

import java.util.List;

import is.hi.hbv601g.brent.Cart;
import is.hi.hbv601g.brent.R;
import is.hi.hbv601g.brent.models.Bike;

public class CartActivity extends CurrentActivity {

    private Cart mCart;
    private Bike mBikes;
    private List aList = new ArrayList();

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

        List<Bike> bikes = mCart.getBikes();

        Map<String, Integer> list = new HashMap<>();
        Map<String, Long> priceList = new HashMap<>();

        for (int i = 0; i < bikes.size(); i++) {
            Integer count = list.get(bikes.get(i).getId());
            if (count == null) {
                list.put(bikes.get(i).getId(), 1);
                priceList.put(bikes.get(i).getId(), bikes.get(i).getPrice());
            } else {
                count += 1;
                list.put(bikes.get(i).getId(), count);
                priceList.put(bikes.get(i).getId(), count * bikes.get(i).getPrice());
            }
        }

        for (Map.Entry<String, Long> entry : priceList.entrySet()) {
            Log.d("Cart", entry.getKey() + " / " + entry.getValue());
        }


        setContentView(R.layout.activity_cart);
        super.setUp();

        TextView mTotalPrice = findViewById(R.id.totalPriceValueText);
        mTotalPrice.setText(mCart.getTotalPrice().toString() + " kr.");

        TextView continueButton = findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent accessoriesActivity = new Intent(getApplicationContext(), AccessoriesActivity.class);
                startActivity(accessoriesActivity);
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
