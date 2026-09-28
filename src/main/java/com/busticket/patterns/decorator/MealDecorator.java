package com.busticket.patterns.decorator;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Concrete Decorator: Adds Hot Meal & Beverage combo service.
 */
public class MealDecorator extends TicketDecorator {
    public static final double PRICE = 75000.0;
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("vi-VN"));

    public MealDecorator(TicketItem decoratedTicket) {
        super(decoratedTicket);
    }

    @Override
    public double getCost() {
        return super.getCost() + PRICE;
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " + [Suất ăn & Nước ngọt]";
    }

    @Override
    public List<String> getBreakdown() {
        List<String> breakdown = super.getBreakdown();
        breakdown.add(String.format("  + Suất ăn nóng & nước giải khát: %s", CURRENCY_FORMAT.format(PRICE)));
        return breakdown;
    }
}
