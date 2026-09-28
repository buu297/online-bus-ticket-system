package com.busticket.ui.theme;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Aesthetic constants and UI helper utilities for modern Swing rendering.
 */
public class UITheme {
    // Color Palette
    public static final Color PRIMARY = new Color(30, 58, 138);       // Deep Navy
    public static final Color PRIMARY_HOVER = new Color(29, 78, 216); // Royal Blue
    public static final Color ACCENT = new Color(14, 165, 233);       // Sky Cyan
    public static final Color SUCCESS = new Color(16, 185, 129);      // Emerald Green
    public static final Color WARNING = new Color(245, 158, 11);      // Amber
    public static final Color DANGER = new Color(239, 68, 68);        // Rose Red
    public static final Color BG_LIGHT = new Color(248, 250, 252);    // Slate light
    public static final Color BG_CARD = Color.WHITE;
    public static final Color TEXT_DARK = new Color(15, 23, 42);      // Slate 900
    public static final Color TEXT_MUTED = new Color(100, 116, 139);  // Slate 500
    public static final Color BORDER_COLOR = new Color(226, 232, 240);// Slate 200

    // Seat Status Colors
    public static final Color SEAT_AVAILABLE = new Color(240, 253, 244);
    public static final Color SEAT_AVAILABLE_BORDER = new Color(34, 197, 94);
    public static final Color SEAT_SELECTED = new Color(59, 130, 246);
    public static final Color SEAT_BOOKED = new Color(254, 226, 226);
    public static final Color SEAT_BOOKED_TEXT = new Color(185, 28, 28);

    // Fonts
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_SUBHEADER = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);

    public static Border createCardBorder() {
        return BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(12, 14, 12, 14)
        );
    }

    public static JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BOLD);
        btn.setBackground(bgColor);
        btn.setForeground(fgColor);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(bgColor.darker(), 1, true),
                new EmptyBorder(8, 16, 8, 16)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
