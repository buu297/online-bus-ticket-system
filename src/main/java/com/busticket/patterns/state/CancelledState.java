package com.busticket.patterns.state;

import com.busticket.model.Booking;

/**
 * Concrete State: The reservation was cancelled.
 */
public class CancelledState implements BookingState {

    @Override
    public void pay(Booking booking) {
        throw new IllegalStateException("Vé đã bị hủy, không thể thực hiện thanh toán.");
    }

    @Override
    public void cancel(Booking booking) {
        // Already cancelled
    }

    @Override
    public void complete(Booking booking) {
        throw new IllegalStateException("Vé đã hủy không thể hoàn thành hành trình.");
    }

    @Override
    public String getStatusName() {
        return "Đã hủy vé";
    }

    @Override
    public String getStatusColor() {
        return "#E74C3C"; // Alizarin Red
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
