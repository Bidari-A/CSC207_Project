package entity;

import java.util.Objects;

public class Accommodation {
    private final String name;
    private final String address;
    private final float price;

    public Accommodation(String name, String address, float price) {
        this.name = name;
        this.address = address;
        this.price = price;
    }

    // Implemented Methods
    public String getName() { return name; }

    public String getAddress() { return address; }

    public float getPrice() { return price; }

    @Override
    public String toString() {
        return "Accommodation{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Accommodation)) return false;
        Accommodation that = (Accommodation) o;
        return Float.compare(that.price, price) == 0 &&
                Objects.equals(name, that.name) &&
                Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address, price);
    }
}
