package is.hi.hbv601g.brent;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity {

    final String TAG = "SignIn";

    FirebaseApp mApp;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;

    EditText mEmailEdit;
    EditText mPassword;
    EditText mConfirmPassword;
    EditText mDisplayNameEdit;

    Button mLoginButton;
    TextView mRegisterText;
    TextView mForgotPasswordText;

    ProgressBar mProgressBar;

    boolean mLoginInProgress = true;
    boolean mRegisterInProgress = false;

    String mDisplayName = "Unknown";

    Map<String, Object> user = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Log.e(TAG, "Started");

        initDisplayControls();
        initListeners();
        initFirebase();
    }

    private void initFirebase() {
        mApp = FirebaseApp.getInstance();
        mAuth = FirebaseAuth.getInstance(mApp);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();

                if (user != null) {
                    Log.e(TAG, "User is authenticated : " + user.getEmail());

                    if (mRegisterInProgress) {
                        setDisplayName(user);
                    } else {
                        mDisplayName = user.getDisplayName();
                    }
                    mLoginInProgress = false;
                    mRegisterInProgress = false;

                    finishActivity();
                } else {
                    Log.e(TAG, "No user logged in");
                }
            }
        };
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    private void initDisplayControls() {
        mEmailEdit = findViewById(R.id.emailEdit);
        mPassword = findViewById(R.id.passwordEdit);
        mConfirmPassword = findViewById(R.id.confirmPasswordEdit);
        mDisplayNameEdit = findViewById(R.id.displayName);

        mLoginButton = findViewById(R.id.loginButton);
        mRegisterText = findViewById(R.id.registerText);
        mForgotPasswordText = findViewById(R.id.forgotPasswordText);

        mProgressBar = findViewById(R.id.login_progress);
        mProgressBar.setVisibility(View.INVISIBLE);

        mEmailEdit.setVisibility(View.VISIBLE);
        mPassword.setVisibility(View.VISIBLE);
        mConfirmPassword.setVisibility(View.GONE);
        mDisplayNameEdit.setVisibility(View.GONE);
    }

    private void initListeners() {

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (mLoginInProgress) {
                    String email = mEmailEdit.getText().toString();
                    String password = mPassword.getText().toString();

                    loginUser(email, password);


                } else {
                    String email = mEmailEdit.getText().toString();
                    String password = mPassword.getText().toString();
                    String confirmPassword = mConfirmPassword.getText().toString();
                    mDisplayName = mDisplayNameEdit.getText().toString();

                    registerUser(email, password, mDisplayName);
                }
                mProgressBar.setVisibility(View.VISIBLE);
                mLoginButton.setVisibility(View.GONE);
            }
        });

        mRegisterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!mRegisterInProgress) {

                    mEmailEdit.setVisibility(View.VISIBLE);
                    mPassword.setVisibility(View.VISIBLE);
                    mConfirmPassword.setVisibility(View.VISIBLE);
                    mDisplayNameEdit.setVisibility(View.VISIBLE);
                    mLoginButton.setText("Sign Up");
                    mRegisterText.setText("Have an account? Sign In");
                    mForgotPasswordText.setText("");


                    mRegisterInProgress = true;
                    mLoginInProgress = false;
                } else {
                    mEmailEdit.setVisibility(View.VISIBLE);
                    mPassword.setVisibility(View.VISIBLE);
                    mConfirmPassword.setVisibility(View.GONE);
                    mDisplayNameEdit.setVisibility(View.GONE);
                    mLoginButton.setText("Sign In");
                    mRegisterText.setText("Don't have an account? Sign Up");
                    mForgotPasswordText.setText("Forgot password?");

                    mRegisterInProgress = false;
                    mLoginInProgress = true;
                }

            }
        });
    }

    private void registerUser(final String email, String password, final String displayName) {
        OnCompleteListener<AuthResult> success = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    FirebaseUser tmp = FirebaseAuth.getInstance().getCurrentUser();
                    Log.e(TAG, "User registration successful");
                    user.put("email", email);
                    user.put("displayName", displayName);
                    db.collection("users").document(tmp.getUid())
                            .set(user);
                } else {
                    Log.e(TAG, "User registration reponse but failed");
                    mProgressBar.setVisibility(View.GONE);
                    mLoginButton.setVisibility(View.VISIBLE);
                }
            }
        };

        OnFailureListener fail = new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Registration call failed");
            }
        };

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(success).addOnFailureListener(fail);
    }

    private void logout() {
        mAuth.signOut();
    }

    private void loginUser(String email, String password) {
        OnCompleteListener<AuthResult> success = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.e(TAG, "User logged in");
                } else {
                    Log.e(TAG, "User logged in failed");
                    mProgressBar.setVisibility(View.GONE);
                    mLoginButton.setVisibility(View.VISIBLE);
                }
            }
        };

        OnFailureListener fail = new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Log on user failure");
            }
        };

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(success).addOnFailureListener(fail);

    }

    private void setDisplayName(FirebaseUser user) {
        UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder().setDisplayName(mDisplayName).build();
        user.updateProfile(changeRequest);
    }

    private void finishActivity() {

        Log.e(TAG, "Finishing Sign In Activity");
        Log.e(TAG,"SignIn Returning to main activity");
        mAuth.removeAuthStateListener(mAuthStateListener);

        Intent returningIntent = new Intent();
        returningIntent.putExtra("displayname", mDisplayName);
        setResult(RESULT_OK, returningIntent);

        finish();
    }
}

