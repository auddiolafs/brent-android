package is.hi.hbv601g.brent;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Booking {

    private List<Bike> mBikes;
    private List<Accessory> mAccessories;
    private List<Tour> mTours;
    private Date mStartDate;
    private Date mEndDate;
    private String mPickupLocation;

    public Booking() {}

    public Booking(Date startDate, Date endDate) {
        mStartDate = startDate;
        mEndDate = endDate;
        mBikes = new ArrayList<>();
        mTours = new ArrayList<>();
        mPickupLocation = "";
    }


    public boolean isEmpty() {
        if (this.mStartDate == null) {
            return true;
        }
        return false;
    }

    public List<Bike> getBikes() {
        return mBikes;
    }

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

    public String getPickupLocation() {
        return mPickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.mPickupLocation = pickupLocation;
    }

    public Booking(List<Bike> bikes, List<Accessory> accessories, List<Tour> tours,
                   User user, Date startDate, Date endDate, String pickupLocation) {
        this.mBikes = bikes;
        this.mAccessories = accessories;
        this.mTours = tours;
        this.mStartDate = startDate;
        this.mEndDate = endDate;
        this.mPickupLocation = pickupLocation;
    }

    public void addBike(Bike bike) {
        mBikes.add(bike);
    }

}
