package is.hi.hbv601g.brent.holders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import is.hi.hbv601g.brent.R;
import is.hi.hbv601g.brent.utils.ItemListListener;
import is.hi.hbv601g.brent.models.Bike;

public class ItemListViewHolder extends RecyclerView.ViewHolder {
    public TextView mCardTitle;
    public TextView mCardInfo1;
    public TextView mCardInfo2;
    public TextView mCardInfo3;
    public ImageView mCardImage;
    public FrameLayout mLayout;
    public Bike mBike;
    public ItemListListener mListener;

    public ItemListViewHolder(@NonNull View itemView, ItemListListener listener) {
        super(itemView);
        mLayout = (FrameLayout) itemView;
        mCardTitle = mLayout.findViewById(R.id.card_title);
        mCardInfo1 = mLayout.findViewById(R.id.card_info1);
        mCardInfo2 = mLayout.findViewById(R.id.card_info2);
        mCardInfo3 = mLayout.findViewById(R.id.card_info3);
        mCardImage = mLayout.findViewById(R.id.card_image_id);
        mListener = listener;
    }

}