package is.hi.hbv601g.brent.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

public class Bike implements Parcelable {

    private String mID;
    private String mBrand;
    private String mName;
    private String mSize;
    private String mType;
    private String mSerial;
    private int mPrice;

    public Bike() { }


    protected Bike(Parcel in) {
        mID = in.readString();
        mBrand = in.readString();
        mName = in.readString();
        mSize = in.readString();
        mType = in.readString();
        mSerial = in.readString();
        mPrice = in.readInt();
    }

    public static final Creator<Bike> CREATOR = new Creator<Bike>() {
        @Override
        public Bike createFromParcel(Parcel in) {
            return new Bike(in);
        }

        @Override
        public Bike[] newArray(int size) {
            return new Bike[size];
        }
    };

    public static Bike toEntity(String bikeId, Map<String, Object> bikeData) {
        Bike b = new Bike();
        try {
            b.setId(bikeId);
            b.setBrand(bikeData.get("brand").toString());
            b.setName(bikeData.get("name").toString());
            b.setSize(bikeData.get("size").toString());
            b.setType(bikeData.get("type").toString());
            b.setSerial(bikeData.get("serial").toString());
            b.setPrice(Integer.parseInt(bikeData.get("ppd").toString()));
            return b;
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

    public String getBrand() {
        return mBrand;
    }

    public void setBrand(String brand) {
        mBrand = brand;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getSize() {
        return mSize;
    }

    public void setSize(String size) {
        mSize = size;
    }

    public void setSerial(String serial) {
        mSerial = serial;
    }

    public int getPrice() {
        return mPrice;
    }

    public void setPrice(int price) {
        mPrice = price;
    }

    public String getType() { return mType; }

    public void setType(String type) { mType = type; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mID);
        dest.writeString(mBrand);
        dest.writeString(mName);
        dest.writeString(mSize);
        dest.writeString(mType);
        dest.writeString(mSerial);
        dest.writeInt(mPrice);
    }
}
