package is.hi.hbv601g.brent.activities.model;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import is.hi.hbv601g.brent.activities.PaymentActivity;
import is.hi.hbv601g.brent.models.Cart;
import is.hi.hbv601g.brent.R;
import is.hi.hbv601g.brent.fragments.ItemListFragment;
import is.hi.hbv601g.brent.fragments.ItemListListener;
import is.hi.hbv601g.brent.holders.ViewHolder;
import is.hi.hbv601g.brent.models.Accessory;
import is.hi.hbv601g.brent.services.AccessoryService;

public class AccessoriesActivity extends ItemListListener {

    private Cart mCart;
    private ArrayList<Accessory> mAccessories;
    private ItemListFragment mItemListFragment;
    private boolean mDataFetched = false;
    private AccessoryService accessoryService = new AccessoryService(this);
    private static final String mTAG = "AccessoriesActivity";
    private static HashMap<String, Boolean> items = new HashMap<>();

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
            accessoryService.fetchAccessories();
        } else {
            setContentView(R.layout.activity_accessories);
            super.setUp();
            mAccessories = accessoryService.getAccessories();
            setAccessoriesList();
            setButtonOnClick();
        }
    }

    public void setIsDataFetched(boolean dataFetched) {
        this.mDataFetched = dataFetched;
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
    public void onAccessoryRemoved(Accessory accessory) {
        mCart.removeAccessoryToCart(accessory);
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
                Log.d(mTAG, accessory.getName());
                if (items.containsKey(accessory.getName())) {
                    items.remove(accessory.getName());
                    viewHolder.mLayout.setBackgroundColor(Color.WHITE);
                    viewHolder.mListener.onAccessoryRemoved(accessory);
                } else {
                    items.put(accessory.getName(), true);
                    viewHolder.mLayout.setBackgroundColor(Color.parseColor("#e1fde2"));
                    viewHolder.mListener.onAccessorySelected(accessory);
                }


                Log.d("Fragment", accessory.getType());
            }
        });
    }

}
