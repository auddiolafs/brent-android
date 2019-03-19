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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;

import is.hi.hbv601g.brent.models.Bike;
import is.hi.hbv601g.brent.R;

public class BikeListFragment extends Fragment {
    private RecyclerView mRecycleView = null;
    private SelectionListener mListener;
    private ArrayList<Bike> mBikes;
    private ArrayList<Bike> mBikesUnfiltered;
    private int MarginLeftAndRight = 0;
    private int MarginTopAndBot = 0;
    private BikeListAdapter mAdapter;
    public static String BIKES_KEY = "bikes";


    public void filterBikes(String selectedType, String selectedSize) throws InterruptedException {
        ArrayList<Bike> res = new ArrayList<>();
        for (Bike bike : mBikesUnfiltered) {
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
        Boolean[] initVals = new Boolean[mBikes.size()];
        for (int i = 0; i < initVals.length; i += 1) {
            initVals[i] = new Boolean(false);
        }
        ArrayBlockingQueue<Boolean> shouldBeInList = new ArrayBlockingQueue<>(mBikesUnfiltered.size(), true, Arrays.asList(initVals));
        int n = res.size() - 1;
        while (!(n < 0)) {
            Bike bikeInRes = res.get(n);
            boolean bikePresent = false;
            for (int i = 0; i<mBikes.size(); i += 1) {
                Bike bike = mBikes.get(i);
                if (bike == bikeInRes) {
                    bikePresent = true;
                    Object[] array = shouldBeInList.toArray();
                    Boolean[] vals  = Arrays.copyOf(array, array.length, Boolean[].class);
                    vals[i] = new Boolean(true);
                    shouldBeInList = new ArrayBlockingQueue<>(mBikesUnfiltered.size(), true, Arrays.asList(vals));
                    break;
                }
            }
            if (!bikePresent) {
                mBikes.add(bikeInRes);
                shouldBeInList.add(new Boolean(true));
            }
            n -= 1;
        }
        n = 0;
        while (shouldBeInList.size() != 0) {
            if (!shouldBeInList.take()) {
                mBikes.remove(n);
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
        mBikes = bikes;
        mBikesUnfiltered = (ArrayList<Bike>) bikes.clone();
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

    private class BikeListAdapter extends RecyclerView.Adapter<BikeListFragment.BikeHolder> {
        public BikeListAdapter() {
            super();
        }
        @NonNull
        @Override
        public BikeListFragment.BikeHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            FrameLayout layout = (FrameLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.viewholder_bike, parent, false);
            BikeHolder bikeHolder = new BikeHolder(layout, parent.getMeasuredHeight());
            return bikeHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull BikeListFragment.BikeHolder bikeHolder, int i) {
            Bike bike = mBikes.get(i);
            bikeHolder.mBike = bike;
            bikeHolder.mCardTitle.setText(bike.getName());
            bikeHolder.mCardPrice.setText(bike.getPrice());
        }

        @Override
        public int getItemCount() {
            return mBikes.size();
        }
    }

    private class BikeHolder extends RecyclerView.ViewHolder {
        TextView mCardTitle;
        TextView mCardPrice;
        ImageView mBikeImage;
        FrameLayout mLayout;
        Bike mBike;
        public BikeHolder(@NonNull View itemView, int parentHeight) {
            super(itemView);
            mLayout = (FrameLayout) itemView;
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) mLayout.getLayoutParams();
            params.height = parentHeight/3;
            mLayout.setLayoutParams(params);
            mCardTitle = mLayout.findViewById(R.id.card_title_id);
            mBikeImage = mLayout.findViewById(R.id.card_image_id);
            mCardPrice = mLayout.findViewById(R.id.card_info3_id);
            mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onBikeSelected(mBike);
                }
            });
        }
    }

    public interface SelectionListener {
        void onBikeSelected(Bike bike);
    }
}
