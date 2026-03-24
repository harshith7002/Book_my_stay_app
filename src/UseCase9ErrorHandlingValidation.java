import java.util.*;

/**
 * UseCase9ErrorHandlingValidation
 * <p>
 * Demonstrates validation, custom exceptions, and safe error handling.
 *
 * @author YourName
 * @version 9.1
 */

// -------- Custom Exception --------
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// -------- Reservation --------
record UC9Reservation(String guestName, String roomType) {
}

// -------- Inventory --------
class UC9RoomInventory {
    private final Map<String, Integer> inventory = new HashMap<>();

    public UC9RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
    }

    public boolean isValidRoomType(String roomType) {
        return inventory.containsKey(roomType);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrement(String roomType) throws InvalidBookingException {
        int current = inventory.get(roomType);

        if (current <= 0) {
            throw new InvalidBookingException("No rooms available for: " + roomType);
        }

        inventory.put(roomType, current - 1);
    }
}

// -------- Validator --------
class BookingValidator {

    public static void validate(UC9Reservation reservation, UC9RoomInventory inventory)
            throws InvalidBookingException {

        if (reservation.guestName() == null || reservation.guestName().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        if (!inventory.isValidRoomType(reservation.roomType())) {
            throw new InvalidBookingException("Invalid room type: " + reservation.roomType());
        }

        if (inventory.getAvailability(reservation.roomType()) <= 0) {
            throw new InvalidBookingException("Room not available: " + reservation.roomType());
        }
    }
}

// -------- Booking Service --------
record UC9BookingService(UC9RoomInventory inventory) {

    public void book(UC9Reservation reservation) {
        try {
            // Validate first (fail-fast)
            BookingValidator.validate(reservation, inventory);

            // Safe allocation
            inventory.decrement(reservation.roomType());

            System.out.println("Booking SUCCESS for "
                    + reservation.guestName()
                    + " (" + reservation.roomType() + ")");

        } catch (InvalidBookingException e) {
            System.out.println("Booking FAILED → " + e.getMessage());
        }
    }
}

// -------- Main --------
public class UseCase9ErrorHandlingValidation {

    public static void main(String[] args) {

        System.out.println("===== Book My Stay - Hotel Booking System v9.1 =====");

        UC9RoomInventory inventory = new UC9RoomInventory();
        UC9BookingService service = new UC9BookingService(inventory);

        // Valid booking
        service.book(new UC9Reservation("Alice", "Single Room"));

        // Invalid room type
        service.book(new UC9Reservation("Bob", "Luxury Room"));

        // Empty name
        service.book(new UC9Reservation("", "Double Room"));

        // Overbooking case
        service.book(new UC9Reservation("Charlie", "Double Room"));
        service.book(new UC9Reservation("David", "Double Room")); // should fail

        System.out.println("\nSystem remains stable after handling errors.");
    }
}