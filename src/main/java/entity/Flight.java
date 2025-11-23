package entity;

import java.util.Date;

public class Flight {
    private final String airlineName;
    private final Date departureTimes;
    private final float price;
    private final String departure;
    private final String arrival;
    private final String date;

    public Flight(String airlineName, Date departureTimes, float price) {
        this.airlineName = airlineName;
        this.departureTimes = departureTimes;
        this.price = price;
        this.departure = "";
        this.arrival = "";
        this.date = "";
    }

    // Constructor for JSON data
    public Flight(String airlineName, String departure, String arrival, String date) {
        this.airlineName = airlineName;
        this.departure = departure;
        this.arrival = arrival;
        this.date = date;
        this.departureTimes = null;
        this.price = 0.0f;
    }

    public String getAirlineName() {
        return airlineName;
    }

    public Date getDepartureTimes() {
        return departureTimes;
    }

    public float getPrice() {
        return price;
    }

    public String getDeparture() {
        return departure;
    }

    public String getArrival() {
        return arrival;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        if (departure != null && !departure.isEmpty() && arrival != null && !arrival.isEmpty()) {
            return String.format("%s\n%s → %s\nDate: %s", airlineName, departure, arrival, date);
        }
        return airlineName;
    }
}
