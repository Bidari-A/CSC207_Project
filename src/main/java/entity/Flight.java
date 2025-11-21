package entity;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Flight {
    private final String airlineName;
    private final Date departureTimes;
    private final float price;

    public Flight(String airlineName, Date departureTimes, float price) {
        this.airlineName = airlineName;
        this.departureTimes = departureTimes;
        this.price = price;
    }

    // Implemented Methods
    public String getAirlineName() {return airlineName;}
    public Date getDepartureTimes() {return departureTimes;}
    public float getPrice() {return price;}
    @Override
    public String toString() {
        return "Flight [airline=" + airlineName + ", departure=" + departureTimes + ", price=" + price + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;                      // same reference
        if (o == null || getClass() != o.getClass()) return false;  // null or different class

        Flight flight = (Flight) o;                      // cast to Flight
        return Float.compare(flight.price, price) == 0 &&
                Objects.equals(airlineName, flight.airlineName) &&
                Objects.equals(departureTimes, flight.departureTimes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(airlineName, departureTimes, price);
    }
}
