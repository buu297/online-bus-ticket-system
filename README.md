# 🚌 Online Bus Ticket Reservation System

[![Java](https://img.shields.io/badge/Java-21%20LTS-orange.svg)](https://openjdk.org/)
[![GUI](https://img.shields.io/badge/GUI-Java%20Swing-blue.svg)](https://docs.oracle.com/javase/tutorial/uiswing/)
[![Design Patterns](https://img.shields.io/badge/Design%20Patterns-Factory%20%7C%20Decorator%20%7C%20State%20%7C%20Builder-success.svg)]()
[![Architecture](https://img.shields.io/badge/Architecture-SOLID%20%7C%20MVC-brightgreen.svg)]()
[![Build](https://img.shields.io/badge/Build-Maven%20%7C%20Batch%20Scripts-informational.svg)]()

A modern, fully functional desktop bus ticket reservation application engineered in **Java Swing**. This project demonstrates the practical application of **SOLID principles** and **4 classic Gang-of-Four (GoF) Design Patterns** (*Factory, Decorator, State, Builder*) in an enterprise desktop architecture.

---

## 🌟 Key Highlights

- **Interactive Visual Seat Picker**: Dynamic bus seat map supporting single-deck and double-deck layouts (Sleeper Bus & VIP Limousine) with color-coded real-time availability states (*Available 🟢, Selected 🔵, Booked 🔴*).
- **Booking Lifecycle State Management**: Employs the **State Pattern** to handle booking transitions (*Pending ➔ Confirmed ➔ Cancelled / Completed*). Reserved seats are automatically released back to the inventory pool upon cancellation.
- **Dynamic Fare Computation**: Utilizes the **Decorator Pattern** to calculate live fares with stackable add-on services (Travel Insurance, Meal combos, Extra Luggage, Priority Boarding).
- **Fluent Construction**: Uses the **Builder Pattern** for step-by-step booking construction, validation, and automated e-ticket generation.
- **Separated UI Concerns**: Clean modular architecture where UI panels (*RouteSelectorPanel, BusSeatLayoutGrid, PassengerFormPanel, AddonServicesPanel, BookingSummaryPanel*) are decoupled into autonomous components, eliminating unnecessary navigation steps and maximizing extensibility.

---

## 🏗️ Design Patterns Implementation

```
+---------------------------------------------------------------------------------+
|                       DESIGN PATTERNS ARCHITECTURE MAP                          |
+---------------------------------------------------------------------------------+
|                                                                                 |
|  [ FACTORY PATTERN ]               [ DECORATOR PATTERN ]                        |
|  +--------------------+            +---------------------------------------+    |
|  |     BusFactory     |            |              TicketItem               |    |
|  +--------------------+            +---------------------------------------+    |
|            |                                           ^                        |
|    +-------+-------+                                   |                        |
|    |       |       |                       +-----------+-----------+            |
|    v       v       v                       |                       |            |
| Standard Sleeper Limousine             BaseTicket            TicketDecorator    |
|   Bus     Bus     Bus                                              ^            |
|                                                    +---------------+---+        |
|                                                    |       |       |   |        |
|                                                Insurance  Meal  Luggage VIP     |
|                                                                                 |
|  [ STATE PATTERN ]                 [ BUILDER PATTERN ]                          |
|  +--------------------+            +---------------------------------------+    |
|  |    BookingState    |            |            BookingBuilder             |    |
|  +--------------------+            +---------------------------------------+    |
|     ^        ^                             |                                    |
|     |        +--------------+              |  .setTrip(...)                     |
|  Pending -> Confirmed -> Cancelled         |  .setPassenger(...)                |
|                  |                         |  .setSelectedSeats(...)            |
|                  +------> Completed        |  .setInsurance(true)...            |
|                                            +---------> [ Builds Booking ]       |
+---------------------------------------------------------------------------------+
```

### 1. 🏭 Factory Pattern (`com.busticket.patterns.factory`)
- **Problem**: Constructing distinct bus categories requires different seat numbers, layout tiers (1 deck vs 2 decks), luxury surcharges, and amenities.
- **Solution**: `BusFactory.createBus(BusType, busId, licensePlate)` encapsulates instantiation logic.
  - `StandardBus`: 32 seats, single deck, 2x2 layout.
  - `SleeperBus`: 36 berths, 2 decks (Lower & Upper), 3 columns.
  - `VipLimousineBus`: 22 private luxury VIP cabins, 2 decks with individual amenities.

### 2. 🎀 Decorator Pattern (`com.busticket.patterns.decorator`)
- **Problem**: Passengers can choose any combination of optional services without altering base ticket pricing logic.
- **Solution**: Wraps `TicketItem` instances dynamically:
  - `BaseTicket`: Core route fare multiplied by vehicle class rate.
  - `InsuranceDecorator`: +50,000 VND (Accident & travel insurance).
  - `MealDecorator`: +75,000 VND (Hot meal & beverage combo).
  - `ExtraLuggageDecorator`: +40,000 VND (+10kg baggage allowance).
  - `PriorityBoardingDecorator`: +30,000 VND (VIP lounge access & fast-track check-in).

### 3. 🔄 State Pattern (`com.busticket.patterns.state`)
- **Problem**: Bookings transition through different operational states with distinct rules (e.g., cancelled tickets cannot be paid; cancellations must release seats).
- **Solution**: `Booking` delegates lifecycle methods (`pay()`, `cancel()`, `complete()`) to polymorphic `BookingState` objects:
  - `PendingState`: Initial reservation pending payment.
  - `ConfirmedState`: Payment confirmed; seats locked.
  - `CancelledState`: Reservation cancelled; seats returned to `AVAILABLE` pool automatically.
  - `CompletedState`: Journey completed.

### 4. 🔨 Builder Pattern (`com.busticket.patterns.builder`)
- **Problem**: Constructing a `Booking` requires trips, passenger details, seat lists, add-on options, payment methods, and ID generation.
- **Solution**: `BookingBuilder` provides a fluent, chained API with validation:
```java
Booking booking = new BookingBuilder()
        .setTrip(selectedTrip)
        .setPassenger(passenger)
        .setSelectedSeats(selectedSeats)
        .setInsurance(true)
        .setMeal(true)
        .setPaymentMethod("VietQR")
        .build();
```

---

## 📐 SOLID Principles Alignment

| Principle | Application in this Project |
| :--- | :--- |
| **S - Single Responsibility** | Entities hold data (`Model`), services govern business rules (`Service`), and visual panels manage display and user interaction (`UI`). |
| **O - Open / Closed** | Adding a new bus category (e.g., `ElectricBus`) or a new add-on service requires zero modifications to existing classes. |
| **L - Liskov Substitution** | Any `Bus` or `TicketDecorator` can substitute their respective base classes without breaking behavior. |
| **I - Interface Segregation** | Fine-grained interfaces (`TicketItem`, `BookingState`) ensure implementing classes only depend on methods they need. |
| **D - Dependency Inversion** | Controllers and decorators depend on abstractions rather than concrete classes. |

---

## 📁 Project Directory Structure

```
online-bus-ticket-system/
├── pom.xml                                   # Standard Maven build configuration
├── compile.bat                               # 1-Click compiler script (Pure OpenJDK)
├── run.bat                                   # 1-Click launch script
├── test.bat                                  # 1-Click design patterns verification
├── .gitignore                                # Git ignore configuration
├── README.md                                 # Documentation & architecture guide
└── src/
    └── main/
        └── java/
            └── com/
                └── busticket/
                    ├── Main.java             # Main application entry point
                    ├── model/                # Core domain models
                    │   ├── Bus.java
                    │   ├── BusType.java
                    │   ├── Seat.java
                    │   ├── SeatStatus.java
                    │   ├── Route.java
                    │   ├── Trip.java
                    │   ├── Passenger.java
                    │   └── Booking.java
                    ├── patterns/             # Gang of Four Design Patterns
                    │   ├── factory/          # Factory Pattern (Bus models)
                    │   │   ├── BusFactory.java
                    │   │   ├── StandardBus.java
                    │   │   ├── SleeperBus.java
                    │   │   └── VipLimousineBus.java
                    │   ├── decorator/        # Decorator Pattern (Ticket Add-ons)
                    │   │   ├── TicketItem.java
                    │   │   ├── BaseTicket.java
                    │   │   ├── TicketDecorator.java
                    │   │   ├── InsuranceDecorator.java
                    │   │   ├── MealDecorator.java
                    │   │   ├── ExtraLuggageDecorator.java
                    │   │   └── PriorityBoardingDecorator.java
                    │   ├── state/            # State Pattern (Booking Lifecycle)
                    │   │   ├── BookingState.java
                    │   │   ├── PendingState.java
                    │   │   ├── ConfirmedState.java
                    │   │   ├── CancelledState.java
                    │   │   └── CompletedState.java
                    │   └── builder/          # Builder Pattern (Fluent Booking creation)
                    │       └── BookingBuilder.java
                    ├── service/              # Business logic & mock storage
                    │   ├── TripService.java
                    │   └── BookingService.java
                    ├── ui/                   # Modular Swing components
                    │   ├── MainFrame.java
                    │   ├── theme/
                    │   │   └── UITheme.java
                    │   ├── components/
                    │   │   ├── RouteSelectorPanel.java
                    │   │   ├── BusSeatLayoutGrid.java
                    │   │   ├── PassengerFormPanel.java
                    │   │   ├── AddonServicesPanel.java
                    │   │   └── BookingSummaryPanel.java
                    │   └── dialogs/
                    │       ├── TicketReceiptDialog.java
                    │       └── BookingHistoryDialog.java
                    └── test/
                        └── PatternVerificationTest.java # Automated verification suite
```

---

## 🚀 Getting Started & Execution

### Prerequisites
- **Java Development Kit (JDK 21 or higher)** installed and added to `PATH`.

### Quick 1-Click Run (Windows)
1. **Compile**: Double-click `compile.bat` (or execute `.\compile.bat`).
2. **Launch Application**: Double-click `run.bat` (or execute `.\run.bat`).
3. **Run Pattern Tests**: Double-click `test.bat` to run the automated verification suite.

### Standard Maven Build
```bash
# Compile and package
mvn clean package

# Run the generated jar
java -jar target/online-bus-ticket-system-1.0.0.jar
```

---

## 👨‍💻 Author

- **GitHub**: [@buu297](https://github.com/buu297)
- **Project**: [online-bus-ticket-system](https://github.com/buu297/online-bus-ticket-system)