package is.hi.hbv601g.brent.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

public class Bike implements Parcelable {

    private String mID;
    private String mBrand;
    private String mName;
    private String mSize;
    private String mSerial;
    private Long mPrice;
    private String mType;

    public Bike() { }

    public Bike(String brand, String name, String size, String serial, Long price, String type,
                String id) {
        mBrand = brand;
        mName = name;
        mSize = size;
        mSerial = serial;
        mPrice = price;
        mType = type;
        mID = id;
    }


    protected Bike(Parcel in) {
        mID = in.readString();
        mBrand = in.readString();
        mName = in.readString();
        mSize = in.readString();
        mSerial = in.readString();
        mPrice = in.readLong();
        if (in.readByte() == 0) {
            mPrice = null;
        } else {
            mPrice = in.readLong();
        }
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
            b.setPrice(Long.parseLong( bikeData.get("ppd").toString()));
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

    public String getSerial() {
        return mSerial;
    }

    public void setSerial(String serial) {
        mSerial = serial;
    }

    public Long getPrice() {
        return mPrice;
    }

    public void setPrice(Long price) {
        mPrice = price;
    }

    public String getType() { return mType; }

    public void setType(String type) { mType = type; }

    public boolean isEmpty() {
        if (mID == null) {
            return true;
        }
        return false;
    }

    public boolean equals(Bike bike) {
        if (bike.getSerial() == mSerial) {
            return true;
        }
        return false;
    }

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
        dest.writeLong(mPrice);
        if (mPrice == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(mPrice);
        }
    }
}
