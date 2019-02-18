package is.hi.hbv601g.brent;

public class Accessory {

    private Long id;
    private String type;
    private String name;
    private Long price;

    public Accessory(String type, String name, Long price) {
        this.type = type;
        this.name = name;
        this.price = price;
        this.id = new Long(1);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public boolean isEmpty() {
        if (this.id == null) {
            return true;
        }
        return false;
    }

}
