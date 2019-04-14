package is.hi.hbv601g.brent.activities.model;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import is.hi.hbv601g.brent.R;
import is.hi.hbv601g.brent.fragments.ItemListFragment;
import is.hi.hbv601g.brent.utils.ItemListListener;
import is.hi.hbv601g.brent.holders.ItemListViewHolder;
import is.hi.hbv601g.brent.models.Route;
import is.hi.hbv601g.brent.services.RouteService;

public class RoutesActivity extends ItemListListener {

    private ArrayList<Route> mRoutes;
    private ItemListFragment mItemListFragment;
    private RouteService routeService = new RouteService(this);
    private boolean mDataFetched = false;
    private static final String KEY_ROUTES = "Routes";

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
            routeService.fetchRoutes();
        } else {
            setContentView(R.layout.activity_routes);
            super.setUp();
            mRoutes = routeService.getRoutes();
            setRouteList();
        }
    }

    public void setIsDataFetched(boolean dataFetched) {
        this.mDataFetched = dataFetched;
    }

    /**
     * Creates the fragment for the list of routes.
     */
    private void setRouteList() {
        FragmentManager fm = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ItemListFragment.getArgumentKey(), mRoutes);
        mItemListFragment = new ItemListFragment();
        mItemListFragment.setArguments(bundle);
        mItemListFragment.setViewHolderLayout(R.layout.viewholder_center_crop);
        mItemListFragment.doubleInLandscapeMode(true);
        fm.beginTransaction().replace(R.id.routesListContainer, mItemListFragment).commit();
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

    @Override
    public void onBindViewHolder(ItemListViewHolder itemListViewHolder, int index) {
        bindViewHolder(itemListViewHolder, mRoutes.get(index));
    }

    private void bindViewHolder(final ItemListViewHolder itemListViewHolder, final Route route) {
        itemListViewHolder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemListViewHolder.mListener.onRouteSelected(route);
            }
        });
        itemListViewHolder.mCardTitle.setText(route.getLocation());
        itemListViewHolder.mCardInfo3.setText(route.getLength() + " km");
        Picasso.get().load(route.getImage())
                .placeholder(R.drawable.menu_map)
                .centerInside()
                .resize(200, 200)
                .into(itemListViewHolder.mCardImage);
    }

}
