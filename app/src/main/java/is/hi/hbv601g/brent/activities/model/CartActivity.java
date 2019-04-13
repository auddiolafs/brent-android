package is.hi.hbv601g.brent.activities.model;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import java.util.List;

import is.hi.hbv601g.brent.activities.CurrentActivity;
import is.hi.hbv601g.brent.activities.MainActivity;
import is.hi.hbv601g.brent.models.Cart;
import is.hi.hbv601g.brent.R;
import is.hi.hbv601g.brent.fragments.CartListFragment;
import is.hi.hbv601g.brent.models.Bike;
import is.hi.hbv601g.brent.models.Tour;

public class CartActivity extends CurrentActivity {

    private Cart mCart;
    private Bike mBikes;
    private List aList = new ArrayList();
    private CartListFragment mCartListFragment;

    public static Intent newIntent(BikeActivity bikeActivity) {
        return new Intent(bikeActivity, CartActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.connected) {
            setUp();
        }
    }

    public class Triplet implements Parcelable {
        String mProductName;
        int mQuantity;
        int mPrice;

        public Triplet(String productName, int quantity, int price) {
            mProductName = productName;
            mQuantity = quantity;
            mPrice = price;
        }

        protected Triplet(Parcel in) {
            mProductName = in.readString();
            mQuantity = in.readInt();
            mPrice = in.readInt();
        }

        public String getPrice() {
            return "" + mPrice;
        }

        public void setPrice(int price) {
            mPrice = price;
        }

        public final Creator<Triplet> CREATOR = new Creator<Triplet>() {
            @Override
            public Triplet createFromParcel(Parcel in) {
                return new Triplet(in);
            }

            @Override
            public Triplet[] newArray(int size) {
                return new Triplet[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(mProductName);
            dest.writeInt(mQuantity);
            dest.writeInt(mPrice);
        }

        public String getProduct() {
            return mProductName;
        }

        public String getQuantity() {
            return "" + mQuantity;
        }
    }


    @Override
    public void setUp() {
        mCart = Cart.getCart();

        List<Bike> bikes = mCart.getBikes();
        List<Tour> tours = mCart.getTours();

        Map<String, Integer> quantity = new HashMap<>();
        Map<String, Long> priceList = new HashMap<>();
        Map<String, String> productName = new HashMap<>();

        ArrayList<Triplet> listOfTriplets = new ArrayList<>();

        for (int i = 0; i < bikes.size(); i++) {
            String bikeID = bikes.get(i).getId();
            productName.put(bikeID, bikes.get(i).getName());
            Integer count = quantity.get(bikeID);
            if (count == null) {
                quantity.put(bikeID, 1);
                priceList.put(bikeID, bikes.get(i).getPrice());
            } else {
                count += 1;
                quantity.put(bikeID, count);
                priceList.put(bikeID, count * bikes.get(i).getPrice());
            }
        }

        for (int i = 0; i < tours.size(); i++) {
            String tourID = tours.get(i).getId();
            productName.put(tourID, tours.get(i).getName());
            Integer count = quantity.get(tourID);
            if (count == null) {
                Integer numPax = tours.get(i).getNumberOfTravelers().intValue();
                quantity.put(tourID, numPax);
                priceList.put(tourID, tours.get(i).getPrice() * numPax);
            } else {
                count += tours.get(i).getNumberOfTravelers().intValue();
                quantity.put(tourID, count);
                priceList.put(tourID, count * tours.get(i).getPrice());
            }
        }

        for (Map.Entry<String, Long> entry : priceList.entrySet()) {
            String bikeID = entry.getKey();
            Log.d("Cart", bikeID + " / " + entry.getValue());
            listOfTriplets.add(new Triplet(productName.get(bikeID), quantity.get(bikeID).intValue(), priceList.get(bikeID).intValue()));
        }


        setContentView(R.layout.activity_cart);
        super.setUp();

        setList(listOfTriplets);

        TextView mTotalPrice = findViewById(R.id.totalPriceValueText);
        mTotalPrice.setText(mCart.getTotalPrice().toString() + " kr.");

        TextView continueButton = findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent accessoriesActivity = new Intent(getApplicationContext(), AccessoriesActivity.class);
                startActivity(accessoriesActivity);
                // saveCartButton.setClickable(false);
                finish();
            }
        });
    }

    private void setList(ArrayList<Triplet> triplets) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.bikeListContainer);
        if (fragment == null) {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("data", triplets);
            mCartListFragment = new CartListFragment();
            mCartListFragment.setArguments(bundle);
            fm.beginTransaction().add(R.id.cart_list_container, mCartListFragment).commit();
        } else {
            mCartListFragment = (CartListFragment) fragment;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, item.getTitle(), Toast.LENGTH_LONG).show();
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateUI() {
        Intent homeActivity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(homeActivity);
        finish();
    }
}
