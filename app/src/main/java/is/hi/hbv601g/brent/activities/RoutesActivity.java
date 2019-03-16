package is.hi.hbv601g.brent.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;


import is.hi.hbv601g.brent.R;

public class  RoutesActivity extends CurrentActivity {

    private boolean mDataFetched = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.connected) {
            setUp();
        }
    }

    @Override
    public void setUp() {
        if (!mDataFetched) {
            fetchData();
            setContentView(R.layout.activity_loading);
            super.setUp();
        } else {
            setContentView(R.layout.activity_routes);
            super.setUp();
        }
    }

    private void fetchData() {
        // TODO: fetch data
        mDataFetched = true;
        setUp();
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
