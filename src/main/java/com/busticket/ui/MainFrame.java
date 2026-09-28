package com.busticket.ui;

import com.busticket.model.Booking;
import com.busticket.model.Passenger;
import com.busticket.model.Seat;
import com.busticket.model.Trip;
import com.busticket.patterns.builder.BookingBuilder;
import com.busticket.service.BookingService;
import com.busticket.service.TripService;
import com.busticket.ui.components.*;
import com.busticket.ui.dialogs.BookingHistoryDialog;
import com.busticket.ui.dialogs.TicketReceiptDialog;
import com.busticket.ui.theme.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * Main application window integrating all modular UI components.
 * Adheres to Single Responsibility and Separation of Concerns.
 */
public class MainFrame extends JFrame {
    private final TripService tripService;
    private final BookingService bookingService;

    private RouteSelectorPanel routePanel;
    private BusSeatLayoutGrid seatGrid;
    private PassengerFormPanel passengerPanel;
    private AddonServicesPanel addonPanel;
    private BookingSummaryPanel summaryPanel;
    private JButton historyBtn;

    private Trip selectedTrip;
    private List<Seat> selectedSeats;

    public MainFrame() {
        super("Online Bus Ticket Reservation System - Đặt Vé Xe Khách Trực Tuyến");
        this.tripService = new TripService();
        this.bookingService = BookingService.getInstance();
        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1240, 800);
        setMinimumSize(new Dimension(1080, 720));
        setLocationRelativeTo(null);
        getContentPane().setBackground(UITheme.BG_LIGHT);
        setLayout(new BorderLayout());

        // 1. Top Navigation Bar
        add(createTopNavBar(), BorderLayout.NORTH);

        // 2. Central Split Dashboard
        JPanel dashboard = new JPanel(new GridLayout(1, 2, 16, 0));
        dashboard.setOpaque(false);
        dashboard.setBorder(new EmptyBorder(14, 16, 14, 16));

        // Left Column: Trips & Seat Layout
        JPanel leftCol = new JPanel();
        leftCol.setLayout(new BoxLayout(leftCol, BoxLayout.Y_AXIS));
        leftCol.setOpaque(false);

        routePanel = new RouteSelectorPanel(tripService, this::onTripChanged);
        seatGrid = new BusSeatLayoutGrid(this::onSeatSelectionChanged);

        leftCol.add(routePanel);
        leftCol.add(Box.createVerticalStrut(12));
        leftCol.add(seatGrid);

        // Right Column: Passenger, Decorator Add-ons, Summary & Booking Action
        JPanel rightCol = new JPanel();
        rightCol.setLayout(new BoxLayout(rightCol, BoxLayout.Y_AXIS));
        rightCol.setOpaque(false);

        passengerPanel = new PassengerFormPanel();
        addonPanel = new AddonServicesPanel(this::recalculateSummary);
        summaryPanel = new BookingSummaryPanel(this::handleConfirmBooking, this::handleReset);

        rightCol.add(passengerPanel);
        rightCol.add(Box.createVerticalStrut(10));
        rightCol.add(addonPanel);
        rightCol.add(Box.createVerticalStrut(10));
        rightCol.add(summaryPanel);

        dashboard.add(leftCol);
        dashboard.add(rightCol);

        add(dashboard, BorderLayout.CENTER);

