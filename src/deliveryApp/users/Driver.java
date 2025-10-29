package deliveryApp.users;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Represents a delivery driver in the system.
 * Each driver can receive up to 10 ratings, and their average rating is used
 * for assigning new orders.
 */
public class Driver extends User {

    private boolean available;
    private final Queue<Integer> ratings;
    private double averageRating;

    public Driver(String name) {
        super(name);
        this.available = true;
        this.ratings = new LinkedList<>();
        this.averageRating = 0.0;
    }

    // --- Availability ---
    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    // --- Ratings ---
    public double getAverageRating() {
        return averageRating;
    }

    /**
     * Adds a new rating (1–5). Only the 10 most recent ratings are stored.
     */
    public void addRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }

        if (ratings.size() == 10) {
            ratings.poll(); // remove oldest
        }
        ratings.offer(rating);
        updateAverage();
    }

    private void updateAverage() {
        if (ratings.isEmpty()) {
            averageRating = 0;
            return;
        }

        double sum = 0;
        for (int r : ratings) {
            sum += r;
        }
        averageRating = sum / ratings.size();
    }

    // --- String Output ---
    @Override
    public String toString() {
        String status = available ? "Available" : "Assigned";
        return String.format("%s | %s | Avg Rating: %.1f", getName(), status, averageRating);
    }
}