package is.hi.hbv601g.brent.activities.authentication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import is.hi.hbv601g.brent.R;
import is.hi.hbv601g.brent.services.AuthenticationService;

public class RegisterActivity extends AppCompatActivity {

    private EditText mUserEmail;
    private EditText mUserPassword;
    private EditText mUserPasswordConfirm;
    private EditText mUserName;
    private TextView mRegisterText;
    private ProgressBar mLoadingProgress;
    private Button mRegBtn;

    private AuthenticationService authService = new AuthenticationService(this);

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
                    authService.CreateUserAccount(email, name, password);
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
     * Creates a toast message.
     * @param message
     */
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    public void onRegisterSuccess(String name, FirebaseUser user) {
        showMessage("Success");
        authService.updateUserInfo(name, user);
    }

    public void onRegisterError(String errorMessage) {
        showMessage("error " + errorMessage);
        mRegBtn.setVisibility(View.VISIBLE);
        mLoadingProgress.setVisibility(View.INVISIBLE);
    }
}
