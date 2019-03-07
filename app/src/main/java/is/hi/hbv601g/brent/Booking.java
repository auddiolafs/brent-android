package is.hi.hbv601g.brent;

import java.util.Date;
import java.util.List;

public class Booking {

    private String id;
    private List<Bike> bikes;
    private List<Accessory> accessories;
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
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Bike> getBikes() {
        return bikes;
    }

    public void setBikes(List<Bike> bikes) {
        this.bikes = bikes;
    }

    public List<Accessory> getAccessories() {
        return accessories;
    }

    public void setAccessories(List<Accessory> accessories) {
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

    public Booking() { }

    public Booking(String id, List<Bike> bikes, List<Accessory> accessories, List<Tour> tours,
                   User user, Date startEnd, Date endDate, String pickupLocation) {
        this.id = id;
        this.bikes = bikes;
        this.accessories = accessories;
        this.tours = tours;
        this.user = user;
        this.startEnd = startEnd;
        this.endDate = endDate;
        this.pickupLocation = pickupLocation;
    }
}
