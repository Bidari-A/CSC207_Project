package entity;

public class Destination {
    private final String name;
    private final String address;
    private final String description;
    private final float price;

    public Destination(String name, String address, String description, float price) {
        this.name = name;
        this.address = address;
        this.description = description;
        this.price = price;
    }

    // Constructor for JSON data (name only)
    public Destination(String name) {
        this(name, "", "", 0.0f);
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public float getPrice() {
        return price;
    }

    @Override
    public String toString() {
        if (address != null && !address.isEmpty()) {
            return String.format("%s - %s", name, address);
        }
        return name;
    }
}
