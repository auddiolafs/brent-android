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
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;

public class MainActivity extends CurrentActivity implements FetchTask.FetchTaskCallback {


    String TAG = "ChatApp >> ";

    FirebaseApp mApp;
    FirebaseDatabase mDatabase;
    FirebaseAuth mAuth;

    FirebaseAuth.AuthStateListener mAuthStateListener;
    String mDisplayName = "";
    TextView mLogoutEdit;

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

        // Get toolbar in layout (defined in xml file)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Set it as actionbar
        setSupportActionBar(toolbar);

//        new FetchTask(this).execute("/requests");
        // Remove label/projectName/title from actionbar
        //getSupportActionBar().setDisplayShowTitleEnabled(false);


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

                    Intent signInIntent = new Intent(getApplicationContext(), SignInActivity.class);
                    startActivityForResult(signInIntent, 101);
                }
            }
        };
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    private void initListeners() {
        mLogoutEdit = findViewById(R.id.logoutText);

        mLogoutEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
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
    public void onResultReceived(JSONArray result) {
        System.out.println(result);
    }
}
