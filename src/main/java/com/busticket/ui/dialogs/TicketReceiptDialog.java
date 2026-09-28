package com.busticket.ui.dialogs;

import com.busticket.model.Booking;
import com.busticket.ui.theme.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Modal dialog presenting a polished digital ticket receipt.
 */
public class TicketReceiptDialog extends JDialog {
    private final Booking booking;
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("vi-VN"));

    public TicketReceiptDialog(Frame parent, Booking booking) {
        super(parent, "Vé Điện Tử - " + booking.getBookingId(), true);
        this.booking = booking;
        initUI();
    }

    private void initUI() {
        setSize(480, 620);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());
        getContentPane().setBackground(UITheme.BG_LIGHT);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(20, 24, 20, 24));

        // Header Title
        JLabel titleLbl = new JLabel("PHIẾU ĐẶT VÉ ĐIỆN TỬ");
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLbl.setForeground(UITheme.PRIMARY);
        titleLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(titleLbl);

        JLabel subTitle = new JLabel("Online Bus Ticket Reservation System");
        subTitle.setFont(UITheme.FONT_SMALL);
        subTitle.setForeground(UITheme.TEXT_MUTED);
        subTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(subTitle);

        contentPanel.add(Box.createVerticalStrut(16));

        // Status Badge
        JPanel badgePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        badgePanel.setOpaque(false);
        JLabel badge = new JLabel(" " + booking.getState().getStatusName() + " ");
        badge.setFont(UITheme.FONT_BOLD);
        badge.setOpaque(true);
        badge.setBackground(Color.decode(booking.getState().getStatusColor()));
        badge.setForeground(Color.WHITE);
        badge.setBorder(new EmptyBorder(4, 12, 4, 12));
        badgePanel.add(badge);
        contentPanel.add(badgePanel);

        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(new JSeparator());
        contentPanel.add(Box.createVerticalStrut(10));

        // Info Rows
        contentPanel.add(createRow("Mã đặt vé:", booking.getBookingId(), true));
        contentPanel.add(createRow("Thời gian đặt:", booking.getFormattedBookingDate(), false));
        contentPanel.add(createRow("Tuyến xe:", booking.getTrip().getRoute().getRouteName(), true));
        contentPanel.add(createRow("Khởi hành:", booking.getTrip().getDepartureTime() + " - " + booking.getTrip().getTravelDate(), false));
        contentPanel.add(createRow("Loại xe:", booking.getTrip().getBus().getBusType().getDisplayName(), false));
        contentPanel.add(createRow("Biển số xe:", booking.getTrip().getBus().getLicensePlate(), false));
        contentPanel.add(createRow("Hành khách:", booking.getPassenger().getFullName(), true));
        contentPanel.add(createRow("Số điện thoại:", booking.getPassenger().getPhoneNumber(), false));
        contentPanel.add(createRow("Điểm đón:", booking.getPassenger().getPickupPoint(), false));
        contentPanel.add(createRow("Phương thức:", booking.getPaymentMethod(), false));

        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(new JSeparator());
        contentPanel.add(Box.createVerticalStrut(10));

        // Itemized Breakdown from Decorator Pattern
        JLabel breakdownTitle = new JLabel("Chi tiết cước phí (Decorator Pattern):");
        breakdownTitle.setFont(UITheme.FONT_BOLD);
        breakdownTitle.setForeground(UITheme.PRIMARY);
        contentPanel.add(breakdownTitle);
        contentPanel.add(Box.createVerticalStrut(6));

        for (String line : booking.getBreakdown()) {
            JLabel lblLine = new JLabel(line);
            lblLine.setFont(UITheme.FONT_SMALL);
            lblLine.setForeground(UITheme.TEXT_DARK);
            contentPanel.add(lblLine);
        }

        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(new JSeparator());
        contentPanel.add(Box.createVerticalStrut(10));

        // Total
        JPanel totalRow = new JPanel(new BorderLayout());
        totalRow.setOpaque(false);
        JLabel totalTxt = new JLabel("TỔNG TIỀN THANH TOÁN:");
        totalTxt.setFont(UITheme.FONT_SUBHEADER);
        JLabel totalVal = new JLabel(CURRENCY_FORMAT.format(booking.getTotalPrice()));
        totalVal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        totalVal.setForeground(UITheme.DANGER);
        totalRow.add(totalTxt, BorderLayout.WEST);
        totalRow.add(totalVal, BorderLayout.EAST);
        contentPanel.add(totalRow);

        JScrollPane scroll = new JScrollPane(contentPanel);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        add(scroll, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        btnPanel.setBackground(UITheme.BG_LIGHT);

        JButton printBtn = UITheme.createStyledButton("In / Lưu Vé", UITheme.PRIMARY, Color.WHITE);
        printBtn.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Vé " + booking.getBookingId() + " đã được xuất và gửi tới email: " + booking.getPassenger().getEmail(),
                "Thông Báo In Vé", JOptionPane.INFORMATION_MESSAGE));

        JButton closeBtn = UITheme.createStyledButton("Đóng", new Color(148, 163, 184), Color.WHITE);
        closeBtn.addActionListener(e -> dispose());

        btnPanel.add(printBtn);
        btnPanel.add(closeBtn);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private JPanel createRow(String label, String value, boolean isBold) {
        JPanel p = new JPanel(new BorderLayout(8, 2));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));

        JLabel l = new JLabel(label);
        l.setFont(isBold ? UITheme.FONT_BOLD : UITheme.FONT_REGULAR);
        l.setForeground(UITheme.TEXT_MUTED);

        JLabel v = new JLabel(value);
        v.setFont(isBold ? UITheme.FONT_BOLD : UITheme.FONT_REGULAR);
        v.setForeground(UITheme.TEXT_DARK);

        p.add(l, BorderLayout.WEST);
        p.add(v, BorderLayout.EAST);
        return p;
    }
}
