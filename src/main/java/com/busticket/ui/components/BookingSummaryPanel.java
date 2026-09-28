package com.busticket.ui.components;

import com.busticket.model.Seat;
import com.busticket.model.Trip;
import com.busticket.patterns.decorator.*;
import com.busticket.ui.theme.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Modular component displaying real-time fare computation and final booking actions.
 */
public class BookingSummaryPanel extends JPanel {
    private final Runnable onConfirmBooking;
    private final Runnable onReset;

    private JLabel routeLabel;
    private JLabel seatsLabel;
    private JLabel seatCostLabel;
    private JLabel addonsLabel;
    private JLabel totalAmountLabel;
    private JComboBox<String> paymentMethodCombo;
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("vi-VN"));

    public BookingSummaryPanel(Runnable onConfirmBooking, Runnable onReset) {
        this.onConfirmBooking = onConfirmBooking;
        this.onReset = onReset;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(8, 8));
        setBackground(UITheme.BG_CARD);
        setBorder(UITheme.createCardBorder());

        JLabel title = new JLabel("5. Chi Tiết Đặt Chỗ & Thanh Toán (Builder Pattern)");
        title.setFont(UITheme.FONT_SUBHEADER);
        title.setForeground(UITheme.PRIMARY);
        add(title, BorderLayout.NORTH);

        // Details Breakdown Panel
        JPanel detailsPanel = new JPanel(new GridLayout(5, 2, 6, 6));
        detailsPanel.setOpaque(false);
        detailsPanel.setBorder(new EmptyBorder(6, 4, 6, 4));

        detailsPanel.add(createKeyLabel("Chuyến xe:"));
        routeLabel = createValLabel("Chưa chọn");
        detailsPanel.add(routeLabel);

        detailsPanel.add(createKeyLabel("Ghế đã chọn:"));
        seatsLabel = createValLabel("Chưa chọn ghế");
        detailsPanel.add(seatsLabel);

        detailsPanel.add(createKeyLabel("Tiền vé cơ bản:"));
        seatCostLabel = createValLabel("0 đ");
        detailsPanel.add(seatCostLabel);

        detailsPanel.add(createKeyLabel("Dịch vụ tiện ích:"));
        addonsLabel = createValLabel("Không");
        detailsPanel.add(addonsLabel);

        detailsPanel.add(createKeyLabel("Phương thức:"));
        String[] paymentOptions = {
                "Chuyển khoản VietQR 24/7",
                "Tiền mặt khi lên xe",
                "Thẻ ATM / Thẻ tín dụng"
        };
        paymentMethodCombo = new JComboBox<>(paymentOptions);
        paymentMethodCombo.setFont(UITheme.FONT_REGULAR);
        detailsPanel.add(paymentMethodCombo);

        add(detailsPanel, BorderLayout.CENTER);

        // Bottom Actions & Total
        JPanel bottomPanel = new JPanel(new BorderLayout(8, 8));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(8, 0, 0, 0));

        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        totalPanel.setOpaque(false);
        JLabel totalText = new JLabel("TỔNG TIỀN:");
        totalText.setFont(UITheme.FONT_SUBHEADER);
        totalText.setForeground(UITheme.TEXT_DARK);

        totalAmountLabel = new JLabel("0 đ");
        totalAmountLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        totalAmountLabel.setForeground(UITheme.DANGER);

        totalPanel.add(totalText);
        totalPanel.add(totalAmountLabel);
        bottomPanel.add(totalPanel, BorderLayout.NORTH);

        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        btnPanel.setOpaque(false);

        JButton resetBtn = UITheme.createStyledButton("Hủy / Chọn Lại", new Color(148, 163, 184), Color.WHITE);
        resetBtn.addActionListener(e -> {
            if (onReset != null) onReset.run();
        });

        JButton confirmBtn = UITheme.createStyledButton("XÁC NHẬN ĐẶT VÉ", UITheme.SUCCESS, Color.WHITE);
        confirmBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        confirmBtn.addActionListener(e -> {
            if (onConfirmBooking != null) onConfirmBooking.run();
        });

        btnPanel.add(resetBtn);
        btnPanel.add(confirmBtn);
        bottomPanel.add(btnPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JLabel createKeyLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(UITheme.FONT_BOLD);
        lbl.setForeground(UITheme.TEXT_MUTED);
        return lbl;
    }

    private JLabel createValLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(UITheme.FONT_BOLD);
        lbl.setForeground(UITheme.TEXT_DARK);
        return lbl;
    }

    public void updateSummary(Trip trip, List<Seat> seats, boolean insurance, boolean meal, boolean luggage, boolean priority) {
        if (trip == null) {
            routeLabel.setText("Chưa chọn");
            seatsLabel.setText("Chưa chọn ghế");
            seatCostLabel.setText("0 đ");
            addonsLabel.setText("Không");
            totalAmountLabel.setText("0 đ");
            return;
        }

        routeLabel.setText(trip.getRoute().getRouteName() + " (" + trip.getDepartureTime() + ")");

        if (seats == null || seats.isEmpty()) {
            seatsLabel.setText("Chưa chọn ghế");
            seatCostLabel.setText("0 đ");
            addonsLabel.setText("Không");
            totalAmountLabel.setText("0 đ");
            return;
        }

        StringBuilder seatNums = new StringBuilder();
        for (int i = 0; i < seats.size(); i++) {
            seatNums.append(seats.get(i).getSeatNumber());
            if (i < seats.size() - 1) seatNums.append(", ");
        }
        seatsLabel.setText(seatNums.toString() + " (" + seats.size() + " chỗ)");

        // Calculate using Decorator Pattern
        TicketItem ticket = new BaseTicket(trip, seats);
        double baseCost = ticket.getCost();
        seatCostLabel.setText(CURRENCY_FORMAT.format(baseCost));

        int addonCount = 0;
        if (insurance) {
            ticket = new InsuranceDecorator(ticket);
            addonCount++;
        }
        if (meal) {
            ticket = new MealDecorator(ticket);
            addonCount++;
        }
        if (luggage) {
            ticket = new ExtraLuggageDecorator(ticket);
            addonCount++;
        }
        if (priority) {
            ticket = new PriorityBoardingDecorator(ticket);
            addonCount++;
        }

        if (addonCount > 0) {
            double addonTotal = ticket.getCost() - baseCost;
            addonsLabel.setText(String.format("%d dịch vụ (+%s)", addonCount, CURRENCY_FORMAT.format(addonTotal)));
        } else {
            addonsLabel.setText("Không chọn thêm");
        }

        totalAmountLabel.setText(CURRENCY_FORMAT.format(ticket.getCost()));
    }

    public String getSelectedPaymentMethod() {
        return (String) paymentMethodCombo.getSelectedItem();
    }
}
