package com.busticket.patterns.factory;

import com.busticket.model.Bus;
import com.busticket.model.BusType;
import com.busticket.model.Seat;

/**
 * Concrete Product in Factory Pattern:
 * Standard single-deck bus with 2x2 seating layout (32 seats).
 */
public class StandardBus extends Bus {

    public StandardBus(String busId, String licensePlate) {
        super(busId, licensePlate, BusType.STANDARD, 32, 1);
    }

    @Override
    protected void initializeSeats() {
        // 8 rows x 4 seats (A, B - aisle - C, D)
        char[] cols = {'A', 'B', 'C', 'D'};
        for (int row = 1; row <= 8; row++) {
            for (char col : cols) {
                String seatNum = String.format("%c%02d", col, row);
                // First 2 rows (front rows) have slight premium extra fee
                double extraFee = (row <= 2) ? 15000.0 : 0.0;
                seats.add(new Seat(seatNum, 1, extraFee));
            }
        }
    }
}
