package entity;

import java.util.concurrent.atomic.AtomicInteger;

public class FlightIdGenerator {
    private static final AtomicInteger counter = new AtomicInteger(1);

    /**
     * Generates the next flight ID in the format F#### (e.g., F0001, F0002).
     * @return the next flight ID
     */
    public static synchronized String nextId() {
        return "F" + String.format("%04d", counter.getAndIncrement());
    }

    /**
     * Initializes the counter based on existing flight IDs to avoid conflicts.
     * @param existingFlightIds list of existing flight IDs
     */
    public static synchronized void initializeFromExistingIds(java.util.Collection<String> existingFlightIds) {
        int maxId = 0;
        for (String flightId : existingFlightIds) {
            if (flightId != null && flightId.startsWith("F")) {
                try {
                    String numericPart = flightId.substring(1);
                    int idValue = Integer.parseInt(numericPart);
                    if (idValue > maxId) {
                        maxId = idValue;
                    }
                } catch (NumberFormatException e) {
                    // Skip invalid flight IDs
                }
            }
        }
        counter.set(maxId + 1);
    }
}

