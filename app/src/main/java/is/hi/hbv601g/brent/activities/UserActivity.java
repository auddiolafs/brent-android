package is.hi.hbv601g.brent.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import is.hi.hbv601g.brent.R;

public class UserActivity extends CurrentActivity {

    Button mLogoutButton;

    TextView mDisplayname;

    FirebaseAuth mAuth;
    FirebaseApp mApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.connected) {
            setUp();
        }
    }

    @Override
    public void setUp() {
        setContentView(R.layout.activity_user);
        super.setUp();

        mApp = FirebaseApp.getInstance();
        mAuth = FirebaseAuth.getInstance(mApp);
        // Get toolbar in layout (defined in xml file

        Log.d("UserAc", mAuth.getCurrentUser().getDisplayName());

        mLogoutButton = findViewById(R.id.logoutButton);
        mDisplayname = findViewById(R.id.user_name_text);
        mDisplayname.setText(mAuth.getCurrentUser().getDisplayName());

        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, item.getTitle(), Toast.LENGTH_LONG).show();
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
}
