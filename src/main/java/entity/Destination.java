package entity;

public class Destination {
    private final String name;

    // Legacy fields for old tests
    private final String address;
    private final String description;
    private final float price;

    // New minimal constructor
    public Destination(String name) {
        this.name = name;
        this.address = null;
        this.description = null;
        this.price = 0;
    }

    // Legacy constructor for teammate’s old tests
    public Destination(String name, String address, String description, float price) {
        this.name = name;
        this.address = address;
        this.description = description;
        this.price = price;
    }

    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getDescription() { return description; }
    public float getPrice() { return price; }

    @Override
    public String toString() {
        return "Destination{name='" + name + "'}";
    }
}
