package interface_adapter.flight_search;

// What is this about: this state kind of stores what is going to be shown in the View!
// So to update the UI, we just update this :)
public class FlightSearchState {
    // Recall the summary stores the entire JSON string for the best flight
    private String summary ="";
    // Make a getter and setter, as usual for instance variables
    public String getSummary() { return summary;}
    public void setSummary(String summary) { this.summary = summary;}
        // the setter is crucial to modify the summary being stored!
}
