package com.busticket.patterns.decorator;

import java.util.List;

/**
 * Component interface in Decorator Pattern.
 * Represents a priced ticket or an add-on item in the booking.
 */
public interface TicketItem {
    /**
     * @return the total price of this ticket item including any decorations.
     */
    double getCost();

    /**
     * @return textual summary description of the ticket and services.
     */
    String getDescription();

    /**
     * @return list of itemized line entries for receipts.
     */
    List<String> getBreakdown();
}
