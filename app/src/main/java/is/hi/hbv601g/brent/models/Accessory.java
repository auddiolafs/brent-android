package is.hi.hbv601g.brent.models;

public class Accessory {

    private String  id;
    private String type;
    private String name;
    private Long price;

    public Accessory(String type, String name, Long price, String id) {
        this.type = type;
        this.name = name;
        this.price = price;
        this.id = id;
    }

    public String getId() { return id; }

    public void setId(String id) {
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
