package is.hi.hbv601g.brent.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import is.hi.hbv601g.brent.R;
import is.hi.hbv601g.brent.fragments.RoutesFragment;
import is.hi.hbv601g.brent.models.Route;

public class RoutesActivity extends CurrentActivity implements RoutesFragment.SelectionListener {

    private static final String KEY_ROUTES = "Routes";
    private ArrayList<Route> mRoutes = new ArrayList<>();
    private RoutesFragment mRouteFragment;
    private static final String mTAG = "RoutesActivity";
    private FirebaseFirestore mDB = FirebaseFirestore.getInstance();
    private boolean mDataFetched = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mRoutes = savedInstanceState.getParcelableArrayList(KEY_ROUTES);
            mDataFetched = true;
        }
        if (this.connected) {
            setUp();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList(KEY_ROUTES, mRoutes);
    }

    /**
     * Fetches the data if it hasn't already been fetched.
     */
    @Override
    public void setUp() {
        if (!mDataFetched) {
            setContentView(R.layout.activity_loading);
            super.setUp();
            fetchRoutes();
        } else {
            setContentView(R.layout.activity_routes);
            super.setUp();
            setRouteList();
        }
    }

    /**
     * Fetches all routes from Firestore db, to be displayed in the routes list.
     */
    private void fetchRoutes() {
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
                        Log.d(mTAG, document.getId() + " => " + document.getData());
                    }
                }


                mRoutes = routes;
                mDataFetched = true;
                setUp();
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            public void onFailure(Exception e) {
                Log.d(mTAG, "Error fetching routes");
            }
        });
    }

    /**
     * Creates the fragment for the list of routes.
     */
    private void setRouteList() {
        FragmentManager fm = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("routes", mRoutes);
        mRouteFragment = new RoutesFragment();
        mRouteFragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.routesListContainer, mRouteFragment).commit();
    }


    /**
     * Start the RouteAcvity to see details for the selected route.
     * @param route
     */
    @Override
    public void onRouteSelected(Route route) {
        Intent intent = new Intent(getApplicationContext(),
                RouteActivity.class);
        intent.putExtra("route", route);
        intent.putExtra("location", route.getLocation());
        intent.putExtra("length", route.getLength());
        startActivity(intent);
    }

}
