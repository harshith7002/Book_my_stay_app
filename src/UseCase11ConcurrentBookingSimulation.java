import java.util.*;


// -------- Reservation --------
record UC11Reservation(String guestName, String roomType) {
}

// -------- Shared Inventory --------
class UC11RoomInventory {
    private final Map<String, Integer> inventory = new HashMap<>();

    public UC11RoomInventory() {
        inventory.put("Single Room", 1);
        inventory.put("Double Room", 1);
    }

    // synchronized → critical section
    public synchronized boolean allocateRoom(String roomType) {
        int available = inventory.getOrDefault(roomType, 0);

        if (available > 0) {
            inventory.put(roomType, available - 1);
            return true;
        }
        return false;
    }

    public void display() {
        System.out.println("\n--- Final Inventory ---");
        for (Map.Entry<String, Integer> e : inventory.entrySet()) {
            System.out.println(e.getKey() + " : " + e.getValue());
        }
    }
}

// -------- Shared Queue --------
class UC11BookingQueue {
    private final Queue<UC11Reservation> queue = new LinkedList<>();

    public synchronized void add(UC11Reservation r) {
        queue.offer(r);
    }

    public synchronized UC11Reservation poll() {
        return queue.poll();
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
}

// -------- Worker Thread --------
class BookingWorker extends Thread {

    private final UC11BookingQueue queue;
    private final UC11RoomInventory inventory;

    public BookingWorker(String name, UC11BookingQueue queue, UC11RoomInventory inventory) {
        super(name);
        this.queue = queue;
        this.inventory = inventory;
    }

    public void run() {

        while (true) {
            UC11Reservation r;

            // synchronized queue access
            synchronized (queue) {
                if (queue.isEmpty()) break;
                r = queue.poll();
            }

            if (r != null) {
                boolean success = inventory.allocateRoom(r.roomType());

                if (success) {
                    System.out.println(getName() + " → Booking CONFIRMED for "
                            + r.guestName() + " (" + r.roomType() + ")");
                } else {
                    System.out.println(getName() + " → Booking FAILED for "
                            + r.guestName() + " (No availability)");
                }
            }
        }
    }
}

// -------- Main --------
public class UseCase11ConcurrentBookingSimulation {

    public static void main(String[] args) {

        System.out.println("===== Book My Stay - Hotel Booking System v11.1 =====");

        UC11RoomInventory inventory = new UC11RoomInventory();
        UC11BookingQueue queue = new UC11BookingQueue();

        // Simulate concurrent requests
        queue.add(new UC11Reservation("Alice", "Single Room"));
        queue.add(new UC11Reservation("Bob", "Single Room"));
        queue.add(new UC11Reservation("Charlie", "Double Room"));
        queue.add(new UC11Reservation("David", "Double Room"));

        // Multiple threads (guests)
        Thread t1 = new BookingWorker("Thread-1", queue, inventory);
        Thread t2 = new BookingWorker("Thread-2", queue, inventory);

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        inventory.display();

        System.out.println("\nAll concurrent bookings processed safely.");
    }
}