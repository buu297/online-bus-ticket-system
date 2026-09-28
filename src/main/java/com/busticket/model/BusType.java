package com.busticket.model;

/**
 * Bus categories with distinct multiplier and amenities.
 */
public enum BusType {
    STANDARD("Ghế Ngồi Tiêu Chuẩn (29 - 40 chỗ)", 1.00, "Điều hòa, Nước suối, Wifi"),
    SLEEPER("Giường Nằm Cao Cấp (36 giường 2 tầng)", 1.25, "Giường nệm da, Chăn gối, Cổng sạc USB, Rèm riêng tư"),
    VIP_LIMOUSINE("Limousine Phòng Nằm VIP (22 phòng)", 1.60, "Phòng riêng biệt, Ghế massage, Màn hình LCD, Wifi 5G, Tai nghe cao cấp");

    private final String displayName;
    private final double priceMultiplier;
    private final String amenities;

    BusType(String displayName, double priceMultiplier, String amenities) {
        this.displayName = displayName;
        this.priceMultiplier = priceMultiplier;
        this.amenities = amenities;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getPriceMultiplier() {
        return priceMultiplier;
    }

    public String getAmenities() {
        return amenities;
    }
}
