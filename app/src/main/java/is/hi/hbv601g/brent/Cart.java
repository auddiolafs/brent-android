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

    public Cart() {

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

    public List<Accessory> getAccessories() {
        return accessories;
    }

    public List<Tour> getTours() {
        return tours;
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

}
