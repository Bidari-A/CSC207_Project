package entity;

import java.util.Date;
import java.util.List;

public class Flight {
    private final String airlineName;
    private final Date departureTimes;
    private final float price;

    public Flight(String airlineName, Date departureTimes, float price) {
        this.airlineName = airlineName;
        this.departureTimes = departureTimes;
        this.price = price;
    }

    // TODO: Implement Methods
}
