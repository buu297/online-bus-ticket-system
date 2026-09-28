package com.busticket.ui.dialogs;

import com.busticket.model.Booking;
import com.busticket.model.Seat;
import com.busticket.service.BookingService;
import com.busticket.ui.theme.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Modal dialog displaying the list of all customer bookings,
 * with search and ticket cancellation actions (State Pattern).
 */
public class BookingHistoryDialog extends JDialog {
    private final BookingService bookingService;
    private final Runnable onHistoryChanged;
    private JTable historyTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private List<Booking> currentList;
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("vi-VN"));

    public BookingHistoryDialog(Frame parent, BookingService bookingService, Runnable onHistoryChanged) {
        super(parent, "Lịch Sử Đặt Vé & Quản Lý Trạng Thái (State Pattern)", true);
        this.bookingService = bookingService;
        this.onHistoryChanged = onHistoryChanged;
        initUI();
        loadData();
    }

    private void initUI() {
        setSize(850, 520);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(UITheme.BG_LIGHT);

        // Top Filter Bar
        JPanel topPanel = new JPanel(new BorderLayout(8, 8));
        topPanel.setOpaque(false);
        topPanel.setBorder(new EmptyBorder(12, 16, 0, 16));

        JLabel title = new JLabel("Danh Sách Vé Đã Đặt (Quản lý trạng thái State Pattern)");
        title.setFont(UITheme.FONT_SUBHEADER);
        title.setForeground(UITheme.PRIMARY);
        topPanel.add(title, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        searchPanel.setOpaque(false);
        searchPanel.add(new JLabel("Tìm kiếm (Mã vé, SĐT, Tên khách):"));

        searchField = new JTextField(20);
        searchField.setFont(UITheme.FONT_REGULAR);
        searchPanel.add(searchField);

        JButton searchBtn = UITheme.createStyledButton("Tìm Kiếm", UITheme.PRIMARY, Color.WHITE);
        searchBtn.addActionListener(e -> performSearch());
        searchPanel.add(searchBtn);

        JButton refreshBtn = UITheme.createStyledButton("Làm Mới", new Color(148, 163, 184), Color.WHITE);
        refreshBtn.addActionListener(e -> {
            searchField.setText("");
            loadData();
        });
        searchPanel.add(refreshBtn);

        topPanel.add(searchPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"Mã Vé", "Hành Khách", "SĐT", "Tuyến Đường", "Ghế", "Tổng Tiền", "Trạng Thái (State)"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        historyTable = new JTable(tableModel);
        historyTable.setFont(UITheme.FONT_REGULAR);
        historyTable.setRowHeight(28);
        historyTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        historyTable.getTableHeader().setFont(UITheme.FONT_BOLD);
        historyTable.getTableHeader().setBackground(new Color(241, 245, 249));

        // Custom status badge renderer
        historyTable.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setFont(UITheme.FONT_BOLD);
                if (row < currentList.size()) {
                    Booking b = currentList.get(row);
                    lbl.setForeground(Color.decode(b.getState().getStatusColor()));
                }
                return lbl;
            }
        });

        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 16));
        add(scrollPane, BorderLayout.CENTER);

        // Bottom Actions
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(0, 16, 12, 16));

        JButton viewBtn = UITheme.createStyledButton("Xem Chi Tiết Vé", UITheme.PRIMARY, Color.WHITE);
        viewBtn.addActionListener(e -> viewDetails());

        JButton cancelBtn = UITheme.createStyledButton("Hủy Vé (Release Ghế)", UITheme.DANGER, Color.WHITE);
        cancelBtn.addActionListener(e -> cancelSelectedBooking());

        JButton closeBtn = UITheme.createStyledButton("Đóng", new Color(148, 163, 184), Color.WHITE);
        closeBtn.addActionListener(e -> dispose());

        bottomPanel.add(viewBtn);
        bottomPanel.add(cancelBtn);
        bottomPanel.add(closeBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadData() {
        currentList = bookingService.getAllBookings();
        populateTable(currentList);
    }

    private void performSearch() {
        currentList = bookingService.searchBookings(searchField.getText());
        populateTable(currentList);
    }

    private void populateTable(List<Booking> list) {
        tableModel.setRowCount(0);
        for (Booking b : list) {
            String seatList = b.getSeats().stream().map(Seat::getSeatNumber).collect(Collectors.joining(", "));
            tableModel.addRow(new Object[]{
                    b.getBookingId(),
                    b.getPassenger().getFullName(),
                    b.getPassenger().getPhoneNumber(),
                    b.getTrip().getRoute().getRouteName(),
                    seatList,
                    CURRENCY_FORMAT.format(b.getTotalPrice()),
                    b.getState().getStatusName()
            });
        }
    }

    private void viewDetails() {
        int row = historyTable.getSelectedRow();
        if (row < 0 || row >= currentList.size()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 vé trong bảng để xem chi tiết!", "Chưa Chọn Vé", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Booking b = currentList.get(row);
        new TicketReceiptDialog((Frame) getParent(), b).setVisible(true);
    }

    private void cancelSelectedBooking() {
        int row = historyTable.getSelectedRow();
        if (row < 0 || row >= currentList.size()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 vé trong bảng để hủy!", "Chưa Chọn Vé", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Booking b = currentList.get(row);
        if (!b.getState().canCancel()) {
            JOptionPane.showMessageDialog(this,
                    "Không thể hủy vé này vì trạng thái hiện tại là: " + b.getState().getStatusName(),
                    "Không Thể Hủy", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn hủy vé " + b.getBookingId() + " không?\n"
                        + "Các ghế (" + b.getSeats().stream().map(Seat::getSeatNumber).collect(Collectors.joining(", ")) + ") sẽ được hoàn lại còn trống!",
                "Xác Nhận Hủy Vé", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            bookingService.cancelBooking(b.getBookingId());
            populateTable(currentList);
            if (onHistoryChanged != null) {
                onHistoryChanged.run();
            }
            JOptionPane.showMessageDialog(this, "Đã hủy vé thành công! Ghế đã được giải phóng.", "Hủy Thành Công", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
