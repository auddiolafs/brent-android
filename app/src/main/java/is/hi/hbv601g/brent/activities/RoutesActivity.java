package is.hi.hbv601g.brent.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import is.hi.hbv601g.brent.R;
import is.hi.hbv601g.brent.adapters.MiniCardsAdapter;
import is.hi.hbv601g.brent.fragments.BikeListFragment;
import is.hi.hbv601g.brent.fragments.RoutesFragment;
import is.hi.hbv601g.brent.models.Bike;
import is.hi.hbv601g.brent.models.Route;

public class RoutesActivity extends CurrentActivity implements RoutesFragment.SelectionListener {

    private ArrayList<Route> mRoutes = new ArrayList<>();
    private RoutesFragment mRouteFragment;
    private static final String TAG = "RoutesActivity";
    private FirebaseFirestore mDB = FirebaseFirestore.getInstance();

    ImageButton toolbarProfile;
    ImageButton toolbarHome;
    ImageButton toolbarCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.connected) {
            setUp();
        }

    }

    @Override
    public void setUp() {
        setContentView(R.layout.activity_loading);

        fetchRoutes();
        // Get toolbar in layout (defined in xml file)

        toolbarProfile = findViewById(R.id.toolbar_profile);
        toolbarProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userIntent = new Intent(getApplicationContext(), UserActivity.class);
                startActivity(userIntent);
            }
        });
        toolbarHome = findViewById(R.id.toolbar_home);
        toolbarHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(home);
            }
        });
        toolbarCart = findViewById(R.id.toolbar_cart);
        toolbarCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cart = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(cart);
            }
        });

    }

    private void fetchRoutes() {
        final ArrayList<Route> routes = new ArrayList<>();
        final Task<QuerySnapshot> task = mDB.collection("routes")
                .get();

        task.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                setContentView(R.layout.activity_routes);
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Log.d(TAG, document.getId());
                    Route route = Route.toEntity(document.getId(), document.getData());
                    if (route == null) {
                        Log.d(TAG, "error");
                    } else {
                        routes.add(route);
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                }

                mRoutes = routes;
                Log.d(TAG, "mRoutes >> " + Integer.toString(mRoutes.size()));
                // setDatePickers();
                setRouteList();
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            public void onFailure(Exception e) {
                Log.d(TAG, "Error fetching bikes");
            }
        });
    }

    private void setRouteList() {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("routes", mRoutes);
        mRouteFragment = new RoutesFragment();
        mRouteFragment.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.routesListContainer, mRouteFragment).commit();
    }


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
