package is.hi.hbv601g.brent;

import java.util.Date;
import java.util.List;

public class Cart {

    private Long id;
    private List<Bike> bikes;
    private List<Accessory> accessories;
    private List<Tour> tours;
    private User user;
    private Date startDate;
    private Date endDate;

    public Cart(List<Bike> bikes, List<Accessory> accessories, List<Tour> tours, User user, Date startDate, Date endDate) {
        this.bikes = bikes;
        this.accessories = accessories;
        this.tours = tours;
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
        this.id = new Long(1);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isEmpty() {
        if (this.id == null) {
            return true;
        }
        return false;
    }

}
