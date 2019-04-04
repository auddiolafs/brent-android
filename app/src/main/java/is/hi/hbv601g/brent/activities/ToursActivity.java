package is.hi.hbv601g.brent.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import is.hi.hbv601g.brent.R;
import is.hi.hbv601g.brent.fragments.RoutesFragment;
import is.hi.hbv601g.brent.fragments.ToursFragment;
import is.hi.hbv601g.brent.models.Tour;

public class ToursActivity extends CurrentActivity {

    private static final String KEY_TOURS = "Tours";
    private ArrayList<Tour> mTours = new ArrayList<>();
    private ToursFragment mTourFragment;
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
        bundle.putParcelableArrayList("tours", mTours);
        mTourFragment = new ToursFragment();
        mTourFragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.toursListContainer, mTourFragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        System.out.println(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                return true;
            default:
                System.out.println(item.getItemId());

                return super.onOptionsItemSelected(item);
        }
    }
}
