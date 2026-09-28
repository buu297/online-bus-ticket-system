package com.busticket.model;

/**
 * Enumeration representing the reservation state of an individual seat.
 */
public enum SeatStatus {
    AVAILABLE("Còn trống", "#2ECC71"),
    SELECTED("Đang chọn", "#3498DB"),
    BOOKED("Đã đặt", "#E74C3C");

    private final String displayName;
    private final String hexColor;

    SeatStatus(String displayName, String hexColor) {
        this.displayName = displayName;
        this.hexColor = hexColor;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getHexColor() {
        return hexColor;
    }
}
