package is.hi.hbv601g.brent.activities.user;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import is.hi.hbv601g.brent.R;
import is.hi.hbv601g.brent.fragments.ItemListFragment;
import is.hi.hbv601g.brent.utils.ItemListListener;
import is.hi.hbv601g.brent.holders.ItemListViewHolder;
import is.hi.hbv601g.brent.models.Booking;
import is.hi.hbv601g.brent.services.BookingService;

public class BookingsActivity extends ItemListListener {


    private ArrayList<Booking> mBookings = new ArrayList<>();
    private ItemListFragment mItemListFragment;
    private BookingService bookingService = new BookingService(this);
    private boolean mDataFetched = false;

    private FirebaseApp mApp;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApp = FirebaseApp.getInstance();
        mAuth = FirebaseAuth.getInstance(mApp);
        mUser = mAuth.getCurrentUser();

        if (this.connected) {
            setUp();
        }
    }

    /**
     * Fetches the data if it hasn't already been fetched.
     */
    @Override
    public void setUp() {
        if (!mDataFetched) {
            setContentView(R.layout.activity_loading);
            super.setUp();
            bookingService.fetchBookings(mUser);
        } else {
            setContentView(R.layout.activity_bookings);
            super.setUp();
            mBookings = bookingService.getBookings();
            setBookingList();
        }
    }

    public void setIsDataFetched(boolean dataFetched) {
        this.mDataFetched = dataFetched;
    }

    /**
     * Creates the fragment for the list of bookings.
     */
    private void setBookingList() {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ItemListFragment.getArgumentKey(), mBookings);
        mItemListFragment = new ItemListFragment();
        mItemListFragment.setArguments(bundle);
        mItemListFragment.doubleInLandscapeMode(true);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.bookingsListContainer, mItemListFragment).commit();
    }


    /**
     * Start the BookingAcvity to see details for the selected booking.
     * @param booking
     */
    @Override
    public void onBookingSelected(Booking booking) {
        Intent intent = new Intent(getApplicationContext(),
                BookingActivity.class);
        intent.putExtra("booking", booking);
        startActivity(intent);
    }

    @Override
    public void onBindViewHolder(ItemListViewHolder itemListViewHolder, int index) {
        bindViewHolder(itemListViewHolder, mBookings.get(index), index);
    }

    public static void bindViewHolder(final ItemListViewHolder itemListViewHolder, final Booking booking, int index) {
        itemListViewHolder.mCardTitle.setText("Booking title");
        itemListViewHolder.mCardInfo3.setText(booking.getPrice() + " kr");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        String startDateString = dateFormat.format(booking.getStartDate());
        String endDateString = dateFormat.format(booking.getEndDate());
        itemListViewHolder.mCardInfo2.setText(startDateString + " to " + endDateString);
        switch (index % 5) {
            case 0:
                itemListViewHolder.mCardImage.setImageResource(R.drawable.booking_img1);
                break;
            case 1:
                itemListViewHolder.mCardImage.setImageResource(R.drawable.booking_img2);
                break;
            case 2:
                itemListViewHolder.mCardImage.setImageResource(R.drawable.booking_img3);
                break;
            case 3:
                itemListViewHolder.mCardImage.setImageResource(R.drawable.booking_img4);
                break;
            default:
                itemListViewHolder.mCardImage.setImageResource(R.drawable.booking_img5);
        }
        itemListViewHolder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemListViewHolder.mListener.onBookingSelected(booking);
            }
        });
    }

}
