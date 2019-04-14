package is.hi.hbv601g.brent.services;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import is.hi.hbv601g.brent.activities.model.ToursActivity;
import is.hi.hbv601g.brent.models.Tour;

public class TourService {

    private ToursActivity toursActivity;
    private FirebaseFirestore mDB = FirebaseFirestore.getInstance();
    private static final String mTAG = "TourService";
    private static ArrayList<Tour> mTours;

    public TourService(ToursActivity toursActivity) {
        this.toursActivity = toursActivity;
    }

    public ArrayList<Tour> getTours() {
        return mTours;
    }

    /**
     * Fetches all tours from Firestore db, to be displayed in the tours list.
     */
    public void fetchTours() {
        final ArrayList<Tour> tours = new ArrayList<>();
        final Task<QuerySnapshot> task = mDB.collection("tours").get();

        task.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    tours.add(Tour.toEntity(document.getId(), document.getData()));
                }

                mTours = tours;
                toursActivity.setIsDataFetched(true);
                toursActivity.setUp();
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            public void onFailure(Exception e) {
                Log.d(mTAG, "error");
            }
        });
    }
}
