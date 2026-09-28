package com.busticket.test;

import com.busticket.model.*;
import com.busticket.patterns.builder.BookingBuilder;
import com.busticket.patterns.decorator.*;
import com.busticket.patterns.factory.BusFactory;
import com.busticket.patterns.state.CancelledState;
import com.busticket.patterns.state.ConfirmedState;
import com.busticket.patterns.state.PendingState;

import java.util.Collections;
import java.util.List;

/**
 * Automated Verification Test for the 4 Design Patterns.
 */
public class PatternVerificationTest {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  STARTING DESIGN PATTERNS VERIFICATION SUITE");
        System.out.println("==================================================");

        testFactoryPattern();
        testDecoratorPattern();
        testStatePattern();
        testBuilderPattern();

        System.out.println("==================================================");
        System.out.println("  ALL 4 DESIGN PATTERNS VERIFIED SUCCESSFULLY! [OK]");
        System.out.println("==================================================");
    }

    private static void testFactoryPattern() {
        System.out.print("[TEST 1/4] Factory Pattern: ");
        Bus standard = BusFactory.createBus(BusType.STANDARD, "B1", "29B-111");
        Bus sleeper = BusFactory.createBus(BusType.SLEEPER, "B2", "29B-222");
        Bus limo = BusFactory.createBus(BusType.VIP_LIMOUSINE, "B3", "29B-333");

        assert standard.getTotalSeats() == 32 : "Standard bus seats mismatch";
        assert sleeper.getTotalSeats() == 36 : "Sleeper bus seats mismatch";
        assert limo.getTotalSeats() == 22 : "Limo bus seats mismatch";
        assert standard.getDecks() == 1 : "Standard bus decks mismatch";
        assert sleeper.getDecks() == 2 : "Sleeper bus decks mismatch";
        assert limo.getDecks() == 2 : "Limo bus decks mismatch";

        System.out.println("PASSED [OK] (Standard: 32 seats, Sleeper: 36 seats 2 decks, Limousine: 22 cabins)");
    }

    private static void testDecoratorPattern() {
        System.out.print("[TEST 2/4] Decorator Pattern: ");
        Route route = new Route("R1", "Hà Nội", "Đà Nẵng", 760, 14.5, 500000.0);
        Bus standard = BusFactory.createBus(BusType.STANDARD, "B1", "29B-111");
        Trip trip = new Trip("T1", route, standard, "08:00", "22:30", "2025-10-15");

        Seat seat1 = standard.getSeats().get(5);
        TicketItem ticket = new BaseTicket(trip, Collections.singletonList(seat1));
        double basePrice = ticket.getCost();
        assert basePrice == trip.calculateSeatPrice(seat1) : "Base price mismatch: " + basePrice;

        // Wrap with Insurance (+50,000)
        ticket = new InsuranceDecorator(ticket);
        assert ticket.getCost() == basePrice + InsuranceDecorator.PRICE : "Insurance price mismatch";

        // Wrap with Meal (+75,000)
        ticket = new MealDecorator(ticket);
        assert ticket.getCost() == basePrice + InsuranceDecorator.PRICE + MealDecorator.PRICE : "Meal price mismatch";

        assert ticket.getDescription().contains("Bảo hiểm") : "Description missing Insurance";
        assert ticket.getDescription().contains("Suất ăn") : "Description missing Meal";
        assert ticket.getBreakdown().size() >= 3 : "Breakdown lines count mismatch";

        System.out.println("PASSED [OK] (Base: " + basePrice + " -> +Insurance +Meal: " + ticket.getCost() + ")");
    }

    private static void testStatePattern() {
        System.out.print("[TEST 3/4] State Pattern: ");
        Route route = new Route("R1", "Sài Gòn", "Đà Lạt", 305, 7.0, 300000.0);
        Bus bus = BusFactory.createBus(BusType.SLEEPER, "B1", "51B-123.45");
        Trip trip = new Trip("T1", route, bus, "22:00", "05:00", "2025-10-15");
        Seat seat = bus.getSeat("A01");

        Booking booking = new BookingBuilder()
                .setBookingId("TEST-BK-01")
                .setTrip(trip)
                .setPassenger(new Passenger("Nguyễn Văn A", "0912345678", "a@test.com", "Bến xe Miền Đông"))
                .addSeat(seat)
                .build();

        // 1. Initial State -> PendingState
        assert booking.getState() instanceof PendingState : "Initial state is not PendingState";
        assert seat.getStatus() != SeatStatus.BOOKED : "Seat should not be booked yet in pending";

        // 2. Pay -> ConfirmedState
        booking.pay();
        assert booking.getState() instanceof ConfirmedState : "State after pay() is not ConfirmedState";
        assert seat.getStatus() == SeatStatus.BOOKED : "Seat should be BOOKED after pay()";

        // 3. Cancel -> CancelledState & seat released
        booking.cancel();
        assert booking.getState() instanceof CancelledState : "State after cancel() is not CancelledState";
        assert seat.getStatus() == SeatStatus.AVAILABLE : "Seat should be released back to AVAILABLE after cancel()";

        System.out.println("PASSED [OK] (Pending -> Confirmed [Seat BOOKED] -> Cancelled [Seat AVAILABLE])");
    }

    private static void testBuilderPattern() {
        System.out.print("[TEST 4/4] Builder Pattern: ");
        Route route = new Route("R1", "Hà Nội", "Sa Pa", 315, 5.5, 350000.0);
        Bus limo = BusFactory.createBus(BusType.VIP_LIMOUSINE, "B-VIP", "29B-999");
        Trip trip = new Trip("T-SP", route, limo, "07:00", "12:30", "2025-10-16");

        Passenger passenger = new Passenger("Trần Thị B", "0988776655", "b@test.com", "Mỹ Đình");
        List<Seat> seats = Collections.singletonList(limo.getSeat("VIP-L03"));

        Booking booking = new BookingBuilder()
                .setTrip(trip)
                .setPassenger(passenger)
                .setSelectedSeats(seats)
                .setInsurance(true)
                .setMeal(true)
                .setExtraLuggage(true)
                .setPriorityBoarding(true)
                .setPaymentMethod("VietQR")
                .build();

        assert booking.getBookingId().startsWith("BK-") : "Generated booking ID invalid";
        assert booking.getPassenger().getFullName().equals("Trần Thị B") : "Passenger name mismatch";
        assert booking.getSeats().size() == 1 : "Seats count mismatch";
        assert booking.getTotalPrice() > 0 : "Total price should be greater than zero";
        assert booking.getBreakdown().size() >= 5 : "Breakdown should include all decorated services";

        System.out.println("PASSED [OK] (Fluent construction of Booking ID: " + booking.getBookingId() + ", Total: " + booking.getTotalPrice() + ")");
    }
}