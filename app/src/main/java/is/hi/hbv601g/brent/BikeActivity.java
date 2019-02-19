package is.hi.hbv601g.brent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;


import org.json.JSONObject;


public class BikeActivity extends CurrentActivity implements HttpService.HttpServiceCallback  {

    private HttpService httpService;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Set it as actionbar
        setSupportActionBar(toolbar);
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
