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
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import is.hi.hbv601g.brent.R;
import is.hi.hbv601g.brent.models.Tour;

public class ToursFragment extends Fragment {

    private RecyclerView mRecycleView = null;
    private SelectionListener mListener;
    private ArrayList<Tour> mTours;
    private ArrayList<Tour> mToursUnfiltered;
    private int MarginLeftAndRight = 0;
    private int MarginTopAndBot = 0;
    private ToursFragment.TourListAdapter mAdapter;
    private boolean mLandscapeMode = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tours, container, false);
        mRecycleView = view.findViewById(R.id.tour_recycle_view);
        if (landscapeMode()) {
            mRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        } else {
            mRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        }
        //mListener = (SelectionListener) getActivity();
        Bundle bundle = getArguments();
        ArrayList<Tour> tours = bundle.getParcelableArrayList("tours");
        mTours = tours;
        mToursUnfiltered = (ArrayList<Tour>) tours.clone();
        mAdapter = new TourListAdapter();
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

    private class TourListAdapter extends RecyclerView.Adapter<ToursFragment.TourHolder> {
        public TourListAdapter() {
            super();
        }
        @NonNull
        @Override
        public ToursFragment.TourHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            FrameLayout layout = (FrameLayout) LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.viewholder_bike, viewGroup, false);
            TourHolder tourHolder = new TourHolder(layout, viewGroup.getMeasuredHeight());
            return tourHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ToursFragment.TourHolder tourHolder, int i) {
            Tour tour = mTours.get(i);
            tourHolder.mTour = tour;
            tourHolder.mCardTitle.setText(tour.getLocation());
            // tourHolder.mCardLength.setText(tour.getLength() + " km");
            Picasso.get().load(tour.getImage())
                    .placeholder(R.drawable.bike_hybrid)
                    .centerInside()
                    .resize(200, 200)
                    .into(tourHolder.mTourImage);
        }

        @Override
        public int getItemCount() {
            return mTours.size();
        }
    }

    private class TourHolder extends RecyclerView.ViewHolder {
        TextView mCardTitle;
        TextView mCardLength;
        TextView mCardDescription;
        TextView mCardLikes;
        ImageView mTourImage;
        FrameLayout mLayout;
        Tour mTour;
        public TourHolder(@NonNull View itemView, int parentHeight) {
            super(itemView);
            mLayout = (FrameLayout) itemView;
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) mLayout.getLayoutParams();
            mLayout.setLayoutParams(params);
            mCardTitle = mLayout.findViewById(R.id.card_title_id);
            mTourImage = mLayout.findViewById(R.id.card_image_id);
            mCardLength = mLayout.findViewById(R.id.card_info3_id);
            mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //mListener.onTourSelected(mTour);
                }
            });
        }
    }

    public interface SelectionListener {
        void onTourSelected(Tour tour);
    }

}
