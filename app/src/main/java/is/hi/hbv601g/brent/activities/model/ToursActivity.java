package is.hi.hbv601g.brent.activities.model;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import is.hi.hbv601g.brent.R;
import is.hi.hbv601g.brent.fragments.ItemListFragment;
import is.hi.hbv601g.brent.fragments.ItemListListener;
import is.hi.hbv601g.brent.holders.ViewHolder;
import is.hi.hbv601g.brent.models.Tour;
import is.hi.hbv601g.brent.services.TourService;

public class ToursActivity extends ItemListListener {

    private static final String KEY_TOURS = "Tours";
    private ArrayList<Tour> mTours = new ArrayList<>();
    private ItemListFragment mItemListFragment;
    private TourService tourService = new TourService(this);
    private boolean mDataFetched = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mTours = savedInstanceState.getParcelableArrayList(KEY_TOURS);
            mDataFetched = true;
        }
        if (this.connected) {
            setUp();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList(KEY_TOURS, mTours);
    }

    /**
     * Fetches the data if it hasn't already been fetched.
     */
    @Override
    public void setUp() {
        if (!mDataFetched) {
            setContentView(R.layout.activity_loading);
            super.setUp();
            tourService.fetchTours();
        } else {
            setContentView(R.layout.activity_tours);
            super.setUp();
            mTours = tourService.getTours();
            setTourList();
        }
    }

    public void setIsDataFetched(boolean dataFetched) {
        this.mDataFetched = dataFetched;
    }

    /**
     * Creates the fragment for the list of tours.
     */
    private void setTourList() {
        FragmentManager fm = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ItemListFragment.getArgumentKey(), mTours);
        mItemListFragment = new ItemListFragment();
        mItemListFragment.setArguments(bundle);
        mItemListFragment.doubleInLandscapeMode(true);
        fm.beginTransaction().replace(R.id.toursListContainer, mItemListFragment).commit();
    }

    @Override
    public void onTourSelected(Tour tour) {
        Intent intent = new Intent(getApplicationContext(),
                TourActivity.class);
        intent.putExtra("tour", tour);
        intent.putExtra("location", tour.getLocation());
        startActivity(intent);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int index) {
        bindViewHolder(viewHolder, mTours.get(index));
    }

    public static void bindViewHolder(final ViewHolder viewHolder, final Tour tour) {
        viewHolder.mCardTitle.setText(tour.getName());
        viewHolder.mCardInfo1.setText(tour.getLocation());
        viewHolder.mCardInfo2.setText(tour.getDuration().toString() + " hours");
        viewHolder.mCardInfo3.setText(tour.getPrice() + " ISK");
        Picasso.get().load(tour.getImage())
                .placeholder(R.drawable.menu_tour)
                .centerInside()
                .resize(200, 200)
                .into(viewHolder.mCardImage);
        viewHolder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.mListener.onTourSelected(tour);
            }
        });
    }
}
