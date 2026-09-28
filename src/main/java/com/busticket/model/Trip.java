package com.busticket.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a scheduled trip on a specific date, route, and assigned bus.
 */
public class Trip {
    private final String tripId;
    private final Route route;
    private final Bus bus;
    private final String departureTime;
    private final String arrivalTime;
    private final String travelDate;

    public Trip(String tripId, Route route, Bus bus, String departureTime, String arrivalTime, String travelDate) {
        this.tripId = tripId;
        this.route = route;
        this.bus = bus;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.travelDate = travelDate;
    }

    public String getTripId() {
        return tripId;
    }

    public Route getRoute() {
        return route;
    }

    public Bus getBus() {
        return bus;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public String getTravelDate() {
        return travelDate;
    }

    /**
     * Calculates the price for a specific seat on this trip based on route base price,
     * vehicle type multiplier, and seat-specific surcharge.
     */
    public double calculateSeatPrice(Seat seat) {
        double base = route.getBasePrice() * bus.getBusType().getPriceMultiplier();
        double extra = (seat != null) ? seat.getExtraFee() : 0.0;
        return Math.round((base + extra) / 1000.0) * 1000.0; // round to nearest 1,000 VND
    }

    public int getAvailableSeats() {
        return bus.getAvailableSeatCount();
    }

    @Override
    public String toString() {
        return String.format("[%s] %s | %s - %s | %s (%d chỗ trống)",
                tripId, route.getRouteName(), departureTime, arrivalTime,
                bus.getBusType().getDisplayName(), getAvailableSeats());
    }
}
