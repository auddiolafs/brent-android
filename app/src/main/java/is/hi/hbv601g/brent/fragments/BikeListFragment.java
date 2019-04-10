package is.hi.hbv601g.brent.fragments;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;

import is.hi.hbv601g.brent.holders.ViewHolder;
import is.hi.hbv601g.brent.models.Bike;
import is.hi.hbv601g.brent.R;

public class BikeListFragment extends Fragment {
    private RecyclerView mRecycleView = null;
    private SelectionListener mListener;
    private ArrayList<Bike> mDisplayedBikes;
    private ArrayList<Bike> mAllBikes;
    private int MarginLeftAndRight = 0;
    private int MarginTopAndBot = 0;
    private BikeListAdapter mAdapter;
    public static String BIKES_KEY = "bikes";


    public static void bindViewHolder(final ViewHolder viewHolder, final Bike bike) {
        Log.d("price", bike.getPrice().toString());
        viewHolder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.mListener.onBikeSelected(bike);
            }
        });
        viewHolder.mCardTitle.setText(bike.getName());
        viewHolder.mCardInfo3.setText(bike.getPrice().toString());
    }


    public void filterBikes(String selectedType, String selectedSize) throws InterruptedException {
        ArrayList<Bike> res = new ArrayList<>();
        for (Bike bike : mAllBikes) {
            Log.d("BikeListFragment", "Bike added");
            if (bike.getType() != null &&
                    (bike.getType().equals(selectedType) && bike.getSize().equals(selectedSize))) {
                res.add(bike);
            } else if (bike.getType() != null &&
                    (bike.getType().equals(selectedType) && selectedSize.equals("All"))) {
                res.add(bike);
            } else if (selectedType.equals("All") && bike.getSize().equals(selectedSize)) {
                res.add(bike);
            } else if (selectedType.equals("All") && selectedSize.equals("All")) {
                res.add(bike);
            }
        }
        Boolean[] initVals = new Boolean[mDisplayedBikes.size()];
        for (int i = 0; i < initVals.length; i += 1) {
            initVals[i] = new Boolean(false);
        }
        ArrayBlockingQueue<Boolean> shouldBeInList = new ArrayBlockingQueue<>(mAllBikes.size(), true, Arrays.asList(initVals));
        int n = res.size() - 1;
        while (!(n < 0)) {
            Bike bikeInRes = res.get(n);
            boolean bikePresent = false;
            for (int i = 0; i<mDisplayedBikes.size(); i += 1) {
                Bike bike = mDisplayedBikes.get(i);
                if (bike == bikeInRes) {
                    bikePresent = true;
                    Object[] array = shouldBeInList.toArray();
                    Boolean[] vals  = Arrays.copyOf(array, array.length, Boolean[].class);
                    vals[i] = new Boolean(true);
                    shouldBeInList = new ArrayBlockingQueue<>(mAllBikes.size(), true, Arrays.asList(vals));
                    break;
                }
            }
            if (!bikePresent) {
                mDisplayedBikes.add(bikeInRes);
                shouldBeInList.add(new Boolean(true));
            }
            n -= 1;
        }
        n = 0;
        while (shouldBeInList.size() != 0) {
            if (!shouldBeInList.take()) {
                mDisplayedBikes.remove(n);
            } else {
                n += 1;
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bikelist, container, false);
        mRecycleView = view.findViewById(R.id.bike_recycle_view);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mListener = (SelectionListener) getActivity();
        Bundle bundle = getArguments();
        ArrayList<Bike> bikes = bundle.getParcelableArrayList(BIKES_KEY);
        mDisplayedBikes = bikes;
        mAllBikes = (ArrayList<Bike>) bikes.clone();
        mAdapter = new BikeListAdapter();
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

    public class BikeListAdapter extends RecyclerView.Adapter<ViewHolder> {
        public BikeListAdapter() {
            super();
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            FrameLayout layout = (FrameLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.viewholder_card, parent, false);
            ViewHolder viewHolder = new ViewHolder(layout, parent.getMeasuredHeight(), mListener);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            Bike bike = mDisplayedBikes.get(i);
            BikeListFragment.bindViewHolder(viewHolder, bike);
        }

        @Override
        public int getItemCount() {
            return mDisplayedBikes.size();
        }
    }

}
