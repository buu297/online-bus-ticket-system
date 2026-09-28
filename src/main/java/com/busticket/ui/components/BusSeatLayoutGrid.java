package com.busticket.ui.components;

import com.busticket.model.Bus;
import com.busticket.model.Seat;
import com.busticket.model.SeatStatus;
import com.busticket.model.Trip;
import com.busticket.ui.theme.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

/**
 * Visual interactive bus seat layout.
 * Supports single-deck and double-deck buses with visual status cues:
 * - Available: Soft green
 * - Selected: Vibrant blue
 * - Booked: Light red / disabled
 */
public class BusSeatLayoutGrid extends JPanel {
    private final Consumer<List<Seat>> onSeatSelectionChanged;
    private final List<Seat> selectedSeats = new ArrayList<>();
    private Trip currentTrip;
    private JPanel seatsContainer;
    private JTabbedPane deckTabbedPane;
    private JLabel infoLabel;
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("vi-VN"));

    public BusSeatLayoutGrid(Consumer<List<Seat>> onSeatSelectionChanged) {
        this.onSeatSelectionChanged = onSeatSelectionChanged;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(8, 8));
        setBackground(UITheme.BG_CARD);
        setBorder(UITheme.createCardBorder());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel title = new JLabel("2. Sơ Đồ Chọn Ghế Ngồi");
        title.setFont(UITheme.FONT_SUBHEADER);
        title.setForeground(UITheme.PRIMARY);

        infoLabel = new JLabel("Vui lòng chọn chuyến xe ở trên");
        infoLabel.setFont(UITheme.FONT_SMALL);
        infoLabel.setForeground(UITheme.TEXT_MUTED);

        headerPanel.add(title, BorderLayout.WEST);
        headerPanel.add(infoLabel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Center container for seats or tabs
        seatsContainer = new JPanel(new BorderLayout());
        seatsContainer.setOpaque(false);
        add(seatsContainer, BorderLayout.CENTER);

        // Footer Legend
        add(createLegendPanel(), BorderLayout.SOUTH);
    }

    private JPanel createLegendPanel() {
        JPanel legend = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 4));
        legend.setOpaque(false);
        legend.setBorder(new EmptyBorder(6, 0, 0, 0));

        legend.add(createLegendItem("Trống", UITheme.SEAT_AVAILABLE_BORDER, Color.WHITE));
        legend.add(createLegendItem("Đang chọn", UITheme.SEAT_SELECTED, UITheme.SEAT_SELECTED));
        legend.add(createLegendItem("Đã đặt", new Color(220, 38, 38), UITheme.SEAT_BOOKED));

        return legend;
    }

    private JPanel createLegendItem(String text, Color borderColor, Color fillColor) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        p.setOpaque(false);
        JPanel box = new JPanel();
        box.setPreferredSize(new Dimension(14, 14));
        box.setBackground(fillColor);
        box.setBorder(new LineBorder(borderColor, 1));
        JLabel lbl = new JLabel(text);
        lbl.setFont(UITheme.FONT_SMALL);
        lbl.setForeground(UITheme.TEXT_DARK);
        p.add(box);
        p.add(lbl);
        return p;
    }

    public void displayTripSeats(Trip trip) {
        this.currentTrip = trip;
        selectedSeats.clear();
        if (onSeatSelectionChanged != null) {
            onSeatSelectionChanged.accept(selectedSeats);
        }

        seatsContainer.removeAll();

        if (trip == null || trip.getBus() == null) {
            infoLabel.setText("Chưa chọn chuyến xe");
            seatsContainer.revalidate();
            seatsContainer.repaint();
            return;
        }

        Bus bus = trip.getBus();
        infoLabel.setText(String.format("%s | Biển số: %s | %d/%d ghế trống",
                bus.getBusType().getDisplayName(), bus.getLicensePlate(),
                bus.getAvailableSeatCount(), bus.getTotalSeats()));

        if (bus.getDecks() > 1) {
            deckTabbedPane = new JTabbedPane();
            deckTabbedPane.setFont(UITheme.FONT_BOLD);
            deckTabbedPane.addTab("Tầng 1 (Dưới)", createDeckPanel(bus, 1));
            deckTabbedPane.addTab("Tầng 2 (Trên)", createDeckPanel(bus, 2));
            seatsContainer.add(deckTabbedPane, BorderLayout.CENTER);
        } else {
            seatsContainer.add(createDeckPanel(bus, 1), BorderLayout.CENTER);
        }

        seatsContainer.revalidate();
        seatsContainer.repaint();
    }

    private JScrollPane createDeckPanel(Bus bus, int deck) {
        List<Seat> deckSeats = bus.getSeatsByDeck(deck);
        JPanel gridPanel = new JPanel();
        gridPanel.setOpaque(false);
        gridPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        int columns = (bus.getDecks() > 1) ? 3 : 4;
        gridPanel.setLayout(new GridLayout(0, columns, 8, 8));

        for (Seat seat : deckSeats) {
            JButton seatBtn = createSeatButton(seat);
            gridPanel.add(seatBtn);
        }

        JScrollPane scroll = new JScrollPane(gridPanel);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setPreferredSize(new Dimension(380, 210));
        return scroll;
    }

    private JButton createSeatButton(Seat seat) {
        double seatPrice = currentTrip.calculateSeatPrice(seat);
        String label = "<html><center><b>" + seat.getSeatNumber() + "</b><br><font size='2'>"
                + (seat.getStatus() == SeatStatus.BOOKED ? "Đã đặt" : (seat.getExtraFee() > 0 ? "+VIP" : ""))
                + "</font></center></html>";

        JButton btn = new JButton(label);
        btn.setFont(UITheme.FONT_REGULAR);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setToolTipText(String.format("Ghế %s - Giá: %s", seat.getSeatNumber(), CURRENCY_FORMAT.format(seatPrice)));

        updateSeatButtonStyle(btn, seat);

        btn.addActionListener(e -> {
            if (seat.getStatus() == SeatStatus.BOOKED) {
                JOptionPane.showMessageDialog(this,
                        "Ghế " + seat.getSeatNumber() + " đã có người đặt. Vui lòng chọn ghế khác!",
                        "Ghế Không Khả Dụng", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (selectedSeats.contains(seat)) {
                selectedSeats.remove(seat);
                seat.unselect();
            } else {
                selectedSeats.add(seat);
                seat.select();
            }

            updateSeatButtonStyle(btn, seat);

            if (onSeatSelectionChanged != null) {
                onSeatSelectionChanged.accept(new ArrayList<>(selectedSeats));
            }
        });

        return btn;
    }

    private void updateSeatButtonStyle(JButton btn, Seat seat) {
        if (seat.getStatus() == SeatStatus.BOOKED) {
            btn.setBackground(UITheme.SEAT_BOOKED);
            btn.setForeground(UITheme.SEAT_BOOKED_TEXT);
            btn.setBorder(new LineBorder(new Color(239, 68, 68), 1));
            btn.setEnabled(false);
        } else if (selectedSeats.contains(seat)) {
            btn.setBackground(UITheme.SEAT_SELECTED);
            btn.setForeground(Color.WHITE);
            btn.setBorder(new LineBorder(UITheme.PRIMARY, 2));
            btn.setEnabled(true);
        } else {
            btn.setBackground(UITheme.SEAT_AVAILABLE);
            btn.setForeground(UITheme.TEXT_DARK);
            btn.setBorder(new LineBorder(UITheme.SEAT_AVAILABLE_BORDER, 1));
            btn.setEnabled(true);
        }
    }

    public List<Seat> getSelectedSeats() {
        return new ArrayList<>(selectedSeats);
    }

    public void clearSelection() {
        for (Seat s : selectedSeats) {
            s.unselect();
        }
        selectedSeats.clear();
        if (currentTrip != null) {
            displayTripSeats(currentTrip);
        }
    }
}
