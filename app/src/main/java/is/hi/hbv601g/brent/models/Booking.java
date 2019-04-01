package is.hi.hbv601g.brent.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Booking implements Parcelable {

    private String mId;
    private String userId;
    private List<Bike> mBikes;
    private List<Accessory> mAccessories;
    private List<Tour> mTours;
    private Date mStartDate;
    private Date mEndDate;
    private String mPickupLocation;

    public Booking() {}

    public Booking(Date startDate, Date endDate, String pickupLocation, List<Bike> bikes, List<Accessory> accessories, List<Tour> tours,
                   User user, String userId) {
        this.mBikes = bikes;
        this.mAccessories = accessories;
        this.mTours = tours;
        this.mStartDate = startDate;
        this.mEndDate = endDate;
        this.mPickupLocation = pickupLocation;
        this.userId = userId;
    }

    public Booking(Date startDate, Date endDate) {
        mStartDate = startDate;
        mEndDate = endDate;
        mBikes = new ArrayList<>();
        mTours = new ArrayList<>();
        mPickupLocation = "";
    }

    protected Booking(Parcel in) {
        mId = in.readString();
        userId = in.readString();
        if (in.readByte() == 0) {
            mId = null;
        } else {
            mId = in.readString();
        }
    }

    public static Booking toEntity(String bookingId, Map<String, Object> boookingData) {
        Booking r = new Booking();
        try {
            r.setId(bookingId);
            r.setUserId(boookingData.get("user").toString());
            return r;
        } catch (Exception e) {
            return null;
        }
    }

    public static final Parcelable.Creator<Booking> CREATOR = new Parcelable.Creator<Booking>() {
        @Override
        public Booking createFromParcel(Parcel in) {
            return new Booking(in);
        }

        @Override
        public Booking[] newArray(int size) {
            return new Booking[size];
        }
    };


    public boolean isEmpty() {
        if (mStartDate == null) {
            return true;
        }
        return false;
    }

    public String getId() { return mId; }

    public void setId(String id) { this.mId = id; }

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

    public String getPickupLocation() {
        return mPickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.mPickupLocation = pickupLocation;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void addBike(Bike bike) {
        mBikes.add(bike);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(userId);
        if (mId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeString(mId);
        }
    }

}
