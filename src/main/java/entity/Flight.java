package entity;


public class Flight {
    private final String airlineName;
    private final String departureTimes;
    private final float price;

    public Flight(String airlineName, String departureTimes, float price) {
        this.airlineName = airlineName;
        this.departureTimes = departureTimes;
        this.price = price;
    }


    public String getAirlineName() {return airlineName;}
    public String getDepartureTimes() {return departureTimes;}
    public float getPrice() {return price;}
    @Override
    public String toString() {
        return "Flight{" +
                "airlineName='" + airlineName + '\'' +
                ", departureTimes='" + departureTimes + '\'' +
                ", price=" + price +
                '}';
    }
}