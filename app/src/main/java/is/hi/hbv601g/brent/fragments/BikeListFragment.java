package is.hi.hbv601g.brent.fragments;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;

import is.hi.hbv601g.brent.models.Bike;
import is.hi.hbv601g.brent.R;

public class BikeListFragment extends Fragment {
    private RecyclerView mRecycleView = null;
    private SelectionListener mListener;
    private int counter = 0;
    private Long GapWidth = null;
    private Long MarginTopAndBot = new Long(25);

    private class Tuple<K,V> {
        private K mX;
        private V mY;

        public Tuple(K x, V y) {
            mX = x;
            mY = y;
        }

        public K getFirstItem() {
            return mX;
        }

        public V getSecondItem() {
            return mY;
        }
    }

    private class MultiMap<K,V> implements Iterable{
        private ArrayList<K> mKeys;
        private ArrayList<V> mValues;

        public MultiMap() {
            mKeys = new ArrayList<>();
            mValues = new ArrayList<>();
        }
        public void put(K key, V value) {
            mKeys.add(key);
            mValues.add(value);
        }
        public Tuple<K,V> get(int index) {
            return new Tuple<>(mKeys.get(index), mValues.get(index));
        }
        public int size() {
            return mKeys.size();
        }
        @Override
        public Iterator iterator() {
            return new Iterator() {
                @Override
                public boolean hasNext() {
                    if (counter == mKeys.size()) {
                        counter = 0;
                        return false;
                    }
                    return true;
                }
                @Override
                public Tuple<K,V> next() {
                    counter += 1;
                    return new Tuple<>(mKeys.get(counter), mValues.get(counter));
                }
            };
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bikelist, container, false);
        mRecycleView = view.findViewById(R.id.bike_recycle_view);
        mRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        System.out.println("GapWidth: " + GapWidth);
        mListener = (SelectionListener) getActivity();
        Bundle bundle = getArguments();
        ArrayList<Bike> bikes = bundle.getParcelableArrayList("bikes");
        System.out.println("Number of bikes: " + bikes.size());
        MultiMap<String, String> data = new MultiMap<>();
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
        mRecycleView.addItemDecoration(new SpacesItemDecoration());
        return view;
    }

    private class SpacesItemDecoration extends RecyclerView.ItemDecoration {

        @Override
        public void getItemOffsets(Rect outRect, int position,
                                   RecyclerView parent) {
            if (position == 1) {
                outRect.right = GapWidth.intValue()/3;
                outRect.left = GapWidth.intValue()/6;
            } else {
                outRect.right = GapWidth.intValue()/6;
                outRect.left = GapWidth.intValue()/3;
            }
            outRect.bottom = MarginTopAndBot.intValue();
            outRect.top = MarginTopAndBot.intValue();
        }
    }

    private class BikeListAdapter extends RecyclerView.Adapter<BikeListFragment.BikeHolder> {
        private MultiMap<String, String> mData;
        private ArrayList<Bike> mBikes;
        public BikeListAdapter(MultiMap<String,String> data, ArrayList<Bike> bikes) {
            super();
            mData = data;
            mBikes = bikes;
        }
        @NonNull
        @Override
        public BikeListFragment.BikeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            FrameLayout layout = (FrameLayout) LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.viewholder_bike, viewGroup, false);
            if (GapWidth == null) {
                GapWidth = new Long(viewGroup.getMeasuredWidth()/8);
            }
            BikeHolder bikeHolder = new BikeHolder(layout, viewGroup.getMeasuredWidth(), viewGroup.getMeasuredHeight());
            return bikeHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull BikeListFragment.BikeHolder bikeHolder, int i) {
            Tuple<String,String> tuple = mData.get(i);
            String dataString = tuple.getFirstItem();
            String color = tuple.getSecondItem();
            LinearLayout.LayoutParams layoutParams = bikeHolder.mParams;
            bikeHolder.mLayout.setLayoutParams(layoutParams);
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
        LinearLayout.LayoutParams mParams;
        public BikeHolder(@NonNull View itemView, int parentWidth, int parentHeight) {
            super(itemView);
            mLayout = (FrameLayout) itemView;
            mParams = new LinearLayout.LayoutParams((parentWidth/2)-GapWidth.intValue(),parentHeight/3);
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
