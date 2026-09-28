package com.busticket.service;

import com.busticket.model.*;
import com.busticket.patterns.factory.BusFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service managing bus routes and scheduled trips.
 * Seeds initial demo trips across major transit corridors in Vietnam.
 */
public class TripService {
    private final List<Route> routes = new ArrayList<>();
    private final List<Trip> trips = new ArrayList<>();

    public TripService() {
        seedData();
    }

    private void seedData() {
        // Seed popular routes
        Route rHnDn = new Route("R-01", "Hà Nội", "Đà Nẵng", 760, 14.5, 450000.0);
        Route rSgDl = new Route("R-02", "TP. Hồ Chí Minh", "Đà Lạt", 305, 7.0, 260000.0);
        Route rHnSp = new Route("R-03", "Hà Nội", "Sa Pa (Lào Cai)", 315, 5.5, 290000.0);
        Route rSgNt = new Route("R-04", "TP. Hồ Chí Minh", "Nha Trang", 435, 8.5, 320000.0);
        Route rHnHp = new Route("R-05", "Hà Nội", "Hải Phòng", 120, 1.8, 150000.0);
        Route rSgVt = new Route("R-06", "TP. Hồ Chí Minh", "Vũng Tàu", 100, 2.0, 160000.0);

        routes.add(rHnDn);
        routes.add(rSgDl);
        routes.add(rHnSp);
        routes.add(rSgNt);
        routes.add(rHnHp);
        routes.add(rSgVt);

        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String tomorrow = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        // Create buses via BusFactory (Factory Pattern)
        Bus busSleeper1 = BusFactory.createBus(BusType.SLEEPER, "BUS-01", "29B-888.68");
        Bus busLimo1    = BusFactory.createBus(BusType.VIP_LIMOUSINE, "BUS-02", "51B-999.99");
        Bus busStandard1= BusFactory.createBus(BusType.STANDARD, "BUS-03", "29B-123.45");
        Bus busSleeper2 = BusFactory.createBus(BusType.SLEEPER, "BUS-04", "51B-777.88");
        Bus busLimo2    = BusFactory.createBus(BusType.VIP_LIMOUSINE, "BUS-05", "29B-555.22");
        Bus busStandard2= BusFactory.createBus(BusType.STANDARD, "BUS-06", "72B-333.11");

        // Pre-book a few seats for realistic UI demonstration
        busSleeper1.getSeat("A01").book();
        busSleeper1.getSeat("A02").book();
        busSleeper1.getSeat("B05").book();

        busLimo1.getSeat("VIP-L01").book();
        busLimo1.getSeat("VIP-L02").book();

        busStandard1.getSeat("A01").book();
        busStandard1.getSeat("B01").book();
        busStandard1.getSeat("C03").book();

        // Create scheduled Trips
        trips.add(new Trip("TRIP-101", rSgDl, busSleeper1, "22:30", "05:30", today));
        trips.add(new Trip("TRIP-102", rSgDl, busLimo1, "23:45", "06:15", today));
        trips.add(new Trip("TRIP-103", rHnDn, busSleeper2, "17:00", "07:30", today));
        trips.add(new Trip("TRIP-104", rHnSp, busLimo2, "06:30", "12:00", today));
        trips.add(new Trip("TRIP-105", rHnHp, busStandard1, "08:00", "09:50", today));
        trips.add(new Trip("TRIP-106", rSgVt, busStandard2, "14:00", "16:00", today));
        trips.add(new Trip("TRIP-107", rSgNt, busLimo1, "21:00", "05:30", tomorrow));
        trips.add(new Trip("TRIP-108", rHnDn, busSleeper1, "19:30", "10:00", tomorrow));
    }

    public List<Route> getAllRoutes() {
        return Collections.unmodifiableList(routes);
    }

    public List<Trip> getAllTrips() {
        return Collections.unmodifiableList(trips);
    }

    public List<Trip> searchTrips(String origin, String destination) {
        return trips.stream()
                .filter(t -> (origin == null || origin.equals("Tất cả điểm đi") || t.getRoute().getOrigin().equalsIgnoreCase(origin))
                        && (destination == null || destination.equals("Tất cả điểm đến") || t.getRoute().getDestination().equalsIgnoreCase(destination)))
                .collect(Collectors.toList());
    }

    public List<String> getUniqueOrigins() {
        List<String> list = new ArrayList<>();
        list.add("Tất cả điểm đi");
        routes.stream().map(Route::getOrigin).distinct().forEach(list::add);
        return list;
    }

    public List<String> getUniqueDestinations() {
        List<String> list = new ArrayList<>();
        list.add("Tất cả điểm đến");
        routes.stream().map(Route::getDestination).distinct().forEach(list::add);
        return list;
    }
}