        // Auto select first trip on launch
        Trip initialTrip = routePanel.getSelectedTrip();
        if (initialTrip != null) {
            onTripChanged(initialTrip);
        }
    }

    private JPanel createTopNavBar() {
        JPanel nav = new JPanel(new BorderLayout());
        nav.setBackground(UITheme.PRIMARY);
        nav.setBorder(new EmptyBorder(12, 20, 12, 20));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel brand = new JLabel("🚌 ONLINE BUS TICKET RESERVATION SYSTEM");
        brand.setFont(new Font("Segoe UI", Font.BOLD, 19));
        brand.setForeground(Color.WHITE);

        JLabel sub = new JLabel("Java Swing Desktop App • OOP & SOLID • 4 Design Patterns (Factory, Decorator, State, Builder)");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(new Color(224, 231, 255));

        titlePanel.add(brand);
        titlePanel.add(Box.createVerticalStrut(2));
        titlePanel.add(sub);
        nav.add(titlePanel, BorderLayout.WEST);

        // Right action buttons
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 4));
        actions.setOpaque(false);

        JButton patternsInfoBtn = UITheme.createStyledButton("Kiến Trúc & Patterns", new Color(37, 99, 235), Color.WHITE);
        patternsInfoBtn.addActionListener(e -> showPatternsDialog());

        historyBtn = UITheme.createStyledButton("📋 Lịch Sử Đặt Vé (" + bookingService.getAllBookings().size() + ")", UITheme.WARNING, Color.WHITE);
        historyBtn.addActionListener(e -> openBookingHistory());

        actions.add(patternsInfoBtn);
        actions.add(historyBtn);
        nav.add(actions, BorderLayout.EAST);

        return nav;
    }

    private void onTripChanged(Trip trip) {
        this.selectedTrip = trip;
        seatGrid.displayTripSeats(trip);
        recalculateSummary();
    }

    private void onSeatSelectionChanged(List<Seat> seats) {
        this.selectedSeats = seats;
        recalculateSummary();
    }

    private void recalculateSummary() {
        summaryPanel.updateSummary(
                selectedTrip,
                selectedSeats,
                addonPanel.isInsuranceSelected(),
                addonPanel.isMealSelected(),
                addonPanel.isExtraLuggageSelected(),
                addonPanel.isPriorityBoardingSelected()
        );
    }

    private void handleConfirmBooking() {
        try {
            if (selectedTrip == null) {
                throw new IllegalStateException("Vui lòng chọn chuyến xe trước.");
            }
            if (selectedSeats == null || selectedSeats.isEmpty()) {
                throw new IllegalStateException("Vui lòng chọn ít nhất 1 chỗ ngồi trên sơ đồ xe.");
            }

            Passenger passenger = passengerPanel.getPassenger();

            // Assemble Booking using Builder Pattern
            Booking booking = new BookingBuilder()
                    .setTrip(selectedTrip)
                    .setPassenger(passenger)
                    .setSelectedSeats(selectedSeats)
                    .setInsurance(addonPanel.isInsuranceSelected())
                    .setMeal(addonPanel.isMealSelected())
                    .setExtraLuggage(addonPanel.isExtraLuggageSelected())
                    .setPriorityBoarding(addonPanel.isPriorityBoardingSelected())
                    .setPaymentMethod(summaryPanel.getSelectedPaymentMethod())
                    .build();

            // Save and confirm via BookingService (State transition to ConfirmedState)
            bookingService.confirmBooking(booking);

            // Update UI
            updateHistoryCounter();
            routePanel.refreshTable();
            seatGrid.displayTripSeats(selectedTrip);

            // Show Ticket Receipt Modal
            TicketReceiptDialog receipt = new TicketReceiptDialog(this, booking);
            receipt.setVisible(true);

            // Reset form for next booking
            handleReset();

        } catch (IllegalArgumentException | IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi Đặt Vé", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Có lỗi xảy ra: " + ex.getMessage(), "Lỗi Hệ Thống", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void handleReset() {
        seatGrid.clearSelection();
        addonPanel.reset();
        selectedSeats = null;
        recalculateSummary();
    }

    private void openBookingHistory() {
        BookingHistoryDialog historyDialog = new BookingHistoryDialog(this, bookingService, () -> {
            updateHistoryCounter();
            routePanel.refreshTable();
            if (selectedTrip != null) {
                seatGrid.displayTripSeats(selectedTrip);
            }
        });
        historyDialog.setVisible(true);
    }

    private void updateHistoryCounter() {
        historyBtn.setText("📋 Lịch Sử Đặt Vé (" + bookingService.getAllBookings().size() + ")");
    }

    private void showPatternsDialog() {
        String msg = "<html><body style='width: 480px; font-family: Segoe UI, sans-serif; padding: 10px;'>"
                + "<h2 style='color: #1E3A8A;'>Kiến Trúc & 4 Design Patterns Áp Dụng:</h2>"
                + "<hr>"
                + "<p><b>1. Factory Pattern (Gói com.busticket.patterns.factory):</b><br>"
                + "Khởi tạo linh hoạt các dòng xe (<i>StandardBus, SleeperBus, VipLimousineBus</i>) theo cấu hình ghế, tầng và tiện ích riêng biệt mà không làm thay đổi mã nguồn client.</p>"
                + "<p><b>2. Decorator Pattern (Gói com.busticket.patterns.decorator):</b><br>"
                + "Cộng dồn động các dịch vụ gia tăng (<i>Bảo hiểm, Suất ăn, Hành lý 10kg, Ưu tiên boarding</i>) vào vé cơ bản (<i>BaseTicket</i>) theo đúng nguyên lý Open/Closed (OCP).</p>"
                + "<p><b>3. State Pattern (Gói com.busticket.patterns.state):</b><br>"
                + "Quản lý vòng đời trạng thái đặt vé (<i>PendingState &#10132; ConfirmedState &#10132; CancelledState / CompletedState</i>). Khi hủy vé, ghế được tự động giải phóng về trạng thái còn trống.</p>"
                + "<p><b>4. Builder Pattern (Gói com.busticket.patterns.builder):</b><br>"
                + "Xây dựng đối tượng đặt vé <i>Booking</i> từng bước linh hoạt, kiểm tra tính hợp lệ dữ liệu và tự động lắp ghép Decorators.</p>"
                + "<p><b>5. Nguyên lý SOLID & Modular UI:</b><br>"
                + "Tách biệt rõ ràng Model - Service - Modular UI Panels giúp dễ bảo trì và mở rộng không giới hạn.</p>"
                + "</body></html>";

        JOptionPane.showMessageDialog(this, msg, "Kiến Trúc & Design Patterns", JOptionPane.INFORMATION_MESSAGE);
    }
}
