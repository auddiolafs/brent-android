package is.hi.hbv601g.brent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONArray;

public class BikesActivity extends CurrentActivity implements FetchTask.FetchTaskCallback {

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
        new FetchTask(this).execute("/requests");
    }

    @Override
    public void onResultReceived(JSONArray result) {
        System.out.println(result);
    }
}
