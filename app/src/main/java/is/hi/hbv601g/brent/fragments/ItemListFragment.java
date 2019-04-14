package is.hi.hbv601g.brent.fragments;


import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.util.ArrayList;

import is.hi.hbv601g.brent.holders.ItemListViewHolder;
import is.hi.hbv601g.brent.R;
import is.hi.hbv601g.brent.utils.ItemListListener;

public class ItemListFragment extends Fragment {

    private RecyclerView mRecycleView = null;
    private ItemListListener mListener;
    private ArrayList<Parcelable> mData;
    private ListAdapter mAdapter;
    private static String DATA_KEY = "data";
    private int mViewHolderLayout = R.layout.viewholder_center_crop;
    private boolean mDoubleInLandscapeMode = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mRecycleView = view.findViewById(R.id.recycle_view);
        if (mDoubleInLandscapeMode && landscapeMode()) {
            mRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        } else {
            mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        mListener = (ItemListListener) getActivity();
        Bundle bundle = getArguments();
        mData = bundle.getParcelableArrayList(DATA_KEY);
        mAdapter = new ListAdapter();
        mRecycleView.setAdapter(mAdapter);
        return view;
    }

    private boolean landscapeMode() {
        Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();
        if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) return true;
        return false;
    }

    public static String getArgumentKey() {
        return DATA_KEY;
    }

    public void setViewHolderLayout(int id) {
        mViewHolderLayout = id;
    }

    public void doubleInLandscapeMode(boolean bool) {
        mDoubleInLandscapeMode = bool;
    }

    public void updateList() {
        mAdapter.notifyDataSetChanged();
    }

    private class ListAdapter extends RecyclerView.Adapter<ItemListViewHolder> {
        public ListAdapter() {
            super();
        }
        @NonNull
        @Override
        public ItemListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            FrameLayout layout = (FrameLayout) LayoutInflater.from(parent.getContext())
                    .inflate(mViewHolderLayout, parent, false);
            ItemListViewHolder itemListViewHolder = new ItemListViewHolder(layout, mListener);
            return itemListViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ItemListViewHolder itemListViewHolder, int i) {
            mListener.onBindViewHolder(itemListViewHolder, i);
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }
}

