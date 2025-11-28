package entity;


public class Flight {
    private final String flightId;
    private final String airlineName;
    private final String departureTimes;
    private final float price;

    public Flight(String flightId, String airlineName, String departureTimes, float price) {
        this.flightId = flightId;
        this.airlineName = airlineName;
        this.departureTimes = departureTimes;
        this.price = price;
    }

    public String getFlightId() {return flightId;}
    public String getAirlineName() {return airlineName;}
    public String getDepartureTimes() {return departureTimes;}
    public float getPrice() {return price;}
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flight flight = (Flight) o;
        return flightId.equals(flight.flightId);
    }

    @Override
    public int hashCode() {
        return flightId.hashCode();
    }
    
    @Override
    public String toString() {
        return "Flight{" +
                "flightId='" + flightId + '\'' +
                ", airlineName='" + airlineName + '\'' +
                ", departureTimes='" + departureTimes + '\'' +
                ", price=" + price +
                '}';
    }
}