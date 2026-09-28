package com.busticket.patterns.state;

import com.busticket.model.Booking;

/**
 * Concrete State: The booking is newly created, pending confirmation and payment.
 */
public class PendingState implements BookingState {

    @Override
    public void pay(Booking booking) {
        // Transition to ConfirmedState
        booking.reserveSeats();
        booking.setState(new ConfirmedState());
    }

    @Override
    public void cancel(Booking booking) {
        // Direct transition to Cancelled
        booking.releaseSeats();
        booking.setState(new CancelledState());
    }

    @Override
    public void complete(Booking booking) {
        throw new IllegalStateException("Không thể hoàn thành vé khi chưa thanh toán / xác nhận.");
    }

    @Override
    public String getStatusName() {
        return "Chờ thanh toán";
    }

    @Override
    public String getStatusColor() {
        return "#F39C12"; // Amber / Warning
    }

    @Override
    public boolean canPay() {
        return true;
    }

    @Override
    public boolean canCancel() {
        return true;
    }
}
