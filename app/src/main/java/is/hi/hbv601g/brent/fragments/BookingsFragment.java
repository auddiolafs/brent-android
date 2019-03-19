package is.hi.hbv601g.brent.fragments;

import android.content.Context;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import is.hi.hbv601g.brent.R;
import is.hi.hbv601g.brent.adapters.BookingAdapter;
import is.hi.hbv601g.brent.models.Booking;
import is.hi.hbv601g.brent.models.Booking;

public class BookingsFragment extends Fragment {

    private RecyclerView mRecycleView = null;
    private BookingsFragment.SelectionListener mListener;
    private ArrayList<Booking> mBookings;
    private ArrayList<Booking> mBookingsUnfiltered;
    private int MarginLeftAndRight = 0;
    private int MarginTopAndBot = 0;
    private BookingsFragment.BookingListAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookings, container, false);
        mRecycleView = view.findViewById(R.id.booking_recycle_view);
        mRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        mListener = (BookingsFragment.SelectionListener) getActivity();
        Bundle bundle = getArguments();
        ArrayList<Booking> bookings = bundle.getParcelableArrayList("bookings");
        mBookings = bookings;
        mBookingsUnfiltered = (ArrayList<Booking>) bookings.clone();
        mAdapter = new BookingsFragment.BookingListAdapter();
        mRecycleView.setAdapter(mAdapter);
        mRecycleView.addItemDecoration(new BookingsFragment.SpacesItemDecoration());
        return view;
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

    private class BookingListAdapter extends RecyclerView.Adapter<BookingsFragment.BookingHolder> {
        public BookingListAdapter() {
            super();
        }
        @NonNull
        @Override
        public BookingsFragment.BookingHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            FrameLayout layout = (FrameLayout) LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.viewholder_bike, viewGroup, false);
            BookingsFragment.BookingHolder bookingHolder = new BookingsFragment.BookingHolder(layout, viewGroup.getMeasuredHeight());
            return bookingHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull BookingsFragment.BookingHolder bookingHolder, int i) {
            Booking booking = mBookings.get(i);
            bookingHolder.mBooking = booking;
            bookingHolder.mCardTitle.setText(booking.getId());
//            bookingHolder.mCardLength.setText(booking.getLength() + " km");

        }

        @Override
        public int getItemCount() {
            return mBookings.size();
        }
    }

    private class BookingHolder extends RecyclerView.ViewHolder {
        TextView mCardTitle;
        TextView mCardLength;
        TextView mCardDescription;
        TextView mCardLikes;
        ImageView mBookingImage;
        FrameLayout mLayout;
        Booking mBooking;
        public BookingHolder(@NonNull View itemView, int parentHeight) {
            super(itemView);
            mLayout = (FrameLayout) itemView;
            GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) mLayout.getLayoutParams();
            mLayout.setLayoutParams(params);
            mCardTitle = mLayout.findViewById(R.id.card_title_id);
            mBookingImage = mLayout.findViewById(R.id.card_image_id);
            mCardLength = mLayout.findViewById(R.id.card_info3_id);
//            mLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mListener.onBookingSelected(mBooking);
//                }
//            });
        }
    }

    public interface SelectionListener {
        void onBookingSelected(Booking Booking);
    }

}
