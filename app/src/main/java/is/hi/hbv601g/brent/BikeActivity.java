package is.hi.hbv601g.brent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


import org.json.JSONObject;


public class BikeActivity extends CurrentActivity implements HttpService.HttpServiceCallback  {

    private HttpService httpService;

    ImageButton toolbarProfile;
    ImageButton toolbarHome;
    ImageButton toolbarCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.connected) {
            setUp();
        }
        httpService = new HttpService(this);
    }

    @Override
    public void setUp() {
        setContentView(R.layout.activity_bike);
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


        Intent bikesActivity_intent = getIntent();
        final Bike bike = (Bike) bikesActivity_intent.getSerializableExtra("bike");
        Button selectButton = findViewById(R.id.selectButton);
        final Intent cartActivity_intent = new Intent(this, CartActivity.class);
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartActivity_intent.putExtra("bike", bike);
                startActivity(cartActivity_intent);

            }
        });
    }


    @Override
    public void onResultReceived(JSONObject result) {
        System.out.println(result);
    }
}
