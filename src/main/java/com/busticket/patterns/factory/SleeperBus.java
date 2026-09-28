package com.busticket.patterns.factory;

import com.busticket.model.Bus;
import com.busticket.model.BusType;
import com.busticket.model.Seat;

/**
 * Concrete Product in Factory Pattern:
 * Double-deck sleeper bus with 36 berths (18 on Lower Deck, 18 on Upper Deck).
 */
public class SleeperBus extends Bus {

    public SleeperBus(String busId, String licensePlate) {
        super(busId, licensePlate, BusType.SLEEPER, 36, 2);
    }

    @Override
    protected void initializeSeats() {
        // Deck 1 (Lower Deck): A01 - A18
        for (int i = 1; i <= 18; i++) {
            String seatNum = String.format("A%02d", i);
            // Lower deck middle berths are standard, front berths have convenience fee
            double extraFee = (i <= 6) ? 10000.0 : 0.0;
            seats.add(new Seat(seatNum, 1, extraFee));
        }

        // Deck 2 (Upper Deck): B01 - B18
        for (int i = 1; i <= 18; i++) {
            String seatNum = String.format("B%02d", i);
            seats.add(new Seat(seatNum, 2, 0.0));
        }
    }
}
