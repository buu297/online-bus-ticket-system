package com.busticket.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Abstract Bus entity representing a generic passenger bus.
 * Subclasses define specific seat layouts and configurations (Factory Pattern).
 */
public abstract class Bus {
    protected final String busId;
    protected final String licensePlate;
    protected final BusType busType;
    protected final int totalSeats;
    protected final int decks;
    protected final List<Seat> seats;

    public Bus(String busId, String licensePlate, BusType busType, int totalSeats, int decks) {
        this.busId = busId;
        this.licensePlate = licensePlate;
        this.busType = busType;
        this.totalSeats = totalSeats;
        this.decks = decks;
        this.seats = new ArrayList<>();
        initializeSeats();
    }

    /**
     * Initializes the seat layout specific to this bus model.
     */
    protected abstract void initializeSeats();

    public String getBusId() {
        return busId;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public BusType getBusType() {
        return busType;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public int getDecks() {
        return decks;
    }

    public List<Seat> getSeats() {
        return Collections.unmodifiableList(seats);
    }

    public Seat getSeat(String seatNumber) {
        for (Seat seat : seats) {
            if (seat.getSeatNumber().equalsIgnoreCase(seatNumber)) {
                return seat;
            }
        }
        return null;
    }

    public List<Seat> getSeatsByDeck(int deck) {
        return seats.stream()
                .filter(s -> s.getDeck() == deck)
                .collect(Collectors.toList());
    }

    public int getAvailableSeatCount() {
        return (int) seats.stream().filter(Seat::isAvailable).count();
    }

    public List<Seat> getAvailableSeats() {
        return seats.stream().filter(Seat::isAvailable).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s) - %d chỗ", busId, licensePlate, busType.getDisplayName(), totalSeats);
    }
}
