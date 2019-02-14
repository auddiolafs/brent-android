package is.hi.hbv601g.brent;

import java.util.Date;
import java.util.List;

public class Booking {

    private Long id;
    private List<Bikes> bikes;
    private List<Accessories> accessories;
    private List<Tour> tours;
    private User user;
    private Date startEnd;
    private Date endDate;
    private String pickupLocation;

    public boolean isEmpty() {
        if (this.id == null) {
            return true;
        }
        return false;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Bikes> getBikes() {
        return bikes;
    }

    public void setBikes(List<Bikes> bikes) {
        this.bikes = bikes;
    }

    public List<Accessories> getAccessories() {
        return accessories;
    }

    public void setAccessories(List<Accessories> accessories) {
        this.accessories = accessories;
    }

    public List<Tour> getTours() {
        return tours;
    }

    public void setTours(List<Tour> tours) {
        this.tours = tours;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getStartEnd() {
        return startEnd;
    }

    public void setStartEnd(Date startEnd) {
        this.startEnd = startEnd;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public Booking(List<Bikes> bikes, List<Accessories> accessories, List<Tour> tours,
                   User user, Date startEnd, Date endDate, String pickupLocation) {
        this.bikes = bikes;
        this.accessories = accessories;
        this.tours = tours;
        this.user = user;
        this.startEnd = startEnd;
        this.endDate = endDate;
        this.pickupLocation = pickupLocation;
    }
}
