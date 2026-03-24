import java.util.LinkedList;
import java.util.Queue;


// -------- Reservation Class --------
record Reservation(String guestName, String roomType) {

    public void display() {
        System.out.println("Guest: " + guestName + " | Room: " + roomType);
    }
}

// -------- Booking Queue --------
class BookingRequestQueue {
    private final Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    // Add request to queue
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Request added for " + reservation.guestName());
    }

    // Display all requests
    public void displayQueue() {
        System.out.println("\n--- Booking Request Queue (FIFO) ---");

        if (queue.isEmpty()) {
            System.out.println("No pending requests.");
            return;
        }

        for (Reservation r : queue) {
            r.display();
        }
    }
}

// -------- Main Class --------
public class UseCase5BookingRequestQueue {

    public static void main(String[] args) {

        System.out.println("===== Book My Stay - Hotel Booking System v5.1 =====");

        // Initialize queue
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Simulate booking requests
        Reservation r1 = new Reservation("Alice", "Single Room");
        Reservation r2 = new Reservation("Bob", "Double Room");
        Reservation r3 = new Reservation("Charlie", "Suite Room");

        // Add requests (FIFO order)
        bookingQueue.addRequest(r1);
        bookingQueue.addRequest(r2);
        bookingQueue.addRequest(r3);

        // Display queue
        bookingQueue.displayQueue();

        System.out.println("\nRequests are stored in arrival order. No allocation done yet.");
    }
}