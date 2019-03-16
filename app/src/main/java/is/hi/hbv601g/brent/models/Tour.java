package is.hi.hbv601g.brent.models;

import java.util.Date;

public class Tour {

    private String mID;
    private String mName;
    private String mLocation;
    private Long mPrice;
    private Date mStartDate;
    private Date mEndDate;

    public Tour() { }

    public Tour(String id, String name, String location, Long price, Date startDate, Date endDate) {
        mID = id;
        mName = name;
        mLocation = location;
        mPrice = price;
        mStartDate = startDate;
        mEndDate = endDate;
    }

    public String getId() {
        return mID;
    }

    public void setId(String id) {
        mID = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }

    public Long getPrice() {
        return mPrice;
    }

    public void setPrice(Long price) {
        mPrice = price;
    }

    public Date getStartDate() {
        return mStartDate;
    }

    public void setStartDate(Date startDate) {
        mStartDate = startDate;
    }

    public Date getEndDate() {
        return mEndDate;
    }

    public void setEndDate(Date endDate) {
        mEndDate = endDate;
    }
}
