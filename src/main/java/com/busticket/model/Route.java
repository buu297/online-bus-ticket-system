package com.busticket.model;

/**
 * Represents a geographical travel route between two cities.
 */
public class Route {
    private final String routeId;
    private final String origin;
    private final String destination;
    private final int distanceKm;
    private final double estimatedHours;
    private final double basePrice; // Base price for standard bus in VND

    public Route(String routeId, String origin, String destination, int distanceKm, double estimatedHours, double basePrice) {
        this.routeId = routeId;
        this.origin = origin;
        this.destination = destination;
        this.distanceKm = distanceKm;
        this.estimatedHours = estimatedHours;
        this.basePrice = basePrice;
    }

    public String getRouteId() {
        return routeId;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public int getDistanceKm() {
        return distanceKm;
    }

    public double getEstimatedHours() {
        return estimatedHours;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public String getRouteName() {
        return origin + " ➔ " + destination;
    }

    @Override
    public String toString() {
        return String.format("%s (%d km - ~%.1fh)", getRouteName(), distanceKm, estimatedHours);
    }
}
