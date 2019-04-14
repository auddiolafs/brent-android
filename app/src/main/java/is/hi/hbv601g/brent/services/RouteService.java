package is.hi.hbv601g.brent.services;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import is.hi.hbv601g.brent.activities.model.RoutesActivity;
import is.hi.hbv601g.brent.models.Route;

public class RouteService {

    private RoutesActivity routesActivity;
    private FirebaseFirestore mDB = FirebaseFirestore.getInstance();
    private static final String mTAG = "RouteService";
    private static ArrayList<Route> mRoutes;

    public RouteService(RoutesActivity routesActivity) {
        this.routesActivity = routesActivity;
    }

    public ArrayList<Route> getRoutes() {
        return mRoutes;
    }

    /**
     * Fetches all routes from Firestore db, to be displayed in the routes list.
     */
    public void fetchRoutes() {
        final ArrayList<Route> routes = new ArrayList<>();
        final Task<QuerySnapshot> task = mDB.collection("routes")
                .get();

        task.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Route route = Route.toEntity(document.getId(), document.getData());
                    if (route == null) {
                        Log.d(mTAG, "error");
                    } else {
                        routes.add(route);
                    }
                }
                
                mRoutes = routes;
                routesActivity.setIsDataFetched(true);
                routesActivity.setUp();
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            public void onFailure(Exception e) {
                Log.d(mTAG, "Error fetching routes");
            }
        });
    }
}
