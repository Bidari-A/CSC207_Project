package entity;

import java.util.Objects;

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
    // Implemented Methods
    public String getName() { return name; }

    public String getAddress() { return address; }

    public String getDescription() { return description; }

    public float getPrice() { return price; }

    @Override
    public String toString() {
        return "Destination{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Destination)) return false;
        Destination that = (Destination) o;
        return Float.compare(that.price, price) == 0 &&
                Objects.equals(name, that.name) &&
                Objects.equals(address, that.address) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address, description, price);
    }
}
