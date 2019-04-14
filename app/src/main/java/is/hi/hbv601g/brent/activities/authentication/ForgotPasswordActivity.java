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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import is.hi.hbv601g.brent.activities.MainActivity;
import is.hi.hbv601g.brent.R;
import is.hi.hbv601g.brent.services.AuthenticationService;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText mUserEmail;
    private TextView mRegisterText;
    private TextView mForgotPassword;
    private ProgressBar mLoadingProgress;
    private Button mBtnLogin;
    private FirebaseAuth firebaseAuth;
    private AuthenticationService authService = new AuthenticationService(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        initDisplayControls();
        initListeners();
    }

    /**
     * Initializes display controls - input fields, buttons, etc.
     */
    private void initDisplayControls() {
        mUserEmail = findViewById(R.id.emailEdit);
        mLoadingProgress = findViewById(R.id.login_progress);
        mRegisterText = findViewById(R.id.registerText);
        mForgotPassword = findViewById(R.id.forgotPasswordText);
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

                Log.d("WOW", mUserEmail.getText().toString());
                authService.forgotPassword(mUserEmail.getText().toString());

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
//        FirebaseUser user = authService.getCurrentUser();
//
//        if (user != null) {
//            updateUI();
//        }
    }

    public void onPasswordSuccess() {
        Log.d("WOW", "HELLO");
        Intent homeIntent= new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(homeIntent);
        finish();
//        mLoadingProgress.setVisibility(View.INVISIBLE);
//        mBtnLogin.setVisibility(View.VISIBLE);
//        updateUI();
    }

    public void onPasswordFail(String errorMessage) {
        showMessage(errorMessage);
        mBtnLogin.setVisibility(View.VISIBLE);
        mLoadingProgress.setVisibility(View.INVISIBLE);
    }

    private void showMessage(String text) {
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();
    }
}
