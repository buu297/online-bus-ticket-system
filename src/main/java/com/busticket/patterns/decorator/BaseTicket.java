package com.busticket.patterns.decorator;

import com.busticket.model.Seat;
import com.busticket.model.Trip;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Concrete Component in Decorator Pattern.
 * Represents the fundamental ticket fare based on chosen trip and selected seats.
 */
public class BaseTicket implements TicketItem {
    private final Trip trip;
    private final List<Seat> seats;
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("vi-VN"));

    public BaseTicket(Trip trip, List<Seat> seats) {
        this.trip = trip;
        this.seats = new ArrayList<>(seats);
    }

    @Override
    public double getCost() {
        double total = 0.0;
        for (Seat seat : seats) {
            total += trip.calculateSeatPrice(seat);
        }
        return total;
    }

    @Override
    public String getDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append(trip.getRoute().getRouteName())
          .append(" (").append(trip.getBus().getBusType().getDisplayName()).append(") - Ghế: ");
        for (int i = 0; i < seats.size(); i++) {
            sb.append(seats.get(i).getSeatNumber());
            if (i < seats.size() - 1) sb.append(", ");
        }
        return sb.toString();
    }

    @Override
    public List<String> getBreakdown() {
        List<String> lines = new ArrayList<>();
        lines.add(String.format("Vé xe tuyến: %s", trip.getRoute().getRouteName()));
        lines.add(String.format("Loại xe: %s (%s)", trip.getBus().getBusType().getDisplayName(), trip.getBus().getLicensePlate()));
        for (Seat seat : seats) {
            lines.add(String.format("  • Ghế %s: %s", seat.getSeatNumber(), CURRENCY_FORMAT.format(trip.calculateSeatPrice(seat))));
        }
        lines.add(String.format("Tổng cước ghế cơ bản: %s", CURRENCY_FORMAT.format(getCost())));
        return lines;
    }
}
