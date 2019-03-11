package is.hi.hbv601g.brent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class BikeListFragment extends Fragment {
    private RecyclerView mRecycleView = null;
    private SelectionListener mListener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bikelist, container, false);
        mRecycleView = view.findViewById(R.id.bike_recycle_view);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mListener = (SelectionListener) getActivity();
        Bundle bundle = getArguments();
        ArrayList<Bike> bikes = bundle.getParcelableArrayList("bikes");
        Map<String, String> data = new LinkedHashMap<>();
        String color;
        for (int i = 0; i<bikes.size(); i += 1) {
            if (i % 2 == 0) {
                color = "baby_purple";
            } else {
                color = "colorAccent";
            }
            data.put(bikes.get(i).getName(), color);
        }
        mRecycleView.setAdapter(new BikeListAdapter(data, bikes));
        return view;
    }

    private class BikeListAdapter extends RecyclerView.Adapter<BikeListFragment.BikeHolder> {
        private Map<String, String> mData;
        private ArrayList<Bike> mBikes;
        public BikeListAdapter(Map<String, String> data, ArrayList<Bike> bikes) {
            super();
            mData = data;
            mBikes = bikes;
        }
        @NonNull
        @Override
        public BikeListFragment.BikeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            FrameLayout layout = (FrameLayout) LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.viewholder_bike, viewGroup, false);
            BikeHolder bikeHolder = new BikeHolder(layout);
            return bikeHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull BikeListFragment.BikeHolder bikeHolder, int i) {
            Object[] keys = mData.keySet().toArray();
            String dataString = keys[i].toString();
            String color = mData.get(keys[i].toString());
            bikeHolder.mBike = mBikes.get(i);
            bikeHolder.mTextView1.setText(dataString);
            bikeHolder.mTextView2.setText(dataString);
            if (color == "baby_purple") {
                bikeHolder.mLayout.setBackgroundColor(getResources().getColor(R.color.baby_purple));
            } else {
                bikeHolder.mLayout.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            }
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }

    private class BikeHolder extends RecyclerView.ViewHolder {
        TextView mTextView1;
        TextView mTextView2;
        FrameLayout mLayout;
        Bike mBike;
        public BikeHolder(@NonNull View itemView) {
            super(itemView);
            mLayout = (FrameLayout) itemView;
            mTextView1 = mLayout.findViewById(R.id.textView1);
            mTextView2 = mLayout.findViewById(R.id.textView2);
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
