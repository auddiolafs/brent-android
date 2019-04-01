package is.hi.hbv601g.brent.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import is.hi.hbv601g.brent.R;
import is.hi.hbv601g.brent.activities.authentication.LoginActivity;

public class SplashScreenActivity extends AppCompatActivity {

    private FirebaseApp mApp;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private String mTAG = "SplashScreenActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        initFirebase();
        finish();
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
                    Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                } else {
                    Log.e(mTAG, "AUTH STATE UPDATE : No user logged in");
                    Intent logInIntent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(logInIntent);
                }
            }
        };
        mAuth.addAuthStateListener(mAuthStateListener);
    }

}
