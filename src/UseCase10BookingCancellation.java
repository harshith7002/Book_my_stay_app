import java.util.*;

// -------- Reservation --------
record UC10Reservation(String reservationId, String roomType, String roomId) {
}

// -------- Inventory --------
class UC10RoomInventory {
    private final Map<String, Integer> inventory = new HashMap<>();

    public UC10RoomInventory() {
        inventory.put("Single Room", 1);
        inventory.put("Double Room", 1);
    }

    public void increment(String roomType) {
        inventory.put(roomType, inventory.getOrDefault(roomType, 0) + 1);
    }

    public void display() {
        System.out.println("\n--- Inventory ---");
        for (Map.Entry<String, Integer> e : inventory.entrySet()) {
            System.out.println(e.getKey() + " : " + e.getValue());
        }
    }
}

// -------- Booking History --------
class UC10BookingHistory {
    private final Map<String, UC10Reservation> confirmedBookings = new HashMap<>();

    public void add(UC10Reservation r) {
        confirmedBookings.put(r.reservationId(), r);
    }

    public UC10Reservation get(String reservationId) {
        return confirmedBookings.get(reservationId);
    }

    public void remove(String reservationId) {
        confirmedBookings.remove(reservationId);
    }
}

// -------- Cancellation Service --------
class CancellationService {

    private final UC10RoomInventory inventory;
    private final UC10BookingHistory history;

    // Stack for rollback (LIFO)
    private final Stack<String> rollbackStack = new Stack<>();

    public CancellationService(UC10RoomInventory inventory, UC10BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
    }

    public void cancel(String reservationId) {

        System.out.println("\nProcessing cancellation for: " + reservationId);

        UC10Reservation reservation = history.get(reservationId);

        // Validation
        if (reservation == null) {
            System.out.println("Cancellation FAILED → Reservation not found");
            return;
        }

        // Step 1: Push roomId to rollback stack
        rollbackStack.push(reservation.roomId());

        // Step 2: Restore inventory
        inventory.increment(reservation.roomType());

        // Step 3: Remove from history
        history.remove(reservationId);

        System.out.println("Cancellation SUCCESS → Room released: " + reservation.roomId());
    }

    public void displayRollbackStack() {
        System.out.println("\n--- Rollback Stack (LIFO) ---");
        System.out.println(rollbackStack);
    }
}

// -------- Main --------
public class UseCase10BookingCancellation {

    public static void main(String[] args) {

        System.out.println("===== Book My Stay - Hotel Booking System v10.1 =====");

        // Setup inventory & history
        UC10RoomInventory inventory = new UC10RoomInventory();
        UC10BookingHistory history = new UC10BookingHistory();

        // Simulate confirmed bookings (from UC6)
        history.add(new UC10Reservation("RES-101", "Single Room", "SINGLE-1"));
        history.add(new UC10Reservation("RES-102", "Double Room", "DOUBLE-1"));

        // Cancellation service
        CancellationService service = new CancellationService(inventory, history);

        // Perform cancellations
        service.cancel("RES-101"); // valid
        service.cancel("RES-999"); // invalid

        // Display results
        service.displayRollbackStack();
        inventory.display();

        System.out.println("\nSystem state restored safely.");
    }
}