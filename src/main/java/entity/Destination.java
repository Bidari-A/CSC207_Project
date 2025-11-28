package entity;

public class Destination {
    private final String attractionId;
    private final String name;
    private final String address;
    private final String description;
    private final float price;

    public Destination(String attractionId, String name, String address, String description, float price) {
        this.attractionId = attractionId;
        this.name = name;
        this.address = address;
        this.description = description;
        this.price = price;
    }

    public String getAttractionId() {return attractionId;}
    public String getName() {return name;}
    public String getAddress() {return address;}
    public String getDescription() {return description;}
    public float getPrice() {return price;}
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Destination that = (Destination) o;
        return attractionId.equals(that.attractionId);
    }

    @Override
    public int hashCode() {
        return attractionId.hashCode();
    }
    
    @Override
    public String toString() {
        return "Destination{" +
                "attractionId='" + attractionId + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }
}
