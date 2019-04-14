package is.hi.hbv601g.brent.activities.user;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import is.hi.hbv601g.brent.activities.model.ToursActivity;
import is.hi.hbv601g.brent.models.Cart;
import is.hi.hbv601g.brent.R;
import is.hi.hbv601g.brent.activities.model.BikeActivity;
import is.hi.hbv601g.brent.activities.model.BikesActivity;
import is.hi.hbv601g.brent.activities.CancelBookingActivity;
import is.hi.hbv601g.brent.fragments.ItemListFragment;
import is.hi.hbv601g.brent.utils.ItemListListener;
import is.hi.hbv601g.brent.holders.ItemListViewHolder;
import is.hi.hbv601g.brent.models.Accessory;
import is.hi.hbv601g.brent.models.Bike;
import is.hi.hbv601g.brent.models.Booking;
import is.hi.hbv601g.brent.models.Tour;
import is.hi.hbv601g.brent.services.BookingService;
import is.hi.hbv601g.brent.utils.Pair;

public class BookingActivity extends ItemListListener {

    private Booking mBooking;
    private TextView mBookingTitle;
    private TextView mBookingStartDate;
    private TextView mBookingEndDate;
    private TextView mBookingDuration;
    private TextView mBookingPrice;
    private TextView mCancelBookingButton;
    private ItemListFragment mItemListFragment;
    private ArrayList<Bike> mBikes;
    private ArrayList<Accessory> mAccessories;
    private ArrayList<Tour> mTours;
    private Cart mCart;
    private boolean mDataFetched = false;
    private ArrayList<Pair> mPairs;
    private BookingService bookingService = new BookingService(this);

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
            setContentView(R.layout.activity_loading);
            super.setUp();
            Intent bookingsActivity_intent = getIntent();
            mBooking = bookingsActivity_intent.getParcelableExtra("booking");
            bookingService.fetchBooking(mBooking.getId());
        } else {
            setContentView(R.layout.activity_booking);
            super.setUp();
            mPairs = createPairs();
            mBookingTitle = findViewById(R.id.booking_title);
            mBookingStartDate = findViewById(R.id.booking_start_date);
            mBookingEndDate = findViewById(R.id.booking_end_date);
            mBookingDuration = findViewById(R.id.booking_duration);
            mBookingPrice = findViewById(R.id.booking_price);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
            mBookingTitle.setText("Booking Title");
            final Date startDate = mBooking.getStartDate();
            mBookingStartDate.setText(dateFormat.format(startDate));
            Date endDate = mBooking.getEndDate();
            mBookingEndDate.setText(dateFormat.format(endDate));
            mBookingDuration.setText(getDuration(startDate, endDate));
            mBookingPrice.setText("" + mBooking.getPrice());
            setList();
            mCart = Cart.getCart();
            mCancelBookingButton = findViewById(R.id.booking_cancel_booking);
            mCancelBookingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), CancelBookingActivity.class);
                    intent.putExtra("booking", mBooking);
                    startActivity(intent);
                }
            });

        }
    }

    public void setBikes(ArrayList<Bike> bikes) {
        this.mBikes = bikes;
    }

    public void setTours(ArrayList<Tour> tours) {
        this.mTours = tours;
    }

    public void setAccessories(ArrayList<Accessory> accessories) {
        this.mAccessories = accessories;
    }

    public void setIsDataFetched(boolean dataFetched) {
        this.mDataFetched = dataFetched;
    }

    private void setList() {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ItemListFragment.getArgumentKey(), mPairs);
        mItemListFragment = new ItemListFragment();
        mItemListFragment.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.bookingListContainer, mItemListFragment).commit();
    }

    private ArrayList<Pair> createPairs() {
        ArrayList<Pair> pairs = new ArrayList<>();
        Pair pair;
        for (Bike bike : mBikes){
            pair = new Pair("bike", bike);
            pairs.add(pair);
        }
        for (Tour tour : mTours){
            pair = new Pair("tour", tour);
            pairs.add(pair);
        }
        for (Accessory accessory : mAccessories){
            pair = new Pair("accessory", accessory);
            pairs.add(pair);
        }
        return pairs;
    }

    private String getDuration(Date startDate, Date endDate) {
        long milliseconds = endDate.getTime() - startDate.getTime();
        int days = (int)(milliseconds/1000)/(3600 * 24);
        return days + " days";
    }

    @Override
    public void onBikeSelected(Bike bike) {
        Intent intent = new Intent(getApplicationContext(), BikeActivity.class);
        intent.putExtra("bike", bike);
        intent.putExtra("startDate", mBooking.getStartDate());
        intent.putExtra("endDate", mBooking.getEndDate());
        startActivity(intent);
    }

    @Override
    public void onBindViewHolder(final ItemListViewHolder itemListViewHolder, int index) {
        Pair pair = mPairs.get(index);
        String key = pair.getKey();
        if (key == "bike") {
            Bike bike = (Bike) pair.getVal();
            BikesActivity.bindViewHolder(itemListViewHolder, bike);
        }
        else if (key == "tour") {
            final Tour tour = (Tour) pair.getVal();
            ToursActivity.bindViewHolder(itemListViewHolder, tour);
        }
        else if(key == "accessory") {
            Accessory accessory = (Accessory) pair.getVal();
            itemListViewHolder.mCardTitle.setText(accessory.getName());
            itemListViewHolder.mCardInfo3.setText("" + accessory.getPrice());
        }
    }
}
