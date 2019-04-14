package is.hi.hbv601g.brent.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class Triplet implements Parcelable {
    String mProductName;
    int mQuantity;
    int mPrice;

    public Triplet(String productName, int quantity, int price) {
        mProductName = productName;
        mQuantity = quantity;
        mPrice = price;
    }

    protected Triplet(Parcel in) {
        mProductName = in.readString();
        mQuantity = in.readInt();
        mPrice = in.readInt();
    }

    public String getPrice() {
        return "" + mPrice;
    }

    public void setPrice(int price) {
        mPrice = price;
    }

    public final Creator<Triplet> CREATOR = new Creator<Triplet>() {
        @Override
        public Triplet createFromParcel(Parcel in) {
            return new Triplet(in);
        }

        @Override
        public Triplet[] newArray(int size) {
            return new Triplet[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mProductName);
        dest.writeInt(mQuantity);
        dest.writeInt(mPrice);
    }

    public String getProduct() {
        return mProductName;
    }

    public String getQuantity() {
        return "" + mQuantity;
    }
}