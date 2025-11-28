package entity;

public class Accommodation {
    private final String hotelId;
    private final String name;
    private final String address;
    private final float price;

    public Accommodation(String hotelId, String name, String address, float price) {
        this.hotelId = hotelId;
        this.name = name;
        this.address = address;
        this.price = price;
    }

    public String getHotelId() {return hotelId;}
    public String getName() {return name;}
    public String getAddress() {return address;}
    public float getPrice() {return price;}
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Accommodation that = (Accommodation) o;
        return hotelId.equals(that.hotelId);
    }

    @Override
    public int hashCode() {
        return hotelId.hashCode();
    }

    @Override
    public String toString() {
        return "Accommodation{" +
                "hotelId='" + hotelId + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", price=" + price +
                '}';
    }
}
