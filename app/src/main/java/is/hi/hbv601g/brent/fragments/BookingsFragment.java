package is.hi.hbv601g.brent.fragments;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import is.hi.hbv601g.brent.R;
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
        if (landscapeMode()) {
            mRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        } else {
            mRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        }
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

    private class BookingListAdapter extends RecyclerView.Adapter<BookingsFragment.BookingHolder> {
        public BookingListAdapter() {
            super();
        }
        @NonNull
        @Override
        public BookingsFragment.BookingHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            FrameLayout layout = (FrameLayout) LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.viewholder_card, viewGroup, false);
            BookingsFragment.BookingHolder bookingHolder = new BookingsFragment.BookingHolder(layout, viewGroup.getMeasuredHeight());
            return bookingHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull BookingsFragment.BookingHolder bookingHolder, int i) {
            Booking booking = mBookings.get(i);
            bookingHolder.mBooking = booking;
            bookingHolder.mCardTitle.setText(booking.getId());
            bookingHolder.mCardPrice.setText(booking.getPrice() + " kr");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
            String startDateString = dateFormat.format(booking.getStartDate());
            String endDateString = dateFormat.format(booking.getEndDate());
            bookingHolder.mCardDate.setText(startDateString + " to " + endDateString);
        }

        @Override
        public int getItemCount() {
            return mBookings.size();
        }
    }

    private class BookingHolder extends RecyclerView.ViewHolder {
        TextView mCardTitle;
        TextView mCardPrice;
        TextView mCardDate;
        TextView mCardDescription;
        ImageView mBookingImage;
        FrameLayout mLayout;
        Booking mBooking;
        public BookingHolder(@NonNull View itemView, int parentHeight) {
            super(itemView);
            mLayout = (FrameLayout) itemView;
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) mLayout.getLayoutParams();
            mLayout.setLayoutParams(params);
            mCardTitle = mLayout.findViewById(R.id.card_title);
            mBookingImage = mLayout.findViewById(R.id.card_image_id);
            mCardPrice = mLayout.findViewById(R.id.card_info3);
            mCardDate = mLayout.findViewById(R.id.card_info2);
            mCardDescription = mLayout.findViewById(R.id.card_info1);
            mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onBookingSelected(mBooking);
                }
            });
        }
    }

    public interface SelectionListener {
        void onBookingSelected(Booking Booking);
    }

}
