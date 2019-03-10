package is.hi.hbv601g.brent.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import is.hi.hbv601g.brent.MainActivity;
import is.hi.hbv601g.brent.R;

public class RegisterActivity extends AppCompatActivity {

    String TAG = "Register >>";

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ImageView headerImage;
    static int PreqCode = 1;
    static int REQUESCODE = 1;

    private EditText userEmail;
    private EditText userPassword;
    private EditText userPasswordConfirm;
    private EditText userName;

    private TextView mRegisterText;

    private ProgressBar loadingProgress;

    private Button regBtn;

    Map<String, Object> user = new HashMap<>();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userEmail = findViewById(R.id.emailEdit);
        userPassword = findViewById(R.id.passwordEdit);
        userPasswordConfirm = findViewById(R.id.confirmPasswordEdit);
        userName = findViewById(R.id.displayName);
        loadingProgress = findViewById(R.id.login_progress);
        headerImage = findViewById(R.id.imageHeader);
        mRegisterText = findViewById(R.id.registerText);
        regBtn = findViewById(R.id.loginButton);
        loadingProgress.setVisibility(View.INVISIBLE);


        mAuth = FirebaseAuth.getInstance();

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regBtn.setVisibility(View.INVISIBLE);
                loadingProgress.setVisibility(View.VISIBLE);
                final String email = userEmail.getText().toString();
                final String password = userPassword.getText().toString();
                final String password2 = userPasswordConfirm.getText().toString();
                final String name = userName.getText().toString();

                if (email.isEmpty() || name.isEmpty() || password.isEmpty() || password2.isEmpty()
                || !password.equals(password2)) {
                    showMessage("Checl fields");
                    regBtn.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);
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

    private void CreateUserAccount(String email, final String name, String password) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    showMessage("Success");
                    updateUserInfo(name, mAuth.getCurrentUser());
                } else {
                    showMessage("error " + task.getException().getMessage());
                    regBtn.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    private void updateUserInfo(String name, FirebaseUser currentUser) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        currentUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "SUCCESS");
                            updateUI();
                        }
                    }
                });

        Log.e(TAG, "User registration successful");
        user.put("email", currentUser.getEmail());
        user.put("displayName", name);
        db.collection("users").document(currentUser.getUid())
                .set(user);
    }

    private void updateUI() {

        Intent homeActivity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(homeActivity);
        finish();
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
