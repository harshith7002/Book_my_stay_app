import java.util.*;

// -------- Reservation --------
record UC7Reservation(String reservationId) {

}

// -------- Add-On Service --------
record AddOnService(String serviceName, double cost) {
}

// -------- Service Manager --------
class AddOnServiceManager {

    // Map<ReservationID, List of Services>
    private final Map<String, List<AddOnService>> serviceMap = new HashMap<>();

    // Add service to reservation
    public void addService(String reservationId, AddOnService service) {
        serviceMap.putIfAbsent(reservationId, new ArrayList<>());
        serviceMap.get(reservationId).add(service);

        System.out.println("Added service: " + service.serviceName()
                + " to Reservation: " + reservationId);
    }

    // Display services
    public void displayServices(String reservationId) {
        System.out.println("\nServices for Reservation: " + reservationId);

        List<AddOnService> services = serviceMap.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }

        for (AddOnService s : services) {
            System.out.println("- " + s.serviceName() + " ($" + s.cost() + ")");
        }
    }

    // Calculate total cost
    public double calculateTotalCost(String reservationId) {
        double total = 0;
        List<AddOnService> services = serviceMap.get(reservationId);

        if (services != null) {
            for (AddOnService s : services) {
                total += s.cost();
            }
        }
        return total;
    }
}

// -------- Main --------
public class UseCase7AddOnServiceSelection {

    public static void main(String[] args) {

        System.out.println("===== Book My Stay - Hotel Booking System v7.1 =====");

        // Create reservation (already booked in UC6)
        UC7Reservation reservation = new UC7Reservation("RES-101");

        // Create service manager
        AddOnServiceManager manager = getAddOnServiceManager(reservation);

        // Total cost
        double total = manager.calculateTotalCost(reservation.reservationId());

        System.out.println("\nTotal Add-On Cost: $" + total);

        System.out.println("\nCore booking remains unchanged.");
    }

    private static AddOnServiceManager getAddOnServiceManager(UC7Reservation reservation) {
        AddOnServiceManager manager = new AddOnServiceManager();

        // Add services
        manager.addService(reservation.reservationId(),
                new AddOnService("Breakfast", 20.0));

        manager.addService(reservation.reservationId(),
                new AddOnService("Airport Pickup", 50.0));

        manager.addService(reservation.reservationId(),
                new AddOnService("Extra Bed", 30.0));

        // Display services
        manager.displayServices(reservation.reservationId());
        return manager;
    }
}