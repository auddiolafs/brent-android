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
import java.util.Date;
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
import is.hi.hbv601g.brent.utils.Triplet;

public class CartActivity extends CurrentActivity {

    private Cart mCart;
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


    @Override
    public void setUp() {
        setContentView(R.layout.activity_cart);
        super.setUp();
        mCart = Cart.getCart();

        List<Bike> bikes = mCart.getBikes();
        List<Tour> tours = mCart.getTours();
        Map<String, Integer> quantityList = new HashMap<>();
        Map<String, Integer> priceList = new HashMap<>();
        Map<String, String> productList = new HashMap<>();

        ArrayList<Triplet> listOfTriplets = new ArrayList<>();

        if (bikes.size() != 0) {
            addBikes(bikes, quantityList, priceList, productList);
        }

        if (tours.size() != 0) {
            addTours(tours, quantityList, priceList, productList);
        }

        int totalPrice = 0;
        for (Map.Entry<String, Integer> entry : priceList.entrySet()) {
            String itemID = entry.getKey();
            Log.d("Cart", itemID + " / " + entry.getValue());
            int price = priceList.get(itemID);
            totalPrice += price;
            String productName = productList.get(itemID);
            int quantity = quantityList.get(itemID).intValue();
            listOfTriplets.add(new Triplet(productName, quantity, price));
        }
        mCart.setTotalPrice(totalPrice);
        setList(listOfTriplets);
        TextView mTotalPrice = findViewById(R.id.totalPriceValueText);
        mTotalPrice.setText(totalPrice + " kr.");
        TextView continueButton = findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent accessoriesActivity = new Intent(getApplicationContext(), AccessoriesActivity.class);
                if (!mCart.isEmpty()) {
                    startActivity(accessoriesActivity);
                    finish();
                } else {
                    showMessage("Current cart is empty");
                }
            }
        });
    }

    private void addTours(List<Tour> tours, Map<String, Integer> quantityList, Map<String, Integer> priceList, Map<String, String> productList) {
        for (int i = 0; i < tours.size(); i++) {
            String tourID = tours.get(i).getId();
            productList.put(tourID, tours.get(i).getName());
            Integer count = quantityList.get(tourID);
            if (count == null) {
                Integer numPax = tours.get(i).getNumberOfTravelers().intValue();
                quantityList.put(tourID, numPax);
                priceList.put(tourID, new Integer(tours.get(i).getPrice() * numPax));
            } else {
                count += tours.get(i).getNumberOfTravelers().intValue();
                quantityList.put(tourID, count);
                priceList.put(tourID, new Integer(count * tours.get(i).getPrice()));
            }
        }
    }

    private void addBikes(List<Bike> bikes, Map<String, Integer> quantityList, Map<String, Integer> priceList, Map<String, String> productList) {
        Date startDate = mCart.getStartDate();
        Date endDate = mCart.getEndDate();
        long durationmilliSeconds = endDate.getTime() - startDate.getTime();
        int durationDays = (int)((durationmilliSeconds / 1000) / (3600*24));
        for (int i = 0; i < bikes.size(); i++) {
            String bikeID = bikes.get(i).getId();
            productList.put(bikeID, bikes.get(i).getName());
            Integer count = quantityList.get(bikeID);
            if (count == null) {
                quantityList.put(bikeID, 1);
                priceList.put(bikeID, new Integer(bikes.get(i).getPrice()*durationDays));
            } else {
                count += 1;
                quantityList.put(bikeID, count);
                priceList.put(bikeID, new Integer(count * bikes.get(i).getPrice()));
            }
        }
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

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
