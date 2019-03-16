package is.hi.hbv601g.brent.models;

public class Accessory {

    private String mID;
    private String mType;
    private String mName;
    private Long mPrice;

    public Accessory(String type, String name, Long price, String id) {
        mType = type;
        mName = name;
        mPrice = price;
        mID = id;
    }

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

    public Long getPrice() {
        return mPrice;
    }

    public void setPrice(Long price) {
        mPrice = price;
    }

    public boolean isEmpty() {
        if (mID == null) {
            return true;
        }
        return false;
    }

}
