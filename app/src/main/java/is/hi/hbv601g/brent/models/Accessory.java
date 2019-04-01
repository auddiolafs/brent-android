package is.hi.hbv601g.brent.models;

import java.util.Map;

import is.hi.hbv601g.brent.activities.AccessoriesActivity;

public class Accessory {

    private String mID;
    private String mType;
    private String mName;
    private Long mPrice;

    public Accessory() { }

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

    public static Accessory toEntity(String id, Map<String, Object> data) {
        Accessory a = new Accessory();
        try {
            a.setId(id);
            a.setName(data.get("name").toString());
            a.setType(data.get("location").toString());
            a.setPrice(Long.parseLong(data.get("price").toString()));
            return a;
        } catch (Exception e) {
            return null;
        }
    }
}
