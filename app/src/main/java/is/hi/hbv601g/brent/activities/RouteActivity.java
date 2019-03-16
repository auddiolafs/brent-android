package is.hi.hbv601g.brent.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import is.hi.hbv601g.brent.R;
import is.hi.hbv601g.brent.models.Route;

public class RouteActivity extends CurrentActivity {

    ImageButton toolbarProfile;
    ImageButton toolbarHome;
    ImageButton toolbarCart;

    TextView mTitle;
    TextView mLength;
    TextView mLikes;
    TextView mDescription;
    TextView mPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.connected) {
            setUp();
        }
    }

    @Override
    public void setUp() {
        setContentView(R.layout.activity_route);
        // Get toolbar in layout (defined in xml file)
        toolbarProfile = findViewById(R.id.toolbar_profile);
        toolbarProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userIntent = new Intent(getApplicationContext(), UserActivity.class);
                startActivity(userIntent);
            }
        });
        toolbarHome = findViewById(R.id.toolbar_home);
        toolbarHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(home);
            }
        });
        toolbarCart = findViewById(R.id.toolbar_cart);
        toolbarCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cart = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(cart);
            }
        });

        Intent routesActivity_intent = getIntent();
        final Route route = routesActivity_intent.getParcelableExtra("route");

        mTitle = findViewById(R.id.place_location);
        mLength = findViewById(R.id.place_info_1);
        mLikes = findViewById(R.id.place_info_2);
        mDescription = findViewById(R.id.place_description);
        mTitle.setText(route.getLocation());
        mLength.setText(route.getLength());
        mDescription.setText(route.getDescription());
        mLikes.setText(route.getLikes());

    }
}
