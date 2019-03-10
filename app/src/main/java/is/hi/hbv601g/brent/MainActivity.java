package is.hi.hbv601g.brent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class MainActivity extends CurrentActivity implements FetchTask.FetchTaskCallback {


    String TAG = "MainActivity >> ";

    FirebaseApp mApp;
    FirebaseDatabase mDatabase;
    FirebaseAuth mAuth;

    FirebaseAuth.AuthStateListener mAuthStateListener;
    String mDisplayName = "";
    TextView mLogoutEdit;
    TextView mBikesButton;
    TextView mToursButton;
    TextView mRoutesButton;

    ImageButton toolbarProfile;
    ImageButton toolbarHome;
    ImageButton toolbarCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme);

        // Basic things which are done in each activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFirebase();
        initListeners();
        if(this.connected) {
            setUp();
        }
    }

    public void setUp() {
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

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    private void initFirebase() {
        mApp = FirebaseApp.getInstance();
        mDatabase = FirebaseDatabase.getInstance(mApp);
        mAuth = FirebaseAuth.getInstance(mApp);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();

                if (user != null) {
                    Log.e(TAG, "AUTH SATE UPDATE : Valid User Login\0");
                    String displayName = user.getDisplayName();

                    if (displayName !=null) {
                        mDisplayName = displayName;
                    } else {
                        mDisplayName = "Unknown DisplayName";
                    }
                } else {
                    Log.e(TAG, "AUTH STATE UPDATE : No user logged in");
                    mDisplayName = "No valid user";

                    Intent signInIntent = new Intent(getApplicationContext(), is.hi.hbv601g.brent.Activities.LoginActivity.class);
                    startActivityForResult(signInIntent, 101);
                }
            }
        };
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    private void initListeners() {
        mLogoutEdit = findViewById(R.id.logoutText);
        mBikesButton = findViewById(R.id.bikeButton);
        mToursButton = findViewById(R.id.toursButton);
        mRoutesButton = findViewById(R.id.routesButton);

        mLogoutEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
            }
        });

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == RESULT_OK) {

            mDisplayName = data.getStringExtra("displayname");
            Log.e(TAG, "Returned Activity display name: [" + mDisplayName + "]");
            mAuth.addAuthStateListener(mAuthStateListener);

        }
    }

    @Override
    public void onResultReceived(Map<String,JSONArray> result) {
        System.out.println(result);
    }
}
