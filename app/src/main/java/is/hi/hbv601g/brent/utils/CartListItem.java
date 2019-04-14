package is.hi.hbv601g.brent.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class CartListItem implements Parcelable {
    String mProductName;
    int mQuantity;
    int mPrice;

    public CartListItem(String productName, int quantity, int price) {
        mProductName = productName;
        mQuantity = quantity;
        mPrice = price;
    }

    protected CartListItem(Parcel in) {
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

    public final Creator<CartListItem> CREATOR = new Creator<CartListItem>() {
        @Override
        public CartListItem createFromParcel(Parcel in) {
            return new CartListItem(in);
        }

        @Override
        public CartListItem[] newArray(int size) {
            return new CartListItem[size];
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