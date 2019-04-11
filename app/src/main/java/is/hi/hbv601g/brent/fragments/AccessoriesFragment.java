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
import is.hi.hbv601g.brent.models.Accessory;

public class AccessoriesFragment extends Fragment {

    private RecyclerView mRecycleView = null;
    private SelectionListener mListener;
    private ArrayList<Accessory> mAccessories;
    private int MarginLeftAndRight = 0;
    private int MarginTopAndBot = 0;
    private AccessoriesFragment.AccessoriesListAdapter mAdapter;
    private boolean mLandscapeMode = false;

    public static void bindViewHolder(final ViewHolder viewHolder, final Accessory accessory) {
        viewHolder.mCardTitle.setText(accessory.getName());
        Picasso.get().load(accessory.getImage())
                .placeholder(R.drawable.menu_tour)
                .centerInside()
                .resize(200, 200)
                .into(viewHolder.mCardImage);
        if (accessory.getType().equals("lock")) {
            viewHolder.mCardImage.setImageResource(R.drawable.accessories_lock);
        } else if (accessory.getType().equals("helmet")) {
            viewHolder.mCardImage.setImageResource(R.drawable.accessories_helmet);
        } else if (accessory.getType().equals("kit")) {
            viewHolder.mCardImage.setImageResource(R.drawable.accessories_repair_kit);
        } else if (accessory.getType().equals("lights")) {
            viewHolder.mCardImage.setImageResource(R.drawable.accessories_lights);
        }
        viewHolder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.mListener.onAccessorySelected(accessory);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accessories, container, false);
        mRecycleView = view.findViewById(R.id.accessories_recycle_view);
        if (landscapeMode()) {
            mRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        } else {
            mRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        }
        mListener = (SelectionListener) getActivity();
        Bundle bundle = getArguments();
        ArrayList<Accessory> accessories = bundle.getParcelableArrayList("accessories");
        mAccessories = accessories;
        mAdapter = new AccessoriesListAdapter();
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

    private class AccessoriesListAdapter extends RecyclerView.Adapter<ViewHolder> {
        public AccessoriesListAdapter() {
            super();
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            FrameLayout layout = (FrameLayout) LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.viewholder_card, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(layout, viewGroup.getMeasuredHeight(), mListener);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            Accessory accessory = mAccessories.get(i);
            AccessoriesFragment.bindViewHolder(viewHolder, accessory);
        }

        @Override
        public int getItemCount() {
            return mAccessories.size();
        }
    }

}
