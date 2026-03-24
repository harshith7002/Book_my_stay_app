import java.util.*;

// -------- Reservation --------
record UC8Reservation(String reservationId, String guestName, String roomType) {

    public void display() {
        System.out.println("ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room: " + roomType);
    }
}

// -------- Booking History --------
class BookingHistory {
    private final List<UC8Reservation> history = new ArrayList<>();

    // Add confirmed booking
    public void addReservation(UC8Reservation reservation) {
        history.add(reservation);
    }

    // Get all bookings
    public List<UC8Reservation> getAllReservations() {
        return history;
    }
}

// -------- Reporting Service --------
class BookingReportService {

    // Display all bookings
    public void displayAllBookings(List<UC8Reservation> reservations) {
        System.out.println("\n--- Booking History ---");

        if (reservations.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (UC8Reservation r : reservations) {
            r.display();
        }
    }

    // Summary report
    public void generateSummary(List<UC8Reservation> reservations) {
        System.out.println("\n--- Booking Summary Report ---");

        Map<String, Integer> countByRoom = new HashMap<>();

        for (UC8Reservation r : reservations) {
            countByRoom.put(
                    r.roomType(),
                    countByRoom.getOrDefault(r.roomType(), 0) + 1
            );
        }

        for (Map.Entry<String, Integer> entry : countByRoom.entrySet()) {
            System.out.println(entry.getKey() + " → " + entry.getValue() + " bookings");
        }
    }
}

// -------- Main --------
public class UseCase8BookingHistoryReport {

    public static void main(String[] args) {

        System.out.println("===== Book My Stay - Hotel Booking System v8.1 =====");

        // Booking history
        BookingHistory history = new BookingHistory();

        // Simulate confirmed bookings (from UC6)
        history.addReservation(new UC8Reservation("RES-101", "Alice", "Single Room"));
        history.addReservation(new UC8Reservation("RES-102", "Bob", "Double Room"));
        history.addReservation(new UC8Reservation("RES-103", "Charlie", "Single Room"));
        history.addReservation(new UC8Reservation("RES-104", "David", "Suite Room"));

        // Reporting
        BookingReportService reportService = new BookingReportService();

        // Display all bookings
        reportService.displayAllBookings(history.getAllReservations());

        // Generate summary
        reportService.generateSummary(history.getAllReservations());

        System.out.println("\nReporting completed. Data remains unchanged.");
    }
}