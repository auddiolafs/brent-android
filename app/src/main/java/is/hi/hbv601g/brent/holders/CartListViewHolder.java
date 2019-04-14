package is.hi.hbv601g.brent.holders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import is.hi.hbv601g.brent.R;


public class CartListViewHolder extends RecyclerView.ViewHolder {
    LinearLayout mLayout;
    public TextView mProduct;
    public TextView mQuantity;
    public TextView mPrice;


    public CartListViewHolder(@NonNull View itemView) {
        super(itemView);
        mLayout = itemView.findViewById(R.id.layout);
        mProduct = itemView.findViewById(R.id.product);
        mQuantity = itemView.findViewById(R.id.quantity);
        mPrice = itemView.findViewById(R.id.price);
        mPrice.setWidth(100);
        mProduct.setWidth(250);
        mQuantity.setWidth(100);
    }
}