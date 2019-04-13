package is.hi.hbv601g.brent.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import is.hi.hbv601g.brent.models.Cart;
import is.hi.hbv601g.brent.R;
import is.hi.hbv601g.brent.fragments.ItemListFragment;
import is.hi.hbv601g.brent.fragments.ItemListListener;
import is.hi.hbv601g.brent.holders.ViewHolder;
import is.hi.hbv601g.brent.models.Accessory;

public class AccessoriesActivity extends ItemListListener {

    private Cart mCart;
    private ArrayList<Accessory> mAccessories;
    private ItemListFragment mItemListFragment;
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
            setAccessoriesList();
            setButtonOnClick();
        }
    }

    /**
     * Fetches all accessories from Firestore db, to be displayed in the accessories list.
     */
    private void fetchAccessories() {
        final ArrayList<Accessory> accessories = new ArrayList<>();
        final Task<QuerySnapshot> task = mDB.collection("accessories").get();

        task.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                setUp();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    accessories.add(Accessory.toEntity(document.getId(), document.getData()));
                }

                mAccessories = accessories;
                mDataFetched = true;
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
        TextView continueButton = findViewById(R.id.buttonContinue);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent accessoriesActivity = new Intent(getApplicationContext(), PaymentActivity.class);
                startActivity(accessoriesActivity);
                finish();
            }
        });
    }

    private void setAccessoriesList() {
        FragmentManager fm = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ItemListFragment.getArgumentKey(), mAccessories);
        mItemListFragment = new ItemListFragment();
        mItemListFragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.accessoriesListContainer, mItemListFragment).commit();
    }

    @Override
    public void onAccessorySelected(Accessory accessory) {
        mCart.addAccessoryToCart(accessory);
        Log.d("Access", accessory.toString());
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int index) {
        bindViewHolder(viewHolder, mAccessories.get(index));
    }

    public static void bindViewHolder(final ViewHolder viewHolder, final Accessory accessory) {
        viewHolder.mCardTitle.setText(accessory.getName());
        Picasso.get().load(accessory.getImage())
                .placeholder(R.drawable.menu_tour)
                .centerInside()
                .resize(200, 200)
                .into(viewHolder.mCardImage);
        if (accessory.getType().equals("lock")) {
            viewHolder.mCardImage.setImageResource(R.drawable.accessories_lock);
        } else if (accessory.getType().equals("helmet")) {
            viewHolder.mCardImage.setImageResource(R.drawable.accessories_helmet);
        } else if (accessory.getType().equals("kit")) {
            viewHolder.mCardImage.setImageResource(R.drawable.accessories_repair_kit);
        } else if (accessory.getType().equals("lights")) {
            viewHolder.mCardImage.setImageResource(R.drawable.accessories_lights);
        }
        viewHolder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.mListener.onAccessorySelected(accessory);
                viewHolder.mLayout.setBackgroundColor(Color.GREEN);
                Log.d("Fragment", accessory.getType());
            }
        });
    }

}
