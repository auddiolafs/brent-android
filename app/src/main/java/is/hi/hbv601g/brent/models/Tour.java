package is.hi.hbv601g.brent.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.Date;
import java.util.Map;

public class Tour implements Parcelable {

    private String mID;
    private String mName;
    private String mLocation;
    private Long mPrice;
    private Date mStartDate;
    private Date mEndDate;

    public Tour() { }

    public Tour(String id, String name, String location, Long price, Date startDate, Date endDate) {
        mID = id;
        mName = name;
        mLocation = location;
        mPrice = price;
        mStartDate = startDate;
        mEndDate = endDate;
    }

    protected Tour(Parcel in) {
        mID = in.readString();
        mName = in.readString();
        mLocation = in.readString();
        mStartDate = new Date(in.readString());
        mEndDate = new Date(in.readString());
        if (in.readByte() == 0) {
            mPrice = null;
        } else {
            mPrice = in.readLong();
        }
    }

    public static final Creator<Tour> CREATOR = new Creator<Tour>() {
        @Override
        public Tour createFromParcel(Parcel in) {
            return new Tour(in);
        }

        @Override
        public Tour[] newArray(int size) {
            return new Tour[size];
        }
    };

    public static Tour toEntity(String tourId, Map<String, Object> tourData) {
        Tour t = new Tour();
        try {
            t.setId(tourId);
            t.setName(tourData.get("name").toString());
            t.setLocation(tourData.get("location").toString());
            t.setPrice(Long.parseLong(tourData.get("price").toString()));
            // TODO: set dates
            return t;
        } catch (Exception e) {
            return null;
        }
    }

    public String getId() {
        return mID;
    }

    public void setId(String id) {
        mID = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }

    public Long getPrice() {
        return mPrice;
    }

    public void setPrice(Long price) {
        mPrice = price;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mID);
        dest.writeString(mName);
        dest.writeString(mPrice.toString());
        dest.writeString(mLocation);
        if (mStartDate != null && mEndDate != null) {
            dest.writeString(mStartDate.toString());
            dest.writeString(mEndDate.toString());
        }
        if (mPrice == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeString(mPrice.toString());
        }
    }
}
