package is.hi.hbv601g.brent.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import is.hi.hbv601g.brent.Cart;
import is.hi.hbv601g.brent.R;
import is.hi.hbv601g.brent.models.Accessory;

public class AccessoriesActivity extends CurrentActivity {

    private Cart mCart;
    private List<Accessory> mAccessories = new ArrayList<>();
    private boolean mDataFetched = false;
    private static final FirebaseFirestore mDB = FirebaseFirestore.getInstance();
    private static final String mTAG = "AccessoriesActivity";

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
        super.setUp();

        if (!mDataFetched) {
            setContentView(R.layout.activity_loading);
            super.setUp();
            fetchAccessories();
        } else {
            setContentView(R.layout.activity_accessories);
            super.setUp();
            setButtonOnClick();
        }
    }

    /**
     * Fetches all accessories from Firestore db, to be displayed in the accessories list.
     */
    private void fetchAccessories() {
        final List<Accessory> accessories = new ArrayList<>();
        final Task<QuerySnapshot> task = mDB.collection("acessories").get();

        task.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                mDataFetched = true;
                setUp();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    accessories.add(Accessory.toEntity(document.getId(), document.getData()));
                }

                mAccessories = accessories;
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            public void onFailure(Exception e) {
                setUp();
                Log.d(mTAG, "error");
            }
        });
    }

    private void setButtonOnClick() {
        Button continueButton = findViewById(R.id.continueToPaymentButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent accessoriesActivity = new Intent(getApplicationContext(), PaymentActivity.class);
                startActivity(accessoriesActivity);
                finish();
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
