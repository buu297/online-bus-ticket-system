package com.busticket.patterns.state;

import com.busticket.model.Booking;

/**
 * State Pattern interface defining operations that change based on
 * the current lifecycle state of a Booking.
 */
public interface BookingState {
    /**
     * Executes payment or confirmation transition.
     */
    void pay(Booking booking);

    /**
     * Executes cancellation transition and frees up reserved resources.
     */
    void cancel(Booking booking);

    /**
     * Marks the journey as successfully completed.
     */
    void complete(Booking booking);

    /**
     * User-friendly Vietnamese display label.
     */
    String getStatusName();

    /**
     * Hex color code used for UI badges.
     */
    String getStatusColor();

    boolean canPay();

    boolean canCancel();
}
