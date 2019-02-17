package is.hi.hbv601g.brent;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class BikesActivity extends CurrentActivity implements FetchTask.FetchTaskCallback {

    //private List<Bike> bikes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.connected) {
            setUp();
        }
    }

    @Override
    public void setUp() {
        setContentView(R.layout.activity_bikes);
        // Get toolbar in layout (defined in xml file)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Set it as actionbar
        setSupportActionBar(toolbar);
//        ActionBar ab = getSupportActionBar();
        // Set "go back" functionality
//        ab.setDisplayHomeAsUpEnabled(true);
        new FetchTask(this).execute("/types", "/getall");
    }

    @Override
    public void onResultReceived(Map<String,JSONArray> result) {

//        this.bike = result
        System.out.println(result);
    }
}
