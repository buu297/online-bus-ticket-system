package com.busticket.patterns.decorator;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract Decorator class adhering to Decorator Pattern.
 * Delegates calls to the wrapped TicketItem and allows subclasses to add extra behavior.
 */
public abstract class TicketDecorator implements TicketItem {
    protected final TicketItem decoratedTicket;

    public TicketDecorator(TicketItem decoratedTicket) {
        if (decoratedTicket == null) {
            throw new IllegalArgumentException("Decorated ticket item cannot be null");
        }
        this.decoratedTicket = decoratedTicket;
    }

    @Override
    public double getCost() {
        return decoratedTicket.getCost();
    }

    @Override
    public String getDescription() {
        return decoratedTicket.getDescription();
    }

    @Override
    public List<String> getBreakdown() {
        return new ArrayList<>(decoratedTicket.getBreakdown());
    }

    public TicketItem getDecoratedTicket() {
        return decoratedTicket;
    }
}
