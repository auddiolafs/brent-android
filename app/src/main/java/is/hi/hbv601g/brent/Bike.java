package is.hi.hbv601g.brent;

public class Bike {

    private Long id;
    private String brand;
    private String name;
    private String size;
    private String serial;
    private Long price;

    public Bike(String brand, String name, String size, String serial, Long price) {
        this.brand = brand;
        this.name = name;
        this.size = size;
        this.serial = serial;
        this.price = price;
        this.id = new Long(1);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
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
