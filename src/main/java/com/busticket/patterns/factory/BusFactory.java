package com.busticket.patterns.factory;

import com.busticket.model.Bus;
import com.busticket.model.BusType;

/**
 * Factory Pattern implementation for Bus creation.
 * Encapsulates instantiation logic, ensuring the client code is decoupled
 * from specific bus subclasses (Open/Closed Principle).
 */
public class BusFactory {

    /**
     * Factory method to create a Bus based on BusType.
     *
     * @param type         The desired BusType
     * @param busId        Unique vehicle identifier
     * @param licensePlate Vehicle registration plate number
     * @return Concrete Bus instance
     */
    public static Bus createBus(BusType type, String busId, String licensePlate) {
        if (type == null) {
            throw new IllegalArgumentException("BusType cannot be null");
        }

        switch (type) {
            case STANDARD:
                return new StandardBus(busId, licensePlate);
            case SLEEPER:
                return new SleeperBus(busId, licensePlate);
            case VIP_LIMOUSINE:
                return new VipLimousineBus(busId, licensePlate);
            default:
                throw new UnsupportedOperationException("Unsupported bus type: " + type);
        }
    }
}
