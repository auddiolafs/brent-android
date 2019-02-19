package is.hi.hbv601g.brent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CheckoutActivity extends CurrentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.connected) {
            setUp();
        }
    }

    @Override
    public void setUp() {
        setContentView(R.layout.activity_checkout);
    }
}
