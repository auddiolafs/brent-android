package is.hi.hbv601g.brent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

public class BikeListFragment extends Fragment {
    private RecyclerView mRecycleView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bikelist, container, false);
        mRecycleView = view.findViewById(R.id.bike_recycle_view);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        String[] data = {"TestString1", "TestString2", "TestString3"};
        mRecycleView.setAdapter(new BikeListAdapter(data));
        return view;
    }

    private class BikeListAdapter extends RecyclerView.Adapter<BikeListFragment.BikeHolder> {
        private String[] mData = null;
        public BikeListAdapter(String[] data) {
            super();
            mData = data;
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
            String dataString = mData[i];
            bikeHolder.mTextView1.setText(dataString);
            bikeHolder.mTextView2.setText(dataString);
            if (dataString == "TestString2") {
                bikeHolder.mLayout.setBackgroundColor(getResources().getColor(R.color.baby_purple));
            } else {
                bikeHolder.mLayout.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            }
        }

        @Override
        public int getItemCount() {
            return mData.length;
        }
    }

    private class BikeHolder extends RecyclerView.ViewHolder{
        TextView mTextView1;
        TextView mTextView2;
        FrameLayout mLayout;;
        public BikeHolder(@NonNull View itemView) {
            super(itemView);
            mLayout = (FrameLayout) itemView;
            mTextView1 = mLayout.findViewById(R.id.textView1);
            mTextView2 = mLayout.findViewById(R.id.textView2);
            mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("Woohoo!");
                }
            });
        }


    }
}
