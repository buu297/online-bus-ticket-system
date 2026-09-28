package com.busticket.patterns.state;

import com.busticket.model.Booking;

/**
 * Concrete State: The booking is confirmed and paid. Seats are firmly reserved.
 */
public class ConfirmedState implements BookingState {

    @Override
    public void pay(Booking booking) {
        // Already paid
    }

    @Override
    public void cancel(Booking booking) {
        // Transition to Cancelled and release seats
        booking.releaseSeats();
        booking.setState(new CancelledState());
    }

    @Override
    public void complete(Booking booking) {
        // Journey completed
        booking.setState(new CompletedState());
    }

    @Override
    public String getStatusName() {
        return "Đã thanh toán (Đã xác nhận)";
    }

    @Override
    public String getStatusColor() {
        return "#2ECC71"; // Emerald Green
    }

    @Override
    public boolean canPay() {
        return false;
    }

    @Override
    public boolean canCancel() {
        return true;
    }
}
