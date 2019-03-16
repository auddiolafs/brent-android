package is.hi.hbv601g.brent.models;

public class Route {
    private Long mID;
    private String mLocation;

    public Route(String location) {
        mLocation = location;
    }

    public Long getId() {
        return mID;
    }

    public void setId(Long id) {
        mID = id;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }
}
