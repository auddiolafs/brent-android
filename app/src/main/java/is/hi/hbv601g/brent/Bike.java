package is.hi.hbv601g.brent;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Bike implements Parcelable {

    private String id;
    private String brand;
    private String name;
    private String size;
    private String serial;
    private Long price;

    public Bike() { }

    public Bike(String brand, String name, String size, String serial, Long price, String id) {
        this.brand = brand;
        this.name = name;
        this.size = size;
        this.serial = serial;
        this.price = price;
        this.id = id;
    }


    protected Bike(Parcel in) {
        id = in.readString();
        brand = in.readString();
        name = in.readString();
        size = in.readString();
        serial = in.readString();
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readLong();
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public boolean isEmpty() {
        if (this.id == null) {
            return true;
        }
        return false;
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("brand", this.brand);
            //obj.put("type", "asdf");
            obj.put("size", this.size);
            obj.put("serial", this.serial);
            obj.put("ppd", "12345");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(brand);
        dest.writeString(name);
        dest.writeString(size);
        dest.writeString(serial);
        if (price == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(price);
        }
    }
}
