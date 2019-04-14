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
    private int mPrice;
    private Date mDate;
    private String mImage;
    private Long mDuration;
    private String mDescription;
    private Long mNumberOfTravelers;
    private String mDepartureTime;

    public Tour() { }

    public Tour(String id, String name, String location, int price, Date date, String image,
                Long duration, String description, Long travelers, String departureTime) {
        mID = id;
        mName = name;
        mLocation = location;
        mPrice = price;
        mDate = date;
        mImage = image;
        mDuration = duration;
        mDescription = description;
        mNumberOfTravelers = travelers;
        mDepartureTime = departureTime;
    }

    protected Tour(Parcel in) {
        mID = in.readString();
        mName = in.readString();
        mLocation = in.readString();
        mImage = in.readString();
        mDuration = in.readLong();
        mDescription = in.readString();
        mDepartureTime = in.readString();
        //mDate = new Date(in.readString());
        //mStartDate = new Date(in.readString());
        //mEndDate = new Date(in.readString());
        mPrice = in.readInt();
        //mNumberOfTravelers = in.readLong();
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
            t.setPrice(Integer.parseInt(tourData.get("price").toString()));
            t.setImage(tourData.get("image").toString());
            t.setDuration(Long.parseLong(tourData.get("durationHours").toString()));
            t.setDescription(tourData.get("description").toString());
            t.setDepartureTime(tourData.get("departure").toString());
            //t.setStartDate(new Date((long)tourData.get("startDate")));
            //t.setEndDate(new Date((long)tourData.get("endDate")));
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

    public int getPrice() {
        return mPrice;
    }

    public void setPrice(int price) {
        mPrice = price;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getImage() { return mImage; }

    public void setImage(String image) { this.mImage = image; }

    public Long getDuration() { return mDuration; }

    public void setDuration(Long duration) { this.mDuration = duration; }

    public String getDescription() { return mDescription; }

    public void setDescription(String description) { this.mDescription = description; }

    public Long getNumberOfTravelers() { return mNumberOfTravelers; }

    public void setNumberOfTravelers(Long travelers) { this.mNumberOfTravelers = travelers; }

    public String getDepartureTime() { return mDepartureTime; }

    public void setDepartureTime(String departureTime) { this.mDepartureTime = departureTime; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mID);
        dest.writeString(mName);
        dest.writeString(mLocation);
        dest.writeString(mImage);
        dest.writeLong(mDuration);
        dest.writeString(mDescription);
        dest.writeString(mDepartureTime);
        /*if (mDate != null) {
            dest.writeString(mDate.toString());
        }*/
        dest.writeInt(mPrice);
        //dest.writeLong(mNumberOfTravelers);
    }


}
