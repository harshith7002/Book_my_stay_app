import java.io.*;
import java.util.*;

/**
 * UseCase12DataPersistenceRecovery
 * <p>
 * Demonstrates saving and restoring system state using serialization.
 * Ensures booking and inventory data persist across restarts.
 *
 * @author YourName
 * @version 12.1
 */

// -------- Reservation --------
record UC12Reservation(String reservationId, String roomType) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public void display() {
        System.out.println("ID: " + reservationId + " | Room: " + roomType);
    }
}

// -------- System State --------
class SystemState implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    Map<String, Integer> inventory;
    List<UC12Reservation> bookings;

    public SystemState(Map<String, Integer> inventory, List<UC12Reservation> bookings) {
        this.inventory = inventory;
        this.bookings = bookings;
    }
}

// -------- Persistence Service --------
class PersistenceService {

    private static final String FILE_NAME = "system_state.ser";

    // Save state
    public void save(SystemState state) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(state);
            System.out.println("\nState saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving state: " + e.getMessage());
        }
    }

    // Load state
    public SystemState load() {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            SystemState state = (SystemState) ois.readObject();
            System.out.println("State loaded successfully.");
            return state;

        } catch (FileNotFoundException e) {
            System.out.println("No saved data found. Starting fresh.");
        } catch (Exception e) {
            System.out.println("Error loading state. Starting with safe defaults.");
        }
        return null;
    }
}

// -------- Main --------
public class UseCase12DataPersistenceRecovery {

    public static void main(String[] args) {

        System.out.println("===== Book My Stay - Hotel Booking System v12.1 =====");

        PersistenceService persistence = new PersistenceService();

        // Try loading previous state
        SystemState state = persistence.load();

        Map<String, Integer> inventory;
        List<UC12Reservation> bookings;

        if (state != null) {
            inventory = state.inventory;
            bookings = state.bookings;
        } else {
            // Initialize fresh state
            inventory = new HashMap<>();
            inventory.put("Single Room", 2);
            inventory.put("Double Room", 1);

            bookings = new ArrayList<>();
        }

        // Simulate booking
        UC12Reservation r1 = new UC12Reservation("RES-201", "Single Room");
        bookings.add(r1);
        inventory.put("Single Room", inventory.get("Single Room") - 1);

        // Display current state
        System.out.println("\n--- Current Bookings ---");
        for (UC12Reservation r : bookings) {
            r.display();
        }

        System.out.println("\n--- Current Inventory ---");
        for (Map.Entry<String, Integer> e : inventory.entrySet()) {
            System.out.println(e.getKey() + " : " + e.getValue());
        }

        // Save state before exit
        persistence.save(new SystemState(inventory, bookings));

        System.out.println("\nRestart the program to see recovery in action.");
    }
}