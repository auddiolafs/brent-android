package is.hi.hbv601g.brent.fragments;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import is.hi.hbv601g.brent.R;
import is.hi.hbv601g.brent.holders.ViewHolder;
import is.hi.hbv601g.brent.models.Bike;
import is.hi.hbv601g.brent.models.Route;

public class BookingListFragment extends Fragment {

    private RecyclerView mRecycleView = null;
    private SelectionListener mListener;
    private ArrayList<Route> mRoutes;
    private int MarginLeftAndRight = 0;
    private int MarginTopAndBot = 0;
    private BookingListFragment.BookingListAdapter mAdapter;
    private ArrayList<Bike> mBikes;
    private MultiMap<String, Object> mData;


    private class MultiMap<K,V> {
        private final ArrayList<K> mKeys;
        private final ArrayList<V> mVals;
        public MultiMap(ArrayList<K> keys, ArrayList<V> vals) {
            mKeys = keys;
            mVals = vals;
        }
        public int size() {
            return mKeys.size();
        }

        public Tuple get(int i) {
            return new Tuple(mKeys.get(i), mVals.get(i));
        }
    }

    private class Tuple<K,V> {
        private final K mKey;
        private final V mVal;

        public Tuple(K key, V val) {
            mKey = key;
            mVal = val;
        }

        public K getKey() {
            return mKey;
        }

        public V getVal() {
            return mVal;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookings, container, false);
        mRecycleView = view.findViewById(R.id.booking_recycle_view);
        if (landscapeMode()) {
            mRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        } else {
            mRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        }
        mListener = (SelectionListener) getActivity();
        mData = createMultiMap();
        mAdapter = new BookingListFragment.BookingListAdapter();
        mRecycleView.setAdapter(mAdapter);
        mRecycleView.addItemDecoration(new BookingListFragment.SpacesItemDecoration());
        return view;
    }

    private MultiMap createMultiMap() {
        Bundle bundle = getArguments();
        ArrayList<Bike> bikes = bundle.getParcelableArrayList("bikes");
        ArrayList<String> keys = new ArrayList<>();
        ArrayList<Object> vals = new ArrayList<>();
        for (Bike bike : bikes){
            keys.add("bike");
            vals.add(bike);
        }
        return new MultiMap(keys, vals);
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

    private class BookingListAdapter extends RecyclerView.Adapter<ViewHolder> {
        public BookingListAdapter() {
            super();
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            FrameLayout layout = (FrameLayout) LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.viewholder_card_booking, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(layout, viewGroup.getMeasuredHeight(), mListener);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            Tuple tuple = mData.get(i);
            String key = (String) tuple.getKey();
            if (key == "bike") {
                Bike bike = (Bike) tuple.getVal();
                BikeListFragment.bindViewHolder(viewHolder, bike);
            } else {
                Route route = (Route) tuple.getVal();
                RoutesFragment.bindViewHolder(viewHolder, route);
            }
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }


}
