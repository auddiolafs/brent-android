package is.hi.hbv601g.brent.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Date;

import is.hi.hbv601g.brent.models.Bike;
import is.hi.hbv601g.brent.models.Booking;
import is.hi.hbv601g.brent.Cart;
import is.hi.hbv601g.brent.R;

public class BikeActivity extends CurrentActivity {

    private TextView mBrand;
    private TextView mType;
    private TextView mPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.connected) {
            setUp();
        }
    }

    /**
     * Sets the information for selected bike and displays the bike details.
     * Sets onClick listener for the button to book the bike.
     */
    @Override
    public void setUp() {
        setContentView(R.layout.activity_bike);
        super.setUp();

        Intent bikesActivity_intent = getIntent();
        final Bike bike = bikesActivity_intent.getParcelableExtra("bike");
        final Date startDate = (Date) bikesActivity_intent.getSerializableExtra("startDate");
        final Date endDate = (Date) bikesActivity_intent.getSerializableExtra("endDate");

        mBrand = findViewById(R.id.bike_brand_id);
        mPrice = findViewById(R.id.bike_price_id);
        mBrand.setText(bike.getBrand());
        mPrice.setText(Long.toString(bike.getPrice()));
//      Currently not working, gives error
//      mType.setText(bike.getType());

        TextView bookButton= findViewById(R.id.buttonBooking);
        final Intent cartActivity_intent = CartActivity.newIntent(this);
        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cart cart = Cart.getCart();
                if (!cart.contains(startDate, endDate)) {
                    Booking booking = new Booking(startDate, endDate);
                    booking.addBike(bike);
                    cart.putBooking(booking);
                }
                startActivity(cartActivity_intent);
            }
        });
    }
}
