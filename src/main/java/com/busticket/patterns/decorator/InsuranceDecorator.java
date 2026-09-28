package com.busticket.patterns.decorator;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Concrete Decorator: Adds comprehensive Travel Accident & Delay Insurance.
 */
public class InsuranceDecorator extends TicketDecorator {
    public static final double PRICE = 50000.0;
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("vi-VN"));

    public InsuranceDecorator(TicketItem decoratedTicket) {
        super(decoratedTicket);
    }

    @Override
    public double getCost() {
        return super.getCost() + PRICE;
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " + [Bảo hiểm chuyến đi]";
    }

    @Override
    public List<String> getBreakdown() {
        List<String> breakdown = super.getBreakdown();
        breakdown.add(String.format("  + Bảo hiểm tai nạn & trễ chuyến: %s", CURRENCY_FORMAT.format(PRICE)));
        return breakdown;
    }
}
