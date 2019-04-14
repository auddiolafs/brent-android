package is.hi.hbv601g.brent.services;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import is.hi.hbv601g.brent.activities.model.BikesActivity;
import is.hi.hbv601g.brent.models.Bike;

public class BikeService {

    private BikesActivity bikesActivity;
    private FirebaseFirestore mDB = FirebaseFirestore.getInstance();
    private static final String mTAG = "BikeService";
    private static ArrayList<Bike> mBikes;
    private ArrayList<String> mTypes;

    public BikeService(BikesActivity bikesActivity) {
        this.bikesActivity = bikesActivity;
    }

    public ArrayList<Bike> getBikes() {
        return mBikes;
    }

    public ArrayList<String> getTypes() {
        return mTypes;
    }

    /**
     * Fetches types from Firestore and calls the function to fetch bikes.
     */
    public void fetchData() {
        final ArrayList<String> types = new ArrayList<>();
        final Task<QuerySnapshot> task = mDB.collection("types").get();

        // Fetch types first and then bikes, both are asynchronous calls so both need to finish
        // before setting the content view
        task.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot document : task.getResult()) {
                    types.add(document.getData().get("type").toString());
                }
                mTypes = types;
                fetchBikes();
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            public void onFailure(Exception e) {
                Log.d(mTAG, "Error fetching types");
            }
        });
    }

    /**
     * Fetches all bikes from Firestore db, to be displayed in the bikes list.
     */
    public void fetchBikes() {
        final ArrayList<Bike> bikes = new ArrayList<>();
        final Task<QuerySnapshot> task = mDB.collection("bikes")
                .get();

        task.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Bike bike = Bike.toEntity(document.getId(), document.getData());
                    if (bike == null) {
                        Log.d(mTAG, "error");
                    } else {
                        bikes.add(bike);
                    }
                }
                mBikes = bikes;
                bikesActivity.setDisplayedBikes((ArrayList<Bike>) bikes.clone());
                bikesActivity.setIsDataFetched(true);
                bikesActivity.setUp();
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            public void onFailure(Exception e) {
                Log.d(mTAG, "Error fetching bikes");
            }
        });
    }
}