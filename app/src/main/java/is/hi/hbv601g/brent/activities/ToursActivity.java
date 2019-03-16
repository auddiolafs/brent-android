package is.hi.hbv601g.brent.activities;

import android.content.Intent;
import android.os.Bundle;
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
import java.util.Map;

import is.hi.hbv601g.brent.R;
import is.hi.hbv601g.brent.models.Tour;

public class ToursActivity extends CurrentActivity {

    private List<Tour> mTours = new ArrayList<>();
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "ToursActivity";
    private boolean mDataFetched = false;

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
            fetchToursFirestore();
        } else {
            setContentView(R.layout.activity_tours);
            super.setUp();
        }
    }

    private void fetchToursFirestore() {
        final List<Tour> tours = new ArrayList<>();
        final Task<QuerySnapshot> task = db.collection("tours")
                .get();

        task.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                mDataFetched = true;
                setUp();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    tours.add(toEntity(document.getId(), document.getData()));
                    Log.d(TAG, document.getId() + " => " + document.getData());
                }

                mTours = tours;
                setTours();
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            public void onFailure(Exception e) {
                mDataFetched = true;
                setUp();
                Log.d(TAG, "error");
            }
        });
    }

    private static Tour toEntity(String tourId, Map<String, Object> tourData) {
        Tour t = new Tour();
        try {
            t.setId(tourId);
            t.setName(tourData.get("name").toString());
            t.setLocation(tourData.get("location").toString());
            t.setPrice(Long.parseLong(tourData.get("price").toString()));
            // TODO: set dates
            return t;
        } catch (Exception e) {
            Log.d(TAG, "error");
            return null;
        }
    }

    private void setTours() {
        // Log.d(TAG, mTours.get(0).getName());
        // TODO: create cards
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
