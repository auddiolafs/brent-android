package is.hi.hbv601g.brent.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import is.hi.hbv601g.brent.R;
import is.hi.hbv601g.brent.activities.authentication.LoginActivity;

public class MainActivity extends CurrentActivity {

    private String mTAG = "MainActivity >> ";
    private FirebaseApp mApp;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private String mDisplayName = "";
    private ImageButton mToursButton;
    private ImageButton mRoutesButton;
    private ImageButton mBikesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme);

        // Basic things which are done in each activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFirebase();
        initListeners();

        if(this.connected) {
            super.setUp();
        }
    }

    /**
     * Initializes the Firebase connection for this app.
     */
    private void initFirebase() {
        mApp = FirebaseApp.getInstance();
        mDatabase = FirebaseDatabase.getInstance(mApp);
        mAuth = FirebaseAuth.getInstance(mApp);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();

                if (user != null) {
                    Log.e(mTAG, "AUTH SATE UPDATE : Valid User Login\0");
                    String displayName = user.getDisplayName();

                    if (displayName !=null) {
                        mDisplayName = displayName;
                    } else {
                        mDisplayName = "Unknown DisplayName";
                    }
                } else {
                    Log.e(mTAG, "AUTH STATE UPDATE : No user logged in");
                    mDisplayName = "No valid user";

                    Intent signInIntent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivityForResult(signInIntent, 101);
                }
            }
        };
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    /**
     * Initializes onClick listeners for the buttons for bikes, tours and routes.
     */
    private void initListeners() {
        mBikesButton = findViewById(R.id.bikeButton);
        mToursButton = findViewById(R.id.toursButton);
        mRoutesButton = findViewById(R.id.routesButton);


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

    /**
     * Gets the result from the subsequent activity when returning to this activity.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == RESULT_OK) {

            mDisplayName = data.getStringExtra("displayname");
            Log.e(mTAG, "Returned Activity display name: [" + mDisplayName + "]");
            mAuth.addAuthStateListener(mAuthStateListener);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
