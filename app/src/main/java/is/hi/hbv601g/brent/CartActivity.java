package is.hi.hbv601g.brent;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class CartActivity extends CurrentActivity {
    private Cart mCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.connected) {
            setUp();
        }
    }

    @Override
    public void setUp() {
        mCart = Cart.getCart();
        setContentView(R.layout.activity_cart);
        Button saveCartButton = findViewById(R.id.saveCartButton);
        saveCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCart.saveCart();
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
