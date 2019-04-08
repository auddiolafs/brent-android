package is.hi.hbv601g.brent.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import is.hi.hbv601g.brent.R;
import is.hi.hbv601g.brent.models.Route;

public class MiniCardsAdapter extends RecyclerView.Adapter<MiniCardsAdapter.MyViewHolder> {

    Context mContext;
    List<Route> mData;

    public MiniCardsAdapter (Context mContext, List<Route> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(mContext).inflate(R.layout.viewholder_card, parent, false);

        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.mTitle.setText(mData.get(position).getLocation());
        // Glide.with(mContext).load(mData.get(position).getPicture()).into(holder.imgPost);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mTitle;
        ImageView mImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.card_title_id);
            mImage = itemView.findViewById(R.id.card_image_id);
        }
    }
}
