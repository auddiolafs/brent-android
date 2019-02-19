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

import com.google.logging.type.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

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
        final Intent checkoutActivity_intent = new Intent(this, CheckoutActivity.class);
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkoutActivity_intent.putExtra("bike", bike);
                startActivity(checkoutActivity_intent);

            }
        });
    }


    @Override
    public void onResultReceived(JSONObject result) {
        System.out.println(result);
    }
}
