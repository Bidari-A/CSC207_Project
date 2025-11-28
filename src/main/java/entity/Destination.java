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

    public String getName() {return name;}
    public String getAddress() {return address;}
    public String getDescription() {return description;}
    public float getPrice() {return price;}
    @Override
    public String toString() {
        return "Destination{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }
}
