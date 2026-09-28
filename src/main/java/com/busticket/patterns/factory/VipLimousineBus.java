package com.busticket.patterns.factory;

import com.busticket.model.Bus;
import com.busticket.model.BusType;
import com.busticket.model.Seat;

/**
 * Concrete Product in Factory Pattern:
 * Luxury VIP Limousine with 22 private royal cabins across 2 decks.
 */
public class VipLimousineBus extends Bus {

    public VipLimousineBus(String busId, String licensePlate) {
        super(busId, licensePlate, BusType.VIP_LIMOUSINE, 22, 2);
    }

    @Override
    protected void initializeSeats() {
        // Deck 1 (Lower Deck): VIP-01 to VIP-11
        for (int i = 1; i <= 11; i++) {
            String seatNum = String.format("VIP-L%02d", i);
            seats.add(new Seat(seatNum, 1, 20000.0));
        }

        // Deck 2 (Upper Deck): VIP-12 to VIP-22
        for (int i = 1; i <= 11; i++) {
            String seatNum = String.format("VIP-U%02d", i);
            seats.add(new Seat(seatNum, 2, 10000.0));
        }
    }
}
