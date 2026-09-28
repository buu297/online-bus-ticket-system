package com.busticket.ui.components;

import com.busticket.model.Trip;
import com.busticket.service.TripService;
import com.busticket.ui.theme.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

/**
 * Modular component for filtering routes and selecting scheduled trips.
 */
public class RouteSelectorPanel extends JPanel {
    private final TripService tripService;
    private final Consumer<Trip> onTripSelected;
    private JComboBox<String> originCombo;
    private JComboBox<String> destCombo;
    private JTable tripTable;
    private DefaultTableModel tableModel;
    private List<Trip> currentTrips;
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("vi-VN"));

    public RouteSelectorPanel(TripService tripService, Consumer<Trip> onTripSelected) {
        this.tripService = tripService;
        this.onTripSelected = onTripSelected;
        initUI();
        loadTrips();
    }

    private void initUI() {
        setLayout(new BorderLayout(8, 8));
        setBackground(UITheme.BG_CARD);
        setBorder(UITheme.createCardBorder());

        // Header and Filter bar
        JPanel topPanel = new JPanel(new BorderLayout(8, 8));
        topPanel.setOpaque(false);

        JLabel title = new JLabel("1. Tìm Kiếm Tuyến Đường & Chuyến Xe");
        title.setFont(UITheme.FONT_SUBHEADER);
        title.setForeground(UITheme.PRIMARY);
        topPanel.add(title, BorderLayout.NORTH);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        filterPanel.setOpaque(false);

        filterPanel.add(new JLabel("Điểm đi:"));
        originCombo = new JComboBox<>(tripService.getUniqueOrigins().toArray(new String[0]));
        originCombo.setFont(UITheme.FONT_REGULAR);
        filterPanel.add(originCombo);

        filterPanel.add(new JLabel("Điểm đến:"));
        destCombo = new JComboBox<>(tripService.getUniqueDestinations().toArray(new String[0]));
        destCombo.setFont(UITheme.FONT_REGULAR);
        filterPanel.add(destCombo);

        JButton filterBtn = UITheme.createStyledButton("Lọc Chuyến", UITheme.PRIMARY, Color.WHITE);
        filterBtn.addActionListener(e -> filterTrips());
        filterPanel.add(filterBtn);

        JButton resetBtn = UITheme.createStyledButton("Tất Cả", new Color(148, 163, 184), Color.WHITE);
        resetBtn.addActionListener(e -> {
            originCombo.setSelectedIndex(0);
            destCombo.setSelectedIndex(0);
            filterTrips();
        });
        filterPanel.add(resetBtn);

        topPanel.add(filterPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // Trips Table
        String[] columnNames = {"Mã Chuyến", "Tuyến Đường", "Giờ Xuất Phát", "Loại Xe", "Chỗ Trống", "Giá Từ"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tripTable = new JTable(tableModel);
        tripTable.setFont(UITheme.FONT_REGULAR);
        tripTable.setRowHeight(28);
        tripTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tripTable.getTableHeader().setFont(UITheme.FONT_BOLD);
        tripTable.getTableHeader().setBackground(new Color(241, 245, 249));

        // Center aligned columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tripTable.getColumnModel().getColumn(0).setPreferredWidth(75);
        tripTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tripTable.getColumnModel().getColumn(1).setPreferredWidth(160);
        tripTable.getColumnModel().getColumn(2).setPreferredWidth(95);
        tripTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tripTable.getColumnModel().getColumn(3).setPreferredWidth(160);
        tripTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        tripTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        tripTable.getColumnModel().getColumn(5).setPreferredWidth(100);

        tripTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tripTable.getSelectedRow();
                if (row >= 0 && row < currentTrips.size()) {
                    onTripSelected.accept(currentTrips.get(row));
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tripTable);
        scrollPane.setPreferredSize(new Dimension(550, 160));
        scrollPane.setBorder(BorderFactory.createLineBorder(UITheme.BORDER_COLOR));
        add(scrollPane, BorderLayout.CENTER);
    }

    private void filterTrips() {
        String origin = (String) originCombo.getSelectedItem();
        String dest = (String) destCombo.getSelectedItem();
        currentTrips = tripService.searchTrips(origin, dest);
        populateTable(currentTrips);
    }

    private void loadTrips() {
        currentTrips = tripService.getAllTrips();
        populateTable(currentTrips);
        if (!currentTrips.isEmpty()) {
            tripTable.setRowSelectionInterval(0, 0);
        }
    }

    private void populateTable(List<Trip> trips) {
        tableModel.setRowCount(0);
        for (Trip trip : trips) {
            tableModel.addRow(new Object[]{
                    trip.getTripId(),
                    trip.getRoute().getRouteName(),
                    trip.getDepartureTime() + " (" + trip.getTravelDate() + ")",
                    trip.getBus().getBusType().getDisplayName(),
                    trip.getAvailableSeats() + " / " + trip.getBus().getTotalSeats(),
                    CURRENCY_FORMAT.format(trip.calculateSeatPrice(null))
            });
        }
    }

    public Trip getSelectedTrip() {
        int row = tripTable.getSelectedRow();
        if (row >= 0 && row < currentTrips.size()) {
            return currentTrips.get(row);
        }
        return null;
    }

    public void refreshTable() {
        filterTrips();
    }
}
