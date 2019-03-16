package is.hi.hbv601g.brent.fragments;

import android.content.Context;
import android.graphics.Rect;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import is.hi.hbv601g.brent.R;
import is.hi.hbv601g.brent.adapters.MiniCardsAdapter;
import is.hi.hbv601g.brent.models.Route;
import is.hi.hbv601g.brent.models.Route;

public class RoutesFragment extends Fragment {


    private RecyclerView mRecycleView = null;
    private SelectionListener mListener;
    private ArrayList<Route> mRoutes;
    private ArrayList<Route> mRoutesUnfiltered;
    private int MarginLeftAndRight = 0;
    private int MarginTopAndBot = 0;
    private RoutesFragment.RouteListAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_routes, container, false);
        mRecycleView = view.findViewById(R.id.route_recycle_view);
        mRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
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

    private class RouteListAdapter extends RecyclerView.Adapter<RoutesFragment.RouteHolder> {
        public RouteListAdapter() {
            super();
        }
        @NonNull
        @Override
        public RoutesFragment.RouteHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            FrameLayout layout = (FrameLayout) LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.viewholder_bike, viewGroup, false);
            RouteHolder routeHolder = new RouteHolder(layout, viewGroup.getMeasuredHeight());
            return routeHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RoutesFragment.RouteHolder routeHolder, int i) {
            Route route = mRoutes.get(i);
            routeHolder.mRoute = route;
            routeHolder.mCardTitle.setText(route.getLocation());
            routeHolder.mCardLength.setText(route.getLength());
        }

        @Override
        public int getItemCount() {
            return mRoutes.size();
        }
    }

    private class RouteHolder extends RecyclerView.ViewHolder {
        TextView mCardTitle;
        TextView mCardLength;
        TextView mCardDescription;
        TextView mCardLikes;
        ImageView mRouteImage;
        FrameLayout mLayout;
        Route mRoute;
        public RouteHolder(@NonNull View itemView, int parentHeight) {
            super(itemView);
            mLayout = (FrameLayout) itemView;
            GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) mLayout.getLayoutParams();
            mLayout.setLayoutParams(params);
            mCardTitle = mLayout.findViewById(R.id.card_title_id);
            mRouteImage = mLayout.findViewById(R.id.card_image_id);
            mCardLength = mLayout.findViewById(R.id.card_info3_id);
            mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onRouteSelected(mRoute);
                }
            });
        }
    }

    public interface SelectionListener {
        void onRouteSelected(Route Route);
    }

}
