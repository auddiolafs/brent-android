package is.hi.hbv601g.brent.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class Pair implements Parcelable{
    private String mKey;
    private Parcelable mVal;

    public Pair(String key, Parcelable val) {
        mKey = key;
        mVal = val;
    }

    protected Pair(Parcel in) {
        mKey = in.readString();
        mVal = in.readParcelable(null);
    }

    public static final Creator<Pair> CREATOR = new Creator<Pair>() {
        @Override
        public Pair createFromParcel(Parcel in) {
            return new Pair(in);
        }

        @Override
        public Pair[] newArray(int size) {
            return new Pair[size];
        }
    };

    public String getKey() {
        return mKey;
    }

    public Parcelable getVal() {
        return mVal;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mKey);
        dest.writeParcelable(mVal, 0);
    }
}