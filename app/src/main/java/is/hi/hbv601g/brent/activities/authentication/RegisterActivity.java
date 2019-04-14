package is.hi.hbv601g.brent.activities.authentication;

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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import is.hi.hbv601g.brent.activities.MainActivity;
import is.hi.hbv601g.brent.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText mUserEmail;
    private EditText mUserPassword;
    private EditText mUserPasswordConfirm;
    private EditText mUserName;
    private TextView mRegisterText;
    private ProgressBar mLoadingProgress;
    private Button mRegBtn;
    private Map<String, Object> mUser = new HashMap<>();

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mDB = FirebaseFirestore.getInstance();
    private String mTAG = "Register >>";

    /**
     * Initializes instance variables.
     * Sets onClick listener for register button with input validations checks.
     * Sets onClick listener for the register text which starts the login activity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUserEmail = findViewById(R.id.emailEdit);
        mUserPassword = findViewById(R.id.passwordEdit);
        mUserPasswordConfirm = findViewById(R.id.confirmPasswordEdit);
        mUserName = findViewById(R.id.displayName);
        mLoadingProgress = findViewById(R.id.login_progress);
        mRegisterText = findViewById(R.id.registerText);
        mRegBtn = findViewById(R.id.loginButton);
        mLoadingProgress.setVisibility(View.INVISIBLE);

        mRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRegBtn.setVisibility(View.INVISIBLE);
                mLoadingProgress.setVisibility(View.VISIBLE);
                final String email = mUserEmail.getText().toString();
                final String password = mUserPassword.getText().toString();
                final String password2 = mUserPasswordConfirm.getText().toString();
                final String name = mUserName.getText().toString();

                if (email.isEmpty() || name.isEmpty() || password.isEmpty() || password2.isEmpty()) {
                    showMessage("Check fields");
                    mRegBtn.setVisibility(View.VISIBLE);
                    mLoadingProgress.setVisibility(View.INVISIBLE);
                } else if (password.length() < 8) {
                    showMessage("Passwords must be at least 8 chars!");
                    mRegBtn.setVisibility(View.VISIBLE);
                    mLoadingProgress.setVisibility(View.INVISIBLE);
                } else if (!password.equals(password2)) {
                    showMessage("Passwords don't match!");
                    mRegBtn.setVisibility(View.VISIBLE);
                    mLoadingProgress.setVisibility(View.INVISIBLE);
                } else {
                    CreateUserAccount(email, name, password);
                }
            }
        });

        mRegisterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(loginActivity);
                finish();
            }
        });
    }

    /**
     * Registers a user - saves the email, name and password in the Firebase db.
     * @param email
     * @param name
     * @param password
     */
    private void CreateUserAccount(String email, final String name, String password) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    showMessage("Success");
                    updateUserInfo(name, mAuth.getCurrentUser());
                } else {
                    showMessage("error " + task.getException().getMessage());
                    mRegBtn.setVisibility(View.VISIBLE);
                    mLoadingProgress.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    /**
     * Updates the name of the currently logged in user
     * @param name
     * @param currentUser
     */
    private void updateUserInfo(String name, FirebaseUser currentUser) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        currentUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(mTAG, "SUCCESS");
                            finish();
                        }
                    }
                });

        Log.e(mTAG, "User registration successful");
        mUser.put("email", currentUser.getEmail());
        mUser.put("displayName", name);
        mDB.collection("users").document(currentUser.getUid()).set(mUser);
    }


    /**
     * Creates a toast message.
     * @param message
     */
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
