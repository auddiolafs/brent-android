package is.hi.hbv601g.brent;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public abstract class CurrentActivity extends AppCompatActivity {

    private static DialogFragment dialogFragment;
    public boolean connected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        this.dialogFragment = new RequireInternet();
        connected = isConnected();
    }

    // Checks whether device is connected to the internet
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

//    // If any action happens in menu it will call this method
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        Intent intent;
//        // Switch on id on each menu item
//        switch (item.getItemId()) {
//            case R.id.cart:
//                intent = new Intent(this, CartActivity.class);
//                startActivity(intent);
//                return true;
//            case R.id.home:
//                intent = new Intent(this, MainActivity.class);
//                startActivity(intent);
//                return true;
//            case R.id.profile:
//                intent = new Intent(this, UserActivity.class);
//                startActivity(intent);
//                return true;
//
//            default:
//                // If we got here, the user's action was not recognized.
//                // Invoke the superclass to handle it.
//                return super.onOptionsItemSelected(item);
//
//        }
//    }

    // Overide method in AppCompatActivity, which allows us
    //          set menu defined in res/menu/main_menu.xml into actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public abstract void setUp();

}
