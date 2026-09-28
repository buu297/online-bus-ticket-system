package com.busticket.service;

import com.busticket.model.Booking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Service managing customer bookings, state transitions, and event notifications.
 */
public class BookingService {
    private static BookingService instance;
    private final List<Booking> bookings = new CopyOnWriteArrayList<>();
    private final List<Consumer<Booking>> bookingChangeListeners = new ArrayList<>();

    private BookingService() {}

    public static synchronized BookingService getInstance() {
        if (instance == null) {
            instance = new BookingService();
        }
        return instance;
    }

    public void addBookingChangeListener(Consumer<Booking> listener) {
        bookingChangeListeners.add(listener);
    }

    private void notifyChange(Booking booking) {
        for (Consumer<Booking> listener : bookingChangeListeners) {
            try {
                listener.accept(booking);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Confirms and saves a booking, executing state transition to ConfirmedState.
     */
    public void confirmBooking(Booking booking) {
        if (booking == null) return;
        booking.pay(); // State transition: Pending -> Confirmed & seats booked
        bookings.add(0, booking);
        notifyChange(booking);
    }

    /**
     * Cancels an existing booking, invoking State pattern cancel() to release seats.
     */
    public boolean cancelBooking(String bookingId) {
        Optional<Booking> opt = bookings.stream()
                .filter(b -> b.getBookingId().equalsIgnoreCase(bookingId))
                .findFirst();

        if (opt.isPresent()) {
            Booking booking = opt.get();
            booking.cancel(); // State pattern transition: Confirmed -> Cancelled & seats freed
            notifyChange(booking);
            return true;
        }
        return false;
    }

    public List<Booking> getAllBookings() {
        return Collections.unmodifiableList(bookings);
    }

    public List<Booking> searchBookings(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllBookings();
        }
        String lower = keyword.trim().toLowerCase();
        return bookings.stream()
                .filter(b -> b.getBookingId().toLowerCase().contains(lower)
                        || b.getPassenger().getFullName().toLowerCase().contains(lower)
                        || b.getPassenger().getPhoneNumber().contains(lower)
                        || b.getTrip().getRoute().getRouteName().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }
}
