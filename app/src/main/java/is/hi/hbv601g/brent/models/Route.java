package is.hi.hbv601g.brent.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

public class Route implements Parcelable {
    private String mID;
    private String mLocation;
    private String mLength;
    private String mDescription;
    private String mLikes;

    public Route() { }

    public Route(String id, String location, String length, String description, String likes) {
        this.mID = id;
        this.mLocation = location;
        this.mLength = length;
        this.mDescription = description;
        this.mLikes = likes;
    }

    protected Route(Parcel in) {
        mID = in.readString();
        mLocation = in.readString();
        mLength = in.readString();
        mDescription = in.readString();
        mLikes = in.readString();
        if (in.readByte() == 0) {
            mLength = null;
        } else {
            mLength = in.readString();
        }
    }

    public static Route toEntity(String routeId, Map<String, Object> routeData) {
        Route r = new Route();
        try {
            r.setId(routeId);
            r.setLocation(routeData.get("Location").toString());
            r.setLength(routeData.get("Length").toString());
            r.setDescription(routeData.get("Description").toString());
            r.setLikes(routeData.get("Likes").toString());
            return r;
        } catch (Exception e) {
            return null;
        }
    }

    public static final Creator<Route> CREATOR = new Creator<Route>() {
        @Override
        public Route createFromParcel(Parcel in) {
            return new Route(in);
        }

        @Override
        public Route[] newArray(int size) {
            return new Route[size];
        }
    };

    public String getId() {
        return mID;
    }

    public void setId(String id) {
        this.mID = id;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }

    public void setLength(String length) {
        this.mLength = length;
    }

    public String getLength() {
        return mLength;
    }

    public String getDescription() { return mDescription; };

    public void setDescription(String mDescription) { this.mDescription = mDescription; }

    public String getLikes() { return mLikes; }

    public void setLikes(String likes) { this.mLikes = likes; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mID);
        dest.writeString(mLocation);
        dest.writeString(mLength);
        dest.writeString(mDescription);
        dest.writeString(mLikes);
        if (mID == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeString(mLength);
        }
    }
}
