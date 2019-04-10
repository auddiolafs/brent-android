package is.hi.hbv601g.brent.fragments;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import is.hi.hbv601g.brent.R;
import is.hi.hbv601g.brent.holders.ViewHolder;
import is.hi.hbv601g.brent.models.Route;

public class RoutesFragment extends Fragment {


    private RecyclerView mRecycleView = null;
    private SelectionListener mListener;
    private ArrayList<Route> mRoutes;
    private ArrayList<Route> mRoutesUnfiltered;
    private int MarginLeftAndRight = 0;
    private int MarginTopAndBot = 0;
    private RoutesFragment.RouteListAdapter mAdapter;


    public static RecyclerView.ViewHolder getViewHolder(@NonNull ViewGroup viewGroup, SelectionListener listener) {
        FrameLayout layout = (FrameLayout) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.viewholder_card, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(layout, viewGroup.getMeasuredHeight(), listener);
        return viewHolder;
    }

    public static void bindViewHolder(final ViewHolder viewHolder, final Route route) {
        viewHolder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.mListener.onRouteSelected(route);
            }
        });
        viewHolder.mCardTitle.setText(route.getLocation());
        viewHolder.mCardInfo3.setText(route.getLength() + " km");
        Picasso.get().load(route.getImage())
                .placeholder(R.drawable.menu_map)
                .centerInside()
                .resize(200, 200)
                .into(viewHolder.mCardImage);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_routes, container, false);
        mRecycleView = view.findViewById(R.id.route_recycle_view);
        if (landscapeMode()) {
            mRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        } else {
            mRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        }
        mListener = (SelectionListener) getActivity();
        Bundle bundle = getArguments();
        ArrayList<Route> routes = bundle.getParcelableArrayList("routes");
        mRoutes = routes;
        mRoutesUnfiltered = (ArrayList<Route>) routes.clone();
        mAdapter = new RouteListAdapter();
        mRecycleView.setAdapter(mAdapter);
        mRecycleView.addItemDecoration(new SpacesItemDecoration());
        return view;
    }

    private boolean landscapeMode() {
        Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();
        if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) return true;
        return false;
    }

    private class SpacesItemDecoration extends RecyclerView.ItemDecoration {

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            outRect.right = MarginLeftAndRight;
            outRect.left = MarginLeftAndRight;
            outRect.bottom = MarginTopAndBot;
            outRect.top = MarginTopAndBot;
        }
    }

    public class RouteListAdapter extends RecyclerView.Adapter<ViewHolder> {
        public RouteListAdapter() {
            super();
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return (ViewHolder) RoutesFragment.getViewHolder(viewGroup, mListener);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            Route route = mRoutes.get(i);
            RoutesFragment.bindViewHolder(viewHolder, route);
        }

        @Override
        public int getItemCount() {
            return mRoutes.size();
        }
    }


}
