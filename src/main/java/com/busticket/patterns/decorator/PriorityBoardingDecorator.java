package com.busticket.patterns.decorator;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Concrete Decorator: Adds Priority VIP Lounge Access & Expedited Boarding.
 */
public class PriorityBoardingDecorator extends TicketDecorator {
    public static final double PRICE = 30000.0;
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("vi-VN"));

    public PriorityBoardingDecorator(TicketItem decoratedTicket) {
        super(decoratedTicket);
    }

    @Override
    public double getCost() {
        return super.getCost() + PRICE;
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " + [Phòng chờ VIP & Lên xe ưu tiên]";
    }

    @Override
    public List<String> getBreakdown() {
        List<String> breakdown = super.getBreakdown();
        breakdown.add(String.format("  + Phòng chờ VIP & ưu tiên check-in: %s", CURRENCY_FORMAT.format(PRICE)));
        return breakdown;
    }
}
