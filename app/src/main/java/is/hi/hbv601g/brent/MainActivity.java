package is.hi.hbv601g.brent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.FirebaseApp;

import org.json.JSONArray;

public class MainActivity extends CurrentActivity implements FetchTask.FetchTaskCallback, View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Basic things which are done in each activity
        super.onCreate(savedInstanceState);

        FirebaseApp.initializeApp(this);

        if(this.connected) {
            setUp();
        }

        findViewById(R.id.textViewSignUp).setOnClickListener(this);
    }

    public void setUp() {

        setContentView(R.layout.activity_main);
        // Get toolbar in layout (defined in xml file)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Set it as actionbar
        setSupportActionBar(toolbar);


        // Remove label/projectName/title from actionbar
        //getSupportActionBar().setDisplayShowTitleEnabled(false);


    }

    @Override
    public void onResultReceived(JSONArray result) {
        //System.out.println(result);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textViewSignUp:
                startActivity(new Intent(this, SignUpActivity.class));
                break;
        }
    }
}
