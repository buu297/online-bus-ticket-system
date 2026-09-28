package com.busticket.patterns.state;

import com.busticket.model.Booking;

/**
 * Concrete State: The passenger completed the journey.
 */
public class CompletedState implements BookingState {

    @Override
    public void pay(Booking booking) {
        // Already paid and completed
    }

    @Override
    public void cancel(Booking booking) {
        throw new IllegalStateException("Hành trình đã hoàn thành, không thể hủy vé.");
    }

    @Override
    public void complete(Booking booking) {
        // Already completed
    }

    @Override
    public String getStatusName() {
        return "Hoàn thành hành trình";
    }

    @Override
    public String getStatusColor() {
        return "#3498DB"; // Peter River Blue
    }

    @Override
    public boolean canPay() {
        return false;
    }

    @Override
    public boolean canCancel() {
        return false;
    }
}
