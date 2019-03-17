package is.hi.hbv601g.brent.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;

import is.hi.hbv601g.brent.R;
import is.hi.hbv601g.brent.utils.RequireInternet;

public abstract class CurrentActivity extends AppCompatActivity {

    private static DialogFragment mDialogFragment;
    public boolean connected;

    private ImageButton mToolbarProfile;
    private ImageButton mToolbarHome;
    private ImageButton mToolbarCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        this.mDialogFragment = new RequireInternet();
        connected = isConnected();
    }

    public void setUp() {
        setToolbar();
    }

    /**
     * Checks whether device is connected to the internet
     */
    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        RequireInternet dialogFragment = new RequireInternet();
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null || !ni.isConnected()) {
            dialogFragment.show(this.getSupportFragmentManager(), "internet-required");
            return false;
        }
        return true;
    }

    /**
     * Sets the toolbar for the current activity with a button for profile, home and cart.
     */
    public void setToolbar() {
        mToolbarProfile = findViewById(R.id.toolbar_profile);
        mToolbarProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userIntent = new Intent(getApplicationContext(), UserActivity.class);
                startActivity(userIntent);
            }
        });
        mToolbarHome = findViewById(R.id.toolbar_home);
        mToolbarHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(home);
            }
        });
        mToolbarCart = findViewById(R.id.toolbar_cart);
        mToolbarCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cart = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(cart);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

}
