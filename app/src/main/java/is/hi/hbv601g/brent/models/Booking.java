package is.hi.hbv601g.brent.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        if (mStartDate == null) {
            return true;
        }
        return false;
    }

    public List<Bike> getBikes() {
        return mBikes;
    }

    public void setBikes(List<Bike> bikes) {
        mBikes = bikes;
    }

    public List<Accessory> getAccessories() {
        return mAccessories;
    }

    public void setAccessories(List<Accessory> accessories) {
        mAccessories = accessories;
    }

    public List<Tour> getTours() {
        return mTours;
    }

    public void setTours(List<Tour> tours) {
        mTours = tours;
    }

    public Date getStartDate() {
        return mStartDate;
    }

    public void setStartDate(Date startDate) {
        mStartDate = startDate;
    }

    public Date getEndDate() {
        return mEndDate;
    }

    public void setEndDate(Date endDate) {
        mEndDate = endDate;
    }

    public String getPickupLocation() {
        return mPickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        mPickupLocation = pickupLocation;
    }

    public Booking(List<Bike> bikes, List<Accessory> accessories, List<Tour> tours,
                   User user, Date startDate, Date endDate, String pickupLocation) {
        mBikes = bikes;
        mAccessories = accessories;
        mTours = tours;
        mStartDate = startDate;
        mEndDate = endDate;
        mPickupLocation = pickupLocation;
    }

    public void addBike(Bike bike) {
        mBikes.add(bike);
    }

}
