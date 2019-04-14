package is.hi.hbv601g.brent.services;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import is.hi.hbv601g.brent.activities.authentication.LoginActivity;
import is.hi.hbv601g.brent.activities.authentication.RegisterActivity;

public class AuthenticationService {

    private LoginActivity loginActivity;
    private RegisterActivity registerActivity;
    private Map<String, Object> mUser = new HashMap<>();
    private FirebaseFirestore mDB = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    private static final String mTAG = "AuthenticationService";

    public AuthenticationService(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
    }

    public AuthenticationService(RegisterActivity registerActivity) {
        this.registerActivity = registerActivity;
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    /**
     * Signs a user in with Firebase.
     * @param mail
     * @param password
     */
    public void signIn(String mail, String password) {
        mAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    loginActivity.onLoginSuccess();
                } else {
                    loginActivity.onLoginError(task.getException().getMessage());
                }
            }
        });
    }

    /**
     * Registers a user - saves the email, name and password in the Firebase db.
     * @param email
     * @param name
     * @param password
     */
    public void CreateUserAccount(String email, final String name, String password) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(registerActivity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    registerActivity.onRegisterSuccess(name, mAuth.getCurrentUser());
                } else {
                    registerActivity.onRegisterError(task.getException().getMessage());
                }
            }
        });
    }

    /**
     * Updates the name of the currently logged in user
     * @param name
     * @param currentUser
     */
    public void updateUserInfo(String name, FirebaseUser currentUser) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        currentUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(mTAG, "SUCCESS");
                            registerActivity.finish();
                        }
                    }
                });

        Log.e(mTAG, "User registration successful");
        mUser.put("email", currentUser.getEmail());
        mUser.put("displayName", name);
        mDB.collection("users").document(currentUser.getUid()).set(mUser);
    }
}
