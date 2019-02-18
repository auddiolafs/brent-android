package is.hi.hbv601g.brent;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Set it as actionbar
        setSupportActionBar(toolbar);
        Intent bikesActivity_intent = getIntent();
        String s = bikesActivity_intent.getStringExtra("args");
        Bike bike = null;
        try {
            JSONObject obj = new JSONObject(s);
            bike = (Bike) obj.get("bike");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final Bike selectedBike = bike;
        Button selectButton = (Button)findViewById(R.id.selectButton);
        final Intent checkoutActivity_intent = new Intent(this, CheckoutActivity.class);
        final JSONObject args = new JSONObject();
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    args.put("bike", selectedBike);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                checkoutActivity_intent.putExtra("args", args.toString());
                startActivity(checkoutActivity_intent);
            }
        });
    }
}
