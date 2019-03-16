package is.hi.hbv601g.brent.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.Map;

public class Route implements Parcelable {
    private String id;
    private String location;
    private String length;
    private String description;
    private String likes;

    public Route() { }

    public Route(String id, String location, String length, String description, String likes) {
        this.id = id;
        this.location = location;
        this.length = length;
        this.description = description;
        this.likes = likes;
    }

    protected Route(Parcel in) {
        id = in.readString();
        location = in.readString();
        length = in.readString();
        description = in.readString();
        likes = in.readString();
        if (in.readByte() == 0) {
            length = null;
        } else {
            length = in.readString();
        }
    }

    public static Route toEntity(String routeId, Map<String, Object> routeData) {
        Route r = new Route();
        Log.d("Route.java", routeData.get("Location").toString());
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
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getLength() {
        return length;
    }

    public String getDescription() { return description; };

    public void setDescription(String description) { this.description = description; }

    public String getLikes() { return likes; }

    public void setLikes(String likes) { this.likes = likes; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(location);
        dest.writeString(length);
        dest.writeString(description);
        dest.writeString(likes);
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeString(length);
        }
    }
}
