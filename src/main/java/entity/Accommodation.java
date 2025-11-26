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


    public String getName() {return name;}
    public String getAddress() {return address;}
    public float getPrice() {return price;}
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Accommodation that = (Accommodation) o;
        return Float.compare(that.price, price) == 0 &&
                name.equals(that.name) &&
                address.equals(that.address);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + address.hashCode();
        result = 31 * result + Float.hashCode(price);
        return result;
    }

    @Override
    public String toString() {
        return "Accommodation{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", price=" + price +
                '}';
    }
}
