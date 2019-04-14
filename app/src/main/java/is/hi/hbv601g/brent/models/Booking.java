package is.hi.hbv601g.brent.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class Booking implements Parcelable {

    private String mId;
    private String mUserID;
    private List<Tour> mTours;
    private Date mStartDate;
    private Date mEndDate;
    private String mPickupLocation;
    private int mPrice;

    public Booking() {}

    protected Booking(Parcel in) {
        mId = in.readString();
        mUserID= in.readString();
        mEndDate = new Date(in.readString());
        mStartDate = new Date(in.readString());;
        mPrice = in.readInt();
        mPickupLocation = in.readString();
    }

    public static Booking toEntity(String bookingId, Map<String, Object> bookingData) {
        Booking r = new Booking();
        try {
            r.setId(bookingId);
            r.setUserId(bookingData.get("userId").toString());
            r.setPickupLocation(bookingData.get("pickupLocation").toString());
            r.setEndDate(((Timestamp)bookingData.get("endDate")).toDate());
            r.setStartDate(((Timestamp)bookingData.get("startDate")).toDate());
            r.setPrice(35500);
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

    public String getId() { return mId; }

    public void setId(String id) { mId = id; }

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

    public void setPickupLocation(String pickupLocation) {
        mPickupLocation = pickupLocation;
    }

    public void setUserId(String userId) {
        mUserID= userId;
    }

    public void setPrice(int price) {
        mPrice = price;
    }

    public int getPrice() {
        return mPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mUserID);
        dest.writeString(mEndDate.toString());
        dest.writeString(mStartDate.toString());
        dest.writeInt(mPrice);
        dest.writeString(mPickupLocation);
    }

}
