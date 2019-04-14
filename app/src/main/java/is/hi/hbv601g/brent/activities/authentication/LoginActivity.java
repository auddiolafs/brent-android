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

import is.hi.hbv601g.brent.activities.MainActivity;
import is.hi.hbv601g.brent.R;
import is.hi.hbv601g.brent.services.AuthenticationService;

public class LoginActivity extends AppCompatActivity {

    private EditText mUserEmail;
    private EditText mUserPassword;
    private TextView mRegisterText;
    private ProgressBar mLoadingProgress;
    private Button mBtnLogin;
    private AuthenticationService authService = new AuthenticationService(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initDisplayControls();
        initListeners();
    }

    /**
     * Initializes display controls - input fields, buttons, etc.
     */
    private void initDisplayControls() {
        mUserEmail = findViewById(R.id.emailEdit);
        mUserPassword = findViewById(R.id.passwordEdit);
        mLoadingProgress = findViewById(R.id.login_progress);
        mRegisterText = findViewById(R.id.registerText);
        mBtnLogin = findViewById(R.id.loginButton);
        mLoadingProgress.setVisibility(View.INVISIBLE);
    }

    /**
     * Initializes listeners for login button and register text.
     */
    private void initListeners() {
        mRegisterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerActivity = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(registerActivity);
                finish();
            }
        });

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadingProgress.setVisibility(View.VISIBLE);
                mBtnLogin.setVisibility(View.INVISIBLE);

                final String mail = mUserEmail.getText().toString();
                final String password = mUserPassword.getText().toString();

                if (mail.isEmpty() || password.isEmpty()) {
                    showMessage("Please verify all fields");
                    mBtnLogin.setVisibility(View.VISIBLE);
                    mLoadingProgress.setVisibility(View.INVISIBLE);
                } else {
                    authService.signIn(mail, password);
                }
            }
        });
    }

    /**
     * Starts the HomeActivity.
     */
    private void updateUI() {
        Intent homeIntent= new Intent(getApplicationContext(), MainActivity.class);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
        finish();
    }

    /**
     * Checks if some user us logged in and redirects him to home.
     */
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = authService.getCurrentUser();

        if (user != null) {
            updateUI();
        }
    }

    public void onLoginSuccess() {
        mLoadingProgress.setVisibility(View.INVISIBLE);
        mBtnLogin.setVisibility(View.VISIBLE);
        updateUI();
    }

    public void onLoginError(String errorMessage) {
        showMessage(errorMessage);
        mBtnLogin.setVisibility(View.VISIBLE);
        mLoadingProgress.setVisibility(View.INVISIBLE);
    }

    private void showMessage(String text) {
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();
    }
}
