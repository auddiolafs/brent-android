package is.hi.hbv601g.brent.activities.model;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import is.hi.hbv601g.brent.R;
import is.hi.hbv601g.brent.fragments.ItemListFragment;
import is.hi.hbv601g.brent.fragments.ItemListListener;
import is.hi.hbv601g.brent.holders.ViewHolder;
import is.hi.hbv601g.brent.models.Tour;

public class ToursActivity extends ItemListListener {

    private static final String KEY_TOURS = "Tours";
    private ArrayList<Tour> mTours = new ArrayList<>();
    private ItemListFragment mItemListFragment;
    private static final FirebaseFirestore mDB = FirebaseFirestore.getInstance();
    private static final String mTAG = "ToursActivity";
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
            fetchTours();
        } else {
            setContentView(R.layout.activity_tours);
            super.setUp();
            setTourList();
        }
    }

    /**
     * Fetches all tours from Firestore db, to be displayed in the tours list.
     */
    private void fetchTours() {
        final ArrayList<Tour> tours = new ArrayList<>();
        final Task<QuerySnapshot> task = mDB.collection("tours").get();

        task.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    tours.add(Tour.toEntity(document.getId(), document.getData()));
                }

                mTours = tours;
                mDataFetched = true;
                setUp();
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            public void onFailure(Exception e) {
                setUp();
                Log.d(mTAG, "error");
            }
        });
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
        // intent.putExtra("length", route.getLength());
        startActivity(intent);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int index) {
        bindViewHolder(viewHolder, mTours.get(index));
    }

    private void bindViewHolder(final ViewHolder viewHolder, final Tour tour) {
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
