package is.hi.hbv601g.brent.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import is.hi.hbv601g.brent.R;

public class UserActivity extends CurrentActivity {

    Button mLogoutButton;

    TextView mDisplayname;
    TextView mEmailText;
    TextView mBookingText;
    View booking;

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
        mEmailText = findViewById(R.id.user_email_text);

        booking = findViewById(R.id.user_bookings);
        TextView bookingText = booking.findViewById(R.id.card_text_id);
        bookingText.setText("Bookings");

        ImageView bookingImage = booking.findViewById(R.id.card_image_id);
        bookingImage.setImageResource(R.drawable.icon_list);

        mDisplayname.setText(mAuth.getCurrentUser().getDisplayName());
        mEmailText.setText(mAuth.getCurrentUser().getEmail());

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
