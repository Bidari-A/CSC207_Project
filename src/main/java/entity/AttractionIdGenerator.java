package entity;

import java.util.concurrent.atomic.AtomicInteger;

public class AttractionIdGenerator {
    private static final AtomicInteger counter = new AtomicInteger(1);

    /**
     * Generates the next attraction ID in the format A#### (e.g., A0001, A0002).
     * @return the next attraction ID
     */
    public static synchronized String nextId() {
        return "A" + String.format("%04d", counter.getAndIncrement());
    }

    /**
     * Initializes the counter based on existing attraction IDs to avoid conflicts.
     * @param existingAttractionIds list of existing attraction IDs
     */
    public static synchronized void initializeFromExistingIds(java.util.Collection<String> existingAttractionIds) {
        int maxId = 0;
        for (String attractionId : existingAttractionIds) {
            if (attractionId != null && attractionId.startsWith("A")) {
                try {
                    String numericPart = attractionId.substring(1);
                    int idValue = Integer.parseInt(numericPart);
                    if (idValue > maxId) {
                        maxId = idValue;
                    }
                } catch (NumberFormatException e) {
                    // Skip invalid attraction IDs
                }
            }
        }
        counter.set(maxId + 1);
    }
}

