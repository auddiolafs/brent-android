package is.hi.hbv601g.brent.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;


public class Accessory implements Parcelable {

    private String mID;
    private String mType;
    private String mName;
    private int mPrice;
    private String mImage;

    public Accessory() { }


    protected Accessory(Parcel in) {
        mID = in.readString();
        mType = in.readString();
        mName = in.readString();
        mPrice = in.readInt();
    }

    public static final Creator<Accessory> CREATOR = new Creator<Accessory>() {
        @Override
        public Accessory createFromParcel(Parcel in) {
            return new Accessory(in);
        }

        @Override
        public Accessory[] newArray(int size) {
            return new Accessory[size];
        }
    };

    public String getId() { return mID; }

    public void setId(String id) {
        mID = id;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getPrice() {
        return mPrice;
    }

    public void setPrice(int price) {
        mPrice = price;
    }

    public String getImage() { return mImage; }

    public static Accessory toEntity(String id, Map<String, Object> data) {
        Accessory a = new Accessory();
        try {
            a.setId(id);
            a.setName(data.get("name").toString());
            a.setType(data.get("type").toString());
            a.setPrice(Integer.parseInt(data.get("price").toString()));
            return a;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mID);
        dest.writeString(mType);
        dest.writeString(mName);
        dest.writeInt(mPrice);
    }
}
