package entity;

public class Accommodation {
    private final String name;
    private final String address;
    private final float price;

    public Accommodation(String name, String address, float price) {
        this.name = name;
        this.address = address;
        this.price = price;
    }

    // Constructor for JSON data (name only)
    public Accommodation(String name) {
        this(name, "", 0.0f);
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
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
