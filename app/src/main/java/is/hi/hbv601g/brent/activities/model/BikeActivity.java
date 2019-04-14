package is.hi.hbv601g.brent.activities.model;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;

import is.hi.hbv601g.brent.activities.CurrentActivity;
import is.hi.hbv601g.brent.models.Bike;
import is.hi.hbv601g.brent.models.Cart;
import is.hi.hbv601g.brent.R;

public class BikeActivity extends CurrentActivity {

    private TextView mTitle;
    private TextView mBrand;
    private TextView mType;
    private TextView mPrice;
    private TextView mName;
    private TextView mSize;
    private ImageView mImage;

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

        setBikeDetails(bike);

        TextView bookButton= findViewById(R.id.buttonBooking);
        final Intent cartActivity_intent = CartActivity.newIntent(this);
        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cart cart = Cart.getCart();
                cart.setStartDate(startDate);
                cart.setEndDate(endDate);
                cart.addBikeToCart(bike);
                startActivity(cartActivity_intent);
            }
        });
    }

    private void setBikeDetails(Bike bike) {
        mTitle = findViewById(R.id.bike_title_id);
        mBrand = findViewById(R.id.bike_brand_id);
        mType = findViewById(R.id.bike_type_id);
        mPrice = findViewById(R.id.bike_price_id);
        mName = findViewById(R.id.bike_name_id);
        mSize = findViewById(R.id.bike_size_id);
        mImage = findViewById(R.id.bike_image);
        mTitle.setText(bike.getName() + " - " + bike.getBrand());
        mBrand.setText(bike.getBrand());
        mType.setText(bike.getType());
        mPrice.setText(bike.getPrice() + " ISK");
        mName.setText(bike.getName());
        mSize.setText(bike.getSize());
        if (bike.getType().equals("Hybrid")) {
            mImage.setImageResource(R.drawable.bike_hybrid);
        } else if (bike.getType().equals("Racer")) {
            mImage.setImageResource(R.drawable.bike_racer);
        }
    }
}
