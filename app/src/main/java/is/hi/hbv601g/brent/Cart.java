package is.hi.hbv601g.brent;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import is.hi.hbv601g.brent.models.Accessory;
import is.hi.hbv601g.brent.models.Bike;
import is.hi.hbv601g.brent.models.Booking;
import is.hi.hbv601g.brent.models.Tour;

public class Cart {

    private List<Bike> mBikes = new ArrayList<>();
    private List<Accessory> mAccessories = new ArrayList<>();
    private List<Tour> mTours = new ArrayList<>();
    private Date mStartDate;
    private Date mEndDate;
    private Long mTotalPrice = new Long(0);

    private static Cart sCart = new Cart();
    private static Long mCartID = new Long(1);
    private static final String TAG = "CartActivity";



    private static final FirebaseFirestore mDB = FirebaseFirestore.getInstance();
    private Map<Long, Booking> mBookings;

    private Cart() {
        mBookings = new HashMap();
    }

    public static Cart getCart() {
        return sCart;
    }

    public boolean contains(Date startDate, Date endDate) {
        Iterator it = mBookings.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String key = pair.getKey().toString();
            Booking booking = (Booking) pair.getValue();
            if (booking.getStartDate() == startDate && booking.getEndDate() == endDate) return true;
        }
        return false;
    }

    public void resetCart() {
        sCart = new Cart();
    }

    public List<Bike> getBikes() { return mBikes; }

    public void setBikes(List<Bike> bikes) {
        this.mBikes = bikes;
    }

    public List<Accessory> getAccessories() {
        return mAccessories;
    }

    public void setAccessories(List<Accessory> accessories) {
        this.mAccessories = accessories;
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

    public void setTotalPrice(Long price) { mTotalPrice = price; }

    public Long getTotalPrice() { return mTotalPrice; }

    public void addBikeToCart(Bike bike) {
        mBikes.add(bike);
    }

    public void addAccessoryToCart(Accessory accessory) {
        mAccessories.add(accessory);
    }

    public void addTourToCart(Tour tour) {
        mTours.add(tour);
    }
}
