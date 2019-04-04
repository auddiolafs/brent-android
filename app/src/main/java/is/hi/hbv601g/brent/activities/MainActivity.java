package is.hi.hbv601g.brent.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import is.hi.hbv601g.brent.R;

public class MainActivity extends CurrentActivity {

    private String mTAG = "MainActivity >> ";
    private FirebaseApp mApp;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private String mDisplayName = "";
//    private ImageButton mToursButton;
//    private ImageButton mRoutesButton;
//    private ImageButton mBikesButton;
    private CardView mToursButton;
    private CardView mRoutesButton;
    private CardView mBikesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme);
        // Basic things which are done in each activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        if(this.connected) {
            setContentView(R.layout.activity_main);
            super.setUp();
            initListeners();
        }
    }

    /**
     * Initializes onClick listeners for the buttons for bikes, tours and routes.
     */
    private void initListeners() {
        mBikesButton = findViewById(R.id.card_view_id);
        mToursButton = findViewById(R.id.card_view_id2);
        mRoutesButton = findViewById(R.id.card_view_id3);


        mBikesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bikesIntent = new Intent(getApplicationContext(), BikesActivity.class);
                startActivity(bikesIntent);
            }
        });

        mToursButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toursIntent = new Intent(getApplicationContext(), ToursActivity.class);
                startActivity(toursIntent);
            }
        });

        mRoutesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent routesIntent = new Intent(getApplicationContext(), RoutesActivity.class);
                startActivity(routesIntent);
            }
        });
    }

}
