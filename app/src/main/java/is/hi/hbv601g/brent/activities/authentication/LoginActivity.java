package is.hi.hbv601g.brent.activities.authentication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import is.hi.hbv601g.brent.activities.MainActivity;
import is.hi.hbv601g.brent.R;

public class LoginActivity extends AppCompatActivity {

    private EditText mUserEmail;
    private EditText mUserPassword;
    private TextView mRegisterText;
    private ProgressBar mLoadingProgress;
    private Button mBtnLogin;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initDisplayControls();
        initListeners();


        mAuth = FirebaseAuth.getInstance();
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
                    signIn(mail, password);
                }
            }
        });
    }

    /**
     * Signs a user in with Firebase.
     * @param mail
     * @param password
     */
    private void signIn(String mail, String password) {
        mAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    mLoadingProgress.setVisibility(View.INVISIBLE);
                    mBtnLogin.setVisibility(View.VISIBLE);
                    updateUI();
                } else {
                    showMessage(task.getException().getMessage());
                    mBtnLogin.setVisibility(View.VISIBLE);
                    mLoadingProgress.setVisibility(View.INVISIBLE);
                }

            }
        });
    }

    private void showMessage(String text) {
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();
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
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            updateUI();
        }
    }
}
