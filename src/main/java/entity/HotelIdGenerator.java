package entity;

import java.util.concurrent.atomic.AtomicInteger;

public class HotelIdGenerator {
    private static final AtomicInteger counter = new AtomicInteger(1);

    /**
     * Generates the next hotel ID in the format H#### (e.g., H0001, H0002).
     * @return the next hotel ID
     */
    public static synchronized String nextId() {
        return "H" + String.format("%04d", counter.getAndIncrement());
    }

    /**
     * Initializes the counter based on existing hotel IDs to avoid conflicts.
     * @param existingHotelIds list of existing hotel IDs
     */
    public static synchronized void initializeFromExistingIds(java.util.Collection<String> existingHotelIds) {
        int maxId = 0;
        for (String hotelId : existingHotelIds) {
            if (hotelId != null && hotelId.startsWith("H")) {
                try {
                    String numericPart = hotelId.substring(1);
                    int idValue = Integer.parseInt(numericPart);
                    if (idValue > maxId) {
                        maxId = idValue;
                    }
                } catch (NumberFormatException e) {
                    // Skip invalid hotel IDs
                }
            }
        }
        counter.set(maxId + 1);
    }
}

