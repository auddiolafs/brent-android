package is.hi.hbv601g.brent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Date;

public class BikeActivity extends CurrentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.connected) {
            setUp();
        }
    }

    @Override
    public void setUp() {
        setContentView(R.layout.activity_bike);
        // Get toolbar in layout (defined in xml file)
        Intent bikesActivity_intent = getIntent();
        final Bike bike = bikesActivity_intent.getParcelableExtra("bike");
        final Date startDate = (Date) bikesActivity_intent.getSerializableExtra("startDate");
        final Date endDate = (Date) bikesActivity_intent.getSerializableExtra("endDate");
        Button bookButton= findViewById(R.id.bookButton);
        final Intent cartActivity_intent = new Intent(this, CartActivity.class);
        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cart cart = Cart.getCart();
                if (!cart.contains(startDate, endDate)) {
                    Booking booking = new Booking(startDate, endDate);
                    booking.setId(Booking.mTempUUID);
                    Booking.incrementUUID();
                    booking.addBike(bike);
                    cart.putBooking(booking);
                }
                startActivity(cartActivity_intent);
            }
        });
    }
}
