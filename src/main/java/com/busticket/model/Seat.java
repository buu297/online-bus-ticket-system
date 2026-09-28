package com.busticket.model;

import java.util.Objects;

/**
 * Represents an individual seat or berth in a bus.
 */
public class Seat {
    private final String seatNumber;
    private final int deck; // 1 = Lower/Ground deck, 2 = Upper deck
    private SeatStatus status;
    private final double extraFee; // Additional fee if premium seat

    public Seat(String seatNumber, int deck) {
        this(seatNumber, deck, 0.0);
    }

    public Seat(String seatNumber, int deck, double extraFee) {
        this.seatNumber = seatNumber;
        this.deck = deck;
        this.status = SeatStatus.AVAILABLE;
        this.extraFee = extraFee;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public int getDeck() {
        return deck;
    }

    public SeatStatus getStatus() {
        return status;
    }

    public void setStatus(SeatStatus status) {
        this.status = status;
    }

    public double getExtraFee() {
        return extraFee;
    }

    public boolean isAvailable() {
        return this.status == SeatStatus.AVAILABLE;
    }

    public void book() {
        this.status = SeatStatus.BOOKED;
    }

    public void release() {
        this.status = SeatStatus.AVAILABLE;
    }

    public void select() {
        if (this.status == SeatStatus.AVAILABLE) {
            this.status = SeatStatus.SELECTED;
        }
    }

    public void unselect() {
        if (this.status == SeatStatus.SELECTED) {
            this.status = SeatStatus.AVAILABLE;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Seat seat)) return false;
        return Objects.equals(seatNumber, seat.seatNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seatNumber);
    }

    @Override
    public String toString() {
        return seatNumber + " (" + status.getDisplayName() + ")";
    }
}
