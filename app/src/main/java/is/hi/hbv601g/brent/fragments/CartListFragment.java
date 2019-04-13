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
import is.hi.hbv601g.brent.activities.model.CartActivity;
import is.hi.hbv601g.brent.models.Tour;

public class CartListFragment extends Fragment {

    private RecyclerView mRecycleView = null;
    private ArrayList<Tour> mTours;
    private int MarginLeftAndRight = 0;
    private int MarginTopAndBot = 0;
    private CartListFragment.CartListAdapter mAdapter;
    private boolean mLandscapeMode = false;
    private ArrayList<CartActivity.Triplet> mData;


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

    private class CartListAdapter extends RecyclerView.Adapter<ViewHolder> {
        public CartListAdapter() {
            super();
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LinearLayout layout = (LinearLayout) LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.viewholder_cart, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(layout);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            CartActivity.Triplet triplet = mData.get(i);
            viewHolder.mProduct.setText(triplet.getProduct());
            viewHolder.mQuantity.setText(triplet.getQuantity());
            viewHolder.mPrice.setText(triplet.getPrice());
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mLayout;
        public TextView mProduct;
        public TextView mQuantity;
        public TextView mPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mLayout = (LinearLayout) itemView.findViewById(R.id.layout);
            mProduct = itemView.findViewById(R.id.product);
            mQuantity = itemView.findViewById(R.id.quantity);
            mPrice = itemView.findViewById(R.id.price);
            mPrice.setWidth(100);
            mProduct.setWidth(250);
            mQuantity.setWidth(100);
        }
    }


}
