package com.busticket.patterns.builder;

import com.busticket.model.Booking;
import com.busticket.model.Passenger;
import com.busticket.model.Seat;
import com.busticket.model.Trip;
import com.busticket.patterns.decorator.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Builder Pattern implementation for Booking.
 * Provides a fluent API to assemble complex Booking objects step-by-step
 * with validation, automatic TicketItem decoration, and ID generation.
 */
public class BookingBuilder {
    private String bookingId;
    private Trip trip;
    private Passenger passenger;
    private final List<Seat> selectedSeats = new ArrayList<>();
    private boolean insuranceSelected = false;
    private boolean mealSelected = false;
    private boolean extraLuggageSelected = false;
    private boolean priorityBoardingSelected = false;
    private String paymentMethod = "Chuyển khoản QR";

    public BookingBuilder() {
    }

    public BookingBuilder setBookingId(String bookingId) {
        this.bookingId = bookingId;
        return this;
    }

    public BookingBuilder setTrip(Trip trip) {
        this.trip = trip;
        return this;
    }

    public BookingBuilder setPassenger(Passenger passenger) {
        this.passenger = passenger;
        return this;
    }

    public BookingBuilder setSelectedSeats(List<Seat> seats) {
        this.selectedSeats.clear();
        if (seats != null) {
            this.selectedSeats.addAll(seats);
        }
        return this;
    }

    public BookingBuilder addSeat(Seat seat) {
        if (seat != null && !this.selectedSeats.contains(seat)) {
            this.selectedSeats.add(seat);
        }
        return this;
    }

    public BookingBuilder setInsurance(boolean enabled) {
        this.insuranceSelected = enabled;
        return this;
    }

    public BookingBuilder setMeal(boolean enabled) {
        this.mealSelected = enabled;
        return this;
    }

    public BookingBuilder setExtraLuggage(boolean enabled) {
        this.extraLuggageSelected = enabled;
        return this;
    }

    public BookingBuilder setPriorityBoarding(boolean enabled) {
        this.priorityBoardingSelected = enabled;
        return this;
    }

    public BookingBuilder setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    /**
     * Builds and validates the complete Booking instance.
     */
    public Booking build() {
        if (trip == null) {
            throw new IllegalStateException("Vui lòng chọn chuyến xe trước khi đặt vé.");
        }
        if (passenger == null || passenger.getFullName() == null || passenger.getFullName().trim().isEmpty()) {
            throw new IllegalStateException("Thông tin hành khách không được để trống.");
        }
        if (selectedSeats.isEmpty()) {
            throw new IllegalStateException("Vui lòng chọn ít nhất 1 chỗ ngồi.");
        }

        // Generate unique Booking ID if not explicitly specified
        if (bookingId == null || bookingId.trim().isEmpty()) {
            String randomDigits = String.format("%04d", new Random().nextInt(10000));
            this.bookingId = "BK-" + System.currentTimeMillis() % 1000000 + "-" + randomDigits;
        }

        // Assemble decorated TicketItem dynamically using Decorator Pattern
        TicketItem ticket = new BaseTicket(trip, selectedSeats);

        if (insuranceSelected) {
            ticket = new InsuranceDecorator(ticket);
        }
        if (mealSelected) {
            ticket = new MealDecorator(ticket);
        }
        if (extraLuggageSelected) {
            ticket = new ExtraLuggageDecorator(ticket);
        }
        if (priorityBoardingSelected) {
            ticket = new PriorityBoardingDecorator(ticket);
        }

        return new Booking(bookingId, trip, passenger, selectedSeats, ticket, paymentMethod);
    }
}
