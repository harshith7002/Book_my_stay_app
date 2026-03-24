import java.util.*;

/**
 * UseCase6RoomAllocationService
 * <p>
 * Booking confirmation with safe allocation and no duplicate classes.
 *
 * @author YourName
 * @version 6.1
 */

// -------- Reservation --------
record UC6Reservation(String guestName, String roomType) {
}

// -------- Inventory --------
class UC6RoomInventory {
    private final HashMap<String, Integer> inventory;

    public UC6RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrement(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void display() {
        System.out.println("\n--- Inventory ---");
        for (Map.Entry<String, Integer> e : inventory.entrySet()) {
            System.out.println(e.getKey() + " : " + e.getValue());
        }
    }
}

// -------- Booking Queue --------
class UC6BookingRequestQueue {
    private final Queue<UC6Reservation> queue = new LinkedList<>();

    public void add(UC6Reservation r) {
        queue.offer(r);
    }

    public UC6Reservation poll() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// -------- Booking Service --------
class UC6BookingService {

    private final UC6RoomInventory inventory;

    private final Set<String> allocatedRoomIds = new HashSet<>();
    private final Map<String, Set<String>> roomAllocations = new HashMap<>();

    private int idCounter = 1;

    public UC6BookingService(UC6RoomInventory inventory) {
        this.inventory = inventory;
    }

    private String generateRoomId(String roomType) {
        String id;
        do {
            id = roomType.replaceAll(" ", "").toUpperCase() + "-" + idCounter++;
        } while (allocatedRoomIds.contains(id));

        allocatedRoomIds.add(id);
        return id;
    }

    public void processQueue(UC6BookingRequestQueue queue) {

        System.out.println("\n--- Processing Booking Requests ---");

        while (!queue.isEmpty()) {

            UC6Reservation r = queue.poll();
            String type = r.roomType();

            System.out.println("\nProcessing: " + r.guestName() + " (" + type + ")");

            if (inventory.getAvailability(type) > 0) {

                String roomId = generateRoomId(type);

                roomAllocations.putIfAbsent(type, new HashSet<>());
                roomAllocations.get(type).add(roomId);

                inventory.decrement(type);

                System.out.println("Booking CONFIRMED → Room ID: " + roomId);

            } else {
                System.out.println("Booking FAILED → No rooms available");
            }
        }
    }

    public void displayAllocations() {
        System.out.println("\n--- Allocated Rooms ---");
        for (Map.Entry<String, Set<String>> entry : roomAllocations.entrySet()) {
            System.out.println(entry.getKey() + " → " + entry.getValue());
        }
    }
}

// -------- Main --------
public class UseCase6RoomAllocationService {

    public static void main(String[] args) {

        System.out.println("===== Book My Stay - Hotel Booking System v6.1 =====");

        UC6RoomInventory inventory = new UC6RoomInventory();

        UC6BookingRequestQueue queue = new UC6BookingRequestQueue();
        queue.add(new UC6Reservation("Alice", "Single Room"));
        queue.add(new UC6Reservation("Bob", "Single Room"));
        queue.add(new UC6Reservation("Charlie", "Single Room")); // should fail
        queue.add(new UC6Reservation("David", "Double Room"));

        UC6BookingService service = new UC6BookingService(inventory);

        service.processQueue(queue);

        service.displayAllocations();
        inventory.display();

        System.out.println("\nAll bookings processed.");
    }
}