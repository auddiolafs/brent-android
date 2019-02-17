package is.hi.hbv601g.brent;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

// Check comments in MainActivity for explanation
public class AboutActivity extends CurrentActivity implements FetchTask.FetchTaskCallback {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Basic things which are done in each activity
        super.onCreate(savedInstanceState);
        if(this.connected) {
            setUp();
        }
    }

    public void setUp() {

        setContentView(R.layout.activity_about);
        // Get toolbar in layout (defined in xml file)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Set it as actionbar
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        // Set "go back" functionality
        ab.setDisplayHomeAsUpEnabled(true);


        // Remove label/projectName/title from actionbar
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    @Override
    public void onResultReceived(List<JSONObject> result) {
        //System.out.println(result);
    }


}
