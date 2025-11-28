package entity;

import java.util.concurrent.atomic.AtomicInteger;

public class TripIdGenerator {
    private static final AtomicInteger counter = new AtomicInteger(1);

    /**
     * Generates the next trip ID in the format T#### (e.g., T0001, T0002).
     * @return the next trip ID
     */
    public static synchronized String nextId() {
        return "T" + String.format("%04d", counter.getAndIncrement());
    }

    /**
     * Initializes the counter based on existing trip IDs to avoid conflicts.
     * Parses trip IDs in the format T#### or T### and sets the counter to the max value + 1.
     * @param existingTripIds list of existing trip IDs
     */
    public static synchronized void initializeFromExistingIds(java.util.Collection<String> existingTripIds) {
        int maxId = 0;
        for (String tripId : existingTripIds) {
            if (tripId != null && tripId.startsWith("T")) {
                try {
                    // Extract numeric part (handles both T000 and T0001 formats)
                    String numericPart = tripId.substring(1);
                    int idValue = Integer.parseInt(numericPart);
                    if (idValue > maxId) {
                        maxId = idValue;
                    }
                } catch (NumberFormatException e) {
                    // Skip invalid trip IDs
                }
            }
        }
        // Set counter to maxId + 1 so next ID will be unique
        counter.set(maxId + 1);
    }
}
