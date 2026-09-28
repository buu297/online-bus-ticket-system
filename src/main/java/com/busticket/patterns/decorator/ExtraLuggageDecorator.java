package com.busticket.patterns.decorator;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Concrete Decorator: Adds Extra Luggage (+10kg) allowance.
 */
public class ExtraLuggageDecorator extends TicketDecorator {
    public static final double PRICE = 40000.0;
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("vi-VN"));

    public ExtraLuggageDecorator(TicketItem decoratedTicket) {
        super(decoratedTicket);
    }

    @Override
    public double getCost() {
        return super.getCost() + PRICE;
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " + [Thêm hành lý 10kg]";
    }

    @Override
    public List<String> getBreakdown() {
        List<String> breakdown = super.getBreakdown();
        breakdown.add(String.format("  + Hành lý ký gửi thêm (10kg): %s", CURRENCY_FORMAT.format(PRICE)));
        return breakdown;
    }
}
