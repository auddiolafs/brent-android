package is.hi.hbv601g.brent.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import is.hi.hbv601g.brent.R;
import is.hi.hbv601g.brent.holders.CartListViewHolder;
import is.hi.hbv601g.brent.models.Tour;
import is.hi.hbv601g.brent.utils.CartListItem;

public class CartListFragment extends Fragment {

    private RecyclerView mRecycleView = null;
    private CartListFragment.CartListAdapter mAdapter;
    private ArrayList<CartListItem> mData;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        mRecycleView = view.findViewById(R.id.cart_recycle_view);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Bundle bundle = getArguments();
        mData = bundle.getParcelableArrayList("data");
        mAdapter = new CartListAdapter();
        mRecycleView.setAdapter(mAdapter);
        return view;
    }

    private class CartListAdapter extends RecyclerView.Adapter<CartListViewHolder> {
        public CartListAdapter() {
            super();
        }
        @NonNull
        @Override
        public CartListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LinearLayout layout = (LinearLayout) LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.viewholder_cart, viewGroup, false);
            CartListViewHolder viewHolder = new CartListViewHolder(layout);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull CartListViewHolder viewHolder, int i) {
            CartListItem cartListItem = mData.get(i);
            viewHolder.mProduct.setText(cartListItem.getProduct());
            viewHolder.mQuantity.setText(cartListItem.getQuantity());
            viewHolder.mPrice.setText(cartListItem.getPrice());
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }



}
