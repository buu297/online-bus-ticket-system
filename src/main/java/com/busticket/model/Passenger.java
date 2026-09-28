package com.busticket.model;

/**
 * Passenger contact and pickup information.
 */
public class Passenger {
    private final String fullName;
    private final String phoneNumber;
    private final String email;
    private final String pickupPoint;

    public Passenger(String fullName, String phoneNumber, String email, String pickupPoint) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.pickupPoint = pickupPoint;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getPickupPoint() {
        return pickupPoint;
    }

    @Override
    public String toString() {
        return fullName + " (" + phoneNumber + ") - Đón tại: " + pickupPoint;
    }
}
