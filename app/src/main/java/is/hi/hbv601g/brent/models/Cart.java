package is.hi.hbv601g.brent.models;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart {

    private List<Bike> mBikes = new ArrayList<>();
    private List<Accessory> mAccessories = new ArrayList<>();
    private List<Tour> mTours = new ArrayList<>();
    private Date mStartDate;
    private Date mEndDate;
    private int mTotalPrice;

    private static Cart sCart = new Cart();
    private static final String TAG = "CartActivity";

    private static final FirebaseFirestore mDB = FirebaseFirestore.getInstance();
    private Map<Long, Booking> mBookings;

    private Cart() {
        mBookings = new HashMap();
    }

    public static Cart getCart() {
        return sCart;
    }

    public void resetCart() {
        sCart = new Cart();
    }

    public List<Bike> getBikes() { return mBikes; }

    public List<Accessory> getAccessories() {
        return mAccessories;
    }

    public List<Tour> getTours() {
        return mTours;
    }

    public void setTours(List<Tour> tours) {
        this.mTours = tours;
    }

    public Date getStartDate() {
        return mStartDate;
    }

    public void setStartDate(Date startDate) {
        this.mStartDate = startDate;
    }

    public Date getEndDate() {
        return mEndDate;
    }

    public void setEndDate(Date endDate) {
        this.mEndDate = endDate;
    }

    public void setTotalPrice(int price) { mTotalPrice = price; }

    public int getTotalPrice() { return mTotalPrice; }

    public void addBikeToCart(Bike bike) {
        mBikes.add(bike);
    }

    public void addAccessoryToCart(Accessory accessory) {
        mAccessories.add(accessory);
    }

    public void removeAccessoryToCart(Accessory accessory) {
        mAccessories.add(accessory);
    }

    public void addTourToCart(Tour tour) {
        mTours.add(tour);
    }

    public boolean isEmpty() {
        return mBikes.size() == 0 && mTours.size() == 0;
    }

}
