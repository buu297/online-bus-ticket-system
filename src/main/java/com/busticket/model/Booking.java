package com.busticket.model;

import com.busticket.patterns.decorator.TicketItem;
import com.busticket.patterns.state.BookingState;
import com.busticket.patterns.state.PendingState;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Context class in State Pattern & Target product of Builder Pattern.
 * Encapsulates the entire reservation record, its decorated ticket items,
 * and maintains its current BookingState.
 */
public class Booking {
    private final String bookingId;
    private final Trip trip;
    private final Passenger passenger;
    private final List<Seat> seats;
    private final TicketItem ticketItem;
    private final LocalDateTime bookingDate;
    private final String paymentMethod;
    private BookingState state;

    public Booking(String bookingId, Trip trip, Passenger passenger, List<Seat> seats,
                   TicketItem ticketItem, String paymentMethod) {
        this.bookingId = bookingId;
        this.trip = trip;
        this.passenger = passenger;
        this.seats = new ArrayList<>(seats);
        this.ticketItem = ticketItem;
        this.paymentMethod = paymentMethod;
        this.bookingDate = LocalDateTime.now();
        this.state = new PendingState(); // Default initial state
    }

    public String getBookingId() {
        return bookingId;
    }

    public Trip getTrip() {
        return trip;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public List<Seat> getSeats() {
        return Collections.unmodifiableList(seats);
    }

    public TicketItem getTicketItem() {
        return ticketItem;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public String getFormattedBookingDate() {
        return bookingDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public BookingState getState() {
        return state;
    }

    public void setState(BookingState state) {
        this.state = state;
    }

    public double getTotalPrice() {
        return ticketItem != null ? ticketItem.getCost() : 0.0;
    }

    public String getDescription() {
        return ticketItem != null ? ticketItem.getDescription() : "";
    }

    public List<String> getBreakdown() {
        return ticketItem != null ? ticketItem.getBreakdown() : Collections.emptyList();
    }

    // State Pattern Delegations
    public void pay() {
        state.pay(this);
    }

    public void cancel() {
        state.cancel(this);
    }

    public void complete() {
        state.complete(this);
    }

    /**
     * Marks all reserved seats as BOOKED on the bus.
     */
    public void reserveSeats() {
        for (Seat seat : seats) {
            seat.book();
        }
    }

    /**
     * Releases all reserved seats back to AVAILABLE on the bus.
     */
    public void releaseSeats() {
        for (Seat seat : seats) {
            seat.release();
        }
    }
}
